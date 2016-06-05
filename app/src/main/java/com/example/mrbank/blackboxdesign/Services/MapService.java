package com.example.mrbank.blackboxdesign.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.widget.Toast;

import com.example.mrbank.blackboxdesign.Database.Controllers.LocationController;
import com.example.mrbank.blackboxdesign.Database.Models.LocationReports;
import com.example.mrbank.blackboxdesign.Fragments.LocationFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.logging.LogRecord;

import static android.app.PendingIntent.getActivity;

public class MapService extends Service implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public LocationController locationController = new LocationController(this);

    public static double currentLatitude;
    public static double currentLongtitude;

    public double lastX;
    public double lastY;
    public int lastID;



    public Long currentTime;
    public long startingTime;   // to keep starting point of period
    public long finishingTime;    // what time period finished , it can update itself by every update interval
    public boolean flagWriteToDatabase;         // controlling when write to database



    public static final String TAG = "Location";

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;

    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 10;

    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 10;

    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    private GoogleApiClient locationClient;


    @Override
    public void onCreate() {
        super.onCreate();



        Log.i(TAG, "On Create'deyiz…");





        Log.i(TAG, "Location service created…");

        locationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks)this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)this)
                .addApi(LocationServices.API)
                .build();

        locationClient.connect();
    }

    // Unregister location listeners
    private void clearLocationData() {
        locationClient.disconnect();

        if (locationClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(locationClient, this);

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // When service destroyed we need to unbind location listeners
    /*@Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "Location service destroyed…");

        clearLocationData();
    } */



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Calling command…");

        return START_STICKY;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Location Callback. onConnected");

        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(locationClient);

        // Create the LocationRequest object
        LocationRequest locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationRequest.setInterval(UPDATE_INTERVAL);

        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationServices.FusedLocationApi.requestLocationUpdates(locationClient, locationRequest, this);

        onLocationChanged(currentLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    // Main Logic is this method
    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged");

        //finishingTime = System.currentTimeMillis();
        currentTime = System.currentTimeMillis();

        // to get just 3 digts after point  // sinan değerler çok hassas geliyor o yüzden kırpma şart oldu
        DecimalFormat df = new DecimalFormat("#.###");
        String dx=df.format(location.getLatitude());
        currentLatitude=Double.valueOf(dx);
        dx = df.format(location.getLongitude());
        currentLongtitude = Double.valueOf(dx);

        Log.i(TAG, "Now LOCATION: " + currentLatitude + ":" + currentLongtitude);


        int pinnedID = locationController.checkLocationIsExisting(currentLatitude,currentLongtitude);
        LocationReports lastPeriod = locationController.returnLastPeriod();
        if(  pinnedID != 0 )       // means this location pinned
        {
            Log.i(TAG, "LOCATION ID: " + pinnedID);

            if(  lastPeriod != null           ) {
                if (lastPeriod.getLocationId() == pinnedID) {
                    locationController.updateLastPeriod(currentTime);
                    Log.i(TAG, "LOCATION Continue: " + pinnedID);

                } else        // create new period
                {
                    LocationReports newPeriod = new LocationReports(currentTime, currentTime + UPDATE_INTERVAL, pinnedID);
                    // write it to DB
                    locationController.createNewPeriod(newPeriod);
                    Log.i(TAG, "New Period Created LOCATION ID: " + pinnedID);


                }

            }
            else
            {
                LocationReports newPeriod = new LocationReports(currentTime, currentTime + UPDATE_INTERVAL, pinnedID);
                // write it to DB
                locationController.createNewPeriod(newPeriod);
                Log.i(TAG, "New Period Created LOCATION ID: " + pinnedID);

            }

        }
        else
        {
            // lastPeriod = locationController.returnLastPeriod();
            if( lastPeriod != null) {
                if (lastPeriod.getStartTimeInMiliSeconds() != 0) {          // create new empty line
                    //create new row
                    LocationReports newPeriod = new LocationReports(0);
                    locationController.createNewPeriod(newPeriod);

                }
            }
            else
            {                                                               //create new empty line
                LocationReports newPeriod = new LocationReports(0);
                locationController.createNewPeriod(newPeriod);

            }





        }



/*
        if( (lastX != currentLatitude) | (lastY != currentLongtitude)  )        // means location changed
        {
            Log.i(TAG, "Değişim Var: " +pinnedID );
            // According to prior Location
            int lastID = locationController.checkLocationIsExisting(lastX,lastY);
            if( lastID != 0 )
            {                                                               // location changed and last point was pinned.So, write period to DB
                LocationReports newReport = new LocationReports(startingTime,finishingTime,lastID);



                boolean success = locationController.createNewPeriod(newReport);
                if (success )
                    Log.i(TAG, "Period was created "  );
                else
                    Log.i(TAG, "Period was not created "  );

                startingTime = 0;



            }






        }
        else
        {                               // location did not change



            Log.i(TAG, "LOCATION ID: " + pinnedID + "StartingTİme" + LocationFragment.getDate(startingTime) + "Finishing Time" + LocationFragment.getDate(finishingTime)+"Location"+locationController.readLocationName(pinnedID));
            Log.i(TAG, "LOCATION değişmedi,beklemedeyiz: ");



        }



        lastX = currentLatitude;
        lastY = currentLongtitude;   */

        // Since location information updated, broadcast it
        Intent broadcast = new Intent("location");

        // Set action so other parts of application can distinguish and use this information if needed
        //broadcast.setAction("location");
        broadcast.putExtra("latitude", currentLatitude);
        broadcast.putExtra("longitude", currentLongtitude);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);


    }







    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Location Callback. onConnectionFailed");
    }






}
