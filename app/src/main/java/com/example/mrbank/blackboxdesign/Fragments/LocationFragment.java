package com.example.mrbank.blackboxdesign.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mrbank.blackboxdesign.Database.Controllers.LocationController;
import com.example.mrbank.blackboxdesign.Database.Models.LocationDetails;
import com.example.mrbank.blackboxdesign.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {


    public static final String TAG = "Location";


    private EditText nameLocation;
    private Button saveLocation;
    private Button showLocation;


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private double currentLatitude;
    private double currentLongitude;





    public View v;


    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final LocationController locationController = new LocationController(getActivity());

        v = inflater.inflate(R.layout.map_fragment, container, false);
        nameLocation = (EditText) v.findViewById(R.id.nameofLocationEditText);
        showLocation = (Button) v.findViewById(R.id.showLocationButton);

        //Set up for broadcast
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("location");
        bManager.registerReceiver(bReceiver, intentFilter);


        showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<LocationDetails> showLocation = locationController.readLocationNames();

                for (int i =0; i<showLocation.size(); i++) {

                    Log.i(TAG, "id"+showLocation.get(i).getLocationId());
                    Log.i(TAG, "Location Name"+showLocation.get(i).getLocationName());
                    Log.i(TAG, "Latitude:"+showLocation.get(i).getLatitude());
                    Log.i(TAG,"Longtitude"+showLocation.get(i).getLongtitude());



                }




            }
            });




        saveLocation = (Button) v.findViewById(R.id.pinCurrentLocation);
        saveLocation.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // save location


                                                if (locationController.checkLocationIsExisting(nameLocation.getText().toString()))
                                                    Toast.makeText(getActivity(), "This name has already used", Toast.LENGTH_SHORT).show();
                                                else {
                                                    if (locationController.checkLocationIsExisting(currentLatitude, currentLongitude) != 0)
                                                        Toast.makeText(getActivity(), "This location has already pinned", Toast.LENGTH_SHORT).show();
                                                    else {
                                                        LocationDetails locationDetails = new LocationDetails(nameLocation.getText().toString(), currentLatitude, currentLongitude);
                                                        boolean success = locationController.createNewLocation(locationDetails);

                                                        if (success) {    //Toast.makeText(getBaseContext(),"Location has been pinned",Toast.LENGTH_SHORT);
                                                            Toast.makeText(getActivity(), "This location has saved", Toast.LENGTH_SHORT).show();
                                                        } else
                                                            Toast.makeText(getActivity(), "This location has not saved", Toast.LENGTH_SHORT).show();
                                                    }


                                                }


                                            }
                                        }
        );

        setUpMapIfNeeded();


        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onPause() {
        super.onPause();

    }
    private void setUpMap() {

    }

    private void handleNewLocation(double x,double y) {
        mMap.clear();
        LatLng latLng = new LatLng(x, y);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);

        float zoomLevel = (float) 17.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));


    }



    //@Override
    public void onMapReady(GoogleMap googleMap) {
        //Nothing

    }






    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("location")) {

                currentLatitude = intent.getExtras().getDouble("latitude");
                currentLongitude = intent.getExtras().getDouble("longitude");

                handleNewLocation(currentLatitude,currentLongitude);

            }
        }
    };

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();

            }
        }
    }

    public static String getDate(long milliSeconds)
    {
        String dateFormat = "dd/MM/yyyy HH:mm:ss a";


        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getDuration(long milliseconds)
    {

        int secs = (int) ((milliseconds / 1000) % 60);
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);

        String result = String.valueOf(hours)+"h:"+String.valueOf(minutes)+"m:"+String.valueOf(secs)+"s";

        return result;




    }



}
