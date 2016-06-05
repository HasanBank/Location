package com.example.mrbank.blackboxdesign.Database.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mrbank.blackboxdesign.Database.DatabaseHandler;
import com.example.mrbank.blackboxdesign.Database.Models.LocationDetails;
import com.example.mrbank.blackboxdesign.Database.Models.LocationReports;
import com.example.mrbank.blackboxdesign.Services.MapService;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by mrbank on 19/05/16.
 */
public class LocationController extends DatabaseHandler {

    public LocationController(Context context) {
        super(context);
    }

    public boolean createNewLocation(LocationDetails locationDetails) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DETAILS_NAME, locationDetails.getLocationName());
        values.put(DETAILS_LATITUDE, locationDetails.getLatitude());
        values.put(DETAILS_LONGTITUDE, locationDetails.getLongtitude());

        boolean createSuccesful = db.insert(TABLE_LOCATION_DETAILS, null, values) > 0;
        db.close();

        return createSuccesful;


    }

    //Update Location Names
    public void updateLocation(int id,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DETAILS_NAME,name);
        db.update(TABLE_LOCATION_DETAILS, values, DETAILS_ID + "='" + id + "'", null);

        //db.close();

    }




    public boolean createNewPeriod(LocationReports locationReports) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(locationReports.getStartTimeInMiliSeconds()!= 0 ) {
            values.put(REPORTS_START, locationReports.getStartTimeInMiliSeconds());
            values.put(REPORTS_FINISH, locationReports.getFinishTimeInMiliSeconds());
            values.put(REPORTS_DURATION, locationReports.getDurationInMiliSeconds());
            values.put(REPORTS_LID, locationReports.getLocationId());
        }
        else
        {
            values.put(REPORTS_START,locationReports.getStartTimeInMiliSeconds());
        }




        boolean createSuccesful = db.insert(TABLE_LOCATION_REPORTS, null, values) > 0;
        //db.close();

        return createSuccesful;


    }

    public int returnLastIndexoReports() {

        SQLiteDatabase db = this.getWritableDatabase();

       Cursor cursor = db.rawQuery("Select " + REPORTS_ID + " from " + TABLE_LOCATION_REPORTS + " Where "
                 + REPORTS_ID + " =(Select MAX(" + REPORTS_ID + ") from " + TABLE_LOCATION_REPORTS+")", null);


        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();

            int id = Integer.parseInt(cursor.getString(0));
            cursor.close();
            //db.close();
            return id;
            // return cursor.getInt(cursor.getColumnIndex(DETAILS_ID));


        }

        else{
            cursor.close();
            //db.close();
            return 0;
        }


    }



    public void updateLastPeriod(Long finishTime) {




        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int lastID = returnLastIndexoReports();

        LocationReports lastPeriod = returnLastPeriod();



        values.put(REPORTS_FINISH,finishTime);
        values.put(REPORTS_DURATION, finishTime - lastPeriod.getStartTimeInMiliSeconds());

        db.update(TABLE_LOCATION_REPORTS, values, REPORTS_ID + "='" + lastID + "'", null);

      //  db.close();


    }

    public void updateLastPeriod(LocationReports locationReports) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int lastID = returnLastIndexoReports();

        values.put(REPORTS_START,locationReports.getStartTimeInMiliSeconds());
        values.put(REPORTS_FINISH,locationReports.getFinishTimeInMiliSeconds());
        values.put(REPORTS_DURATION,locationReports.getDurationInMiliSeconds());
        values.put(REPORTS_LID,locationReports.getLocationId());


        db.update(TABLE_LOCATION_REPORTS, values, REPORTS_ID + "='" + lastID + "'", null);

        //db.close();


    }





    public LocationReports returnLastPeriod() {
        SQLiteDatabase db = this.getWritableDatabase();

        LocationReports lastPeriod;

        Cursor cursor = db.rawQuery("Select * from " + TABLE_LOCATION_REPORTS + " Where " + REPORTS_ID +
                "=(Select MAX(" + REPORTS_ID + ") from " + TABLE_LOCATION_REPORTS + ")", null);

        if( cursor.getCount() > 0  ) {

                cursor.moveToFirst();
                try{
                     lastPeriod = new LocationReports(Integer.parseInt(cursor.getString(0)),Long.parseLong(cursor.getString(1)), Long.parseLong(cursor.getString(2)),Integer.parseInt(cursor.getString(4)));
                }
                catch (NumberFormatException e){
                     lastPeriod = new LocationReports(Integer.parseInt(cursor.getString(0)));

                }
                cursor.close();
                //db.close();
                return  lastPeriod;


        }

        else
        {
            cursor.close();
            //db.close();
            return null;
        }










    }






    public List<LocationReports> readLocationReports() {
        List<LocationReports> locationReports = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM " + TABLE_LOCATION_REPORTS;
           ;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()    ) {
                do {
                    LocationReports details = new LocationReports();
                    details.setId(Integer.parseInt(cursor.getString(0)));
                    details.setStartTimeInMiliSeconds(Long.parseLong(cursor.getString(1)));
                    details.setDurationInMiliSeconds(Long.parseLong(cursor.getString(3)));
                    details.setFinishTimeInMiliSeconds(Long.parseLong(cursor.getString(2)));
                    details.setLocationId(Integer.parseInt(cursor.getString(4)));

                    locationReports.add(details);


                } while (cursor.moveToNext());
            }

            cursor.close();
            //db.close();


        } catch (Exception e) {

            Log.e("exception while reading", "" + e);
        }


        return locationReports;


    }


    public List<LocationDetails> readLocationNames() {

        Log.i(MapService.TAG, "inside of reading");

        List<LocationDetails> locationList = new ArrayList<>();
        try{

            String selectQuery = "SELECT * FROM " + TABLE_LOCATION_DETAILS;





            SQLiteDatabase db = this.getWritableDatabase();



            Log.i(MapService.TAG, "DB is "+db.isOpen());


            Cursor cursor = db.rawQuery(selectQuery,null);

            if( cursor.moveToFirst() ) {
                do{
                    LocationDetails details = new LocationDetails();

                    details.setId(Integer.parseInt(cursor.getString(0)));
                    details.setLocationName(cursor.getString(1));
                    details.setLatitude(Double.parseDouble(cursor.getString(2)));
                    details.setLongtitude(Double.parseDouble(cursor.getString(3)));


                    Log.i(MapService.TAG, "id" + cursor.getString(0));
                    Log.i(MapService.TAG, "name" + cursor.getString(1));
                    Log.i(MapService.TAG, "Latitude:" + cursor.getString(2));
                    Log.i(MapService.TAG, "Longtitude" + cursor.getString(3));

                    locationList.add(details);




                } while ( cursor.moveToNext());
            }

            cursor.close();
            db.close();

        }
        catch(SQLException e){
            Log.e("SQL Bomb",""+e);

        }
        catch (Exception e) {
            Log.e("exception while reading",""+e);
        }



        return locationList;


    }

    public List<LocationReports> dailyReport(Date date) {

        List<LocationReports> dailyReport = new ArrayList<>();

        Long ms = date.getTime();

        Long minumumMs = ms;        //minumum range of db tha will look over
        Long maximumMs = ms + (24 * 60 * 60 * 1000) ;

        SQLiteDatabase db = this.getWritableDatabase();

        try {


            Cursor cursor = db.rawQuery("Select " +REPORTS_LID+", SUM("+REPORTS_DURATION+") from " + TABLE_LOCATION_REPORTS + " Where " + REPORTS_START +
                    " >= '" + minumumMs + "' AND " + REPORTS_FINISH + " <= '" + maximumMs + "' GROUP BY "+REPORTS_LID, null);



            if (cursor.moveToFirst()) {
                do {
                    LocationReports reports = new LocationReports();



                    reports.setLocationId(Integer.parseInt(cursor.getString(0)));
                    reports.setDurationInMiliSeconds(Long.parseLong(cursor.getString(1)));

                    dailyReport.add(reports);


                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        }
        catch (Exception e) {
            Log.e("exception while reading",""+e);
        }


        return dailyReport;






    }


    public List<LocationReports> periodReport(Date dateStart,Date dateFinish){

        List<LocationReports> dailyReport = new ArrayList<>();

      /*  Long ms = dateStart.getTime();

        Long minumumMs = ms;        //minumum range of db tha will look over
        Long maximumMs = ms + (24 * 60 * 60 * 1000) ;               */


        Long minumumMs = dateStart.getTime();
        Long maximumMs = dateFinish.getTime() + (24*60*60*1000);



        SQLiteDatabase db = this.getWritableDatabase();

        try {

            if (cursor.moveToFirst()) {
                do {
                    LocationReports reports = new LocationReports();



                    reports.setLocationId(Integer.parseInt(cursor.getString(0)));
                    reports.setDurationInMiliSeconds(Long.parseLong(cursor.getString(1)));

                    dailyReport.add(reports);


                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        }
        catch (Exception e) {
            Log.e("exception while reading",""+e);
        }


        return dailyReport;


    }



    public List<LocationReports> readByDay(Date date) {

        List<LocationReports> locationReport = new ArrayList<>();

        Long ms = date.getTime();

        Long minumumMs = ms;        //minumum range of db tha will look over
        Long maximumMs = ms + (24 * 60 * 60 * 1000) ;




        SQLiteDatabase db = this.getWritableDatabase();

        try {


            Cursor cursor = db.rawQuery("Select * from " + TABLE_LOCATION_REPORTS + " Where " + REPORTS_START +
                    " >= '" + minumumMs + "' AND " + REPORTS_FINISH + " <= '" + maximumMs + "'", null);




            if (cursor.moveToFirst()) {
                do {
                    LocationReports reports = new LocationReports();

                    reports.setId(Integer.parseInt(cursor.getString(0)));
                    reports.setStartTimeInMiliSeconds(Long.parseLong(cursor.getString(1)));
                    reports.setFinishTimeInMiliSeconds(Long.parseLong(cursor.getString(2)));
                    reports.setDurationInMiliSeconds(Long.parseLong(cursor.getString(3)));
                    reports.setLocationId(Integer.parseInt(cursor.getString(4)));

                    locationReport.add(reports);


                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        }
        catch (Exception e) {
            Log.e("exception while reading",""+e);
        }


        return locationReport;




    }


    public String readLocationName(int id){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select " + DETAILS_NAME + " from " + TABLE_LOCATION_DETAILS + " Where " +
                DETAILS_ID + " = '" + id + "'", null);

        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();

            String name = cursor.getString(0);
            cursor.close();
            db.close();
            return name;
            // return cursor.getInt(cursor.getColumnIndex(DETAILS_ID));


        }

        else{
            cursor.close();
            db.close();
            return null;
        }



    }



    public boolean checkLocationIsExisting(String locationName) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from "+ TABLE_LOCATION_DETAILS + " Where " + DETAILS_NAME +
        "='"+locationName+"'",null);





        if( cursor.getCount() <= 0 ) {
            cursor.close();
            db.close();
            return false;
        }

        cursor.close();
        db.close();
        return true;


    }

    public int checkLocationIsExisting(Double locationLatitude,Double locationLongtitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select * from " + TABLE_LOCATION_DETAILS + " Where " + DETAILS_LATITUDE +
                " = '" + locationLatitude + "' AND " + DETAILS_LONGTITUDE + " = '" + locationLongtitude + "'", null);


        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();

            int ID = Integer.parseInt(cursor.getString(0));
            cursor.close();
            db.close();
            return ID;
           // return cursor.getInt(cursor.getColumnIndex(DETAILS_ID));


        }

        else{
            cursor.close();
            db.close();
            return 0;
        }

    }


    public SQLiteDatabase openDataBase() throws SQLException {
        //String myPath = DB_PATH + DB_NAME;

        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase("blackboxDesign", null,
                SQLiteDatabase.OPEN_READWRITE);

        return myDataBase;
    }


}



