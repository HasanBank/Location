package com.example.mrbank.blackboxdesign.Fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mrbank.blackboxdesign.CustomListAdapter;
import com.example.mrbank.blackboxdesign.Database.Controllers.LocationController;
import com.example.mrbank.blackboxdesign.Database.Models.LocationReports;
import com.example.mrbank.blackboxdesign.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.inflate;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationHoursFragment extends Fragment {

    CalendarPickerView calendar;

    public static final String TAG = "Location";


    public LocationHoursFragment(CalendarPickerView calendar) {
       this.calendar = calendar;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LocationController locationController = new LocationController(getActivity());
        List<LocationReports> locationReport = new ArrayList<>();
        List<LocationReports> locationDailyReport = new ArrayList<>();
        List<LocationReports> locationTotal = new ArrayList<>();


        View view = inflater.inflate(R.layout.fragment_location_dates,container,false);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        TextView totalView = (TextView) view.findViewById(R.id.totalDurations);






        String[] dates = new String[calendar.getSelectedDates().size()];
        //String dates;
        //String[] dateDetails = new String[2048];
        String dateDetails;
        String dailyReport;
        //String[] dailyReport = new String[128];


        if(calendar.getSelectedDates().size()==1)
        {
        //for(int i =0; i< calendar.getSelectedDates().size() ; i++ ) {

            String details = null;
            String daily = null;
            dates[0] = calendar.getSelectedDates().get(0).toString();

            locationReport =locationController.readByDay(calendar.getSelectedDates().get(0));
            for (int j=0 ; j< locationReport.size(); j++)
            {
                details = details + LocationFragment.getDate(locationReport.get(j).getStartTimeInMiliSeconds());
                details = details + " ";
                details = details + LocationFragment.getDate(locationReport.get(j).getFinishTimeInMiliSeconds());
                details = details + " ";
                details = details + locationController.readLocationName(locationReport.get(j).getLocationId());
                details = details + "\n";
                Log.i("Location",details);


            }
            //dateDetails[i] = details;
            dateDetails = details;

            locationDailyReport = locationController.dailyReport(calendar.getSelectedDates().get(0));
            for (int k = 0; k < locationDailyReport.size(); k++ ){
                daily = daily + locationController.readLocationName(locationDailyReport.get(k).getLocationId());
                daily = daily + " ";
                daily = daily + LocationFragment.getDuration(locationDailyReport.get(k).getDurationInMiliSeconds());
                daily = daily + "\n";


            }

            //dailyReport[i] = daily;
            dailyReport = daily;



            CustomListAdapter adapter=new CustomListAdapter(getActivity(), dates, dateDetails,R.layout.locationrepotslistelement,dailyReport);

            listView.setAdapter(adapter);


        }
        else {


            List<java.util.Date> list = new ArrayList<>();
            list = getMinandMaxDate();
            Log.i(TAG, "Starting Date " + list.get(0) + "Finishing Date" + list.get(1));

            locationTotal = locationController.periodReport(list.get(0), list.get(1));
            String total = "";
            for (int i = 0; i < locationTotal.size(); i++) {
                total = total + locationController.readLocationName(locationTotal.get(i).getLocationId());
                total = total + " ";
                total = total + LocationFragment.getDuration(locationTotal.get(i).getDurationInMiliSeconds());
                total = total + "\n";

            }

            totalView.setText(total);
        }





        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.activity_listview,dates);


        //listView.setAdapter(adapter);




        return view;
    }

    public List<java.util.Date> getMinandMaxDate(){

        java.util.Date date1;
        java.util.Date date2;

        int size = calendar.getSelectedDates().size();



        List<java.util.Date> list = new ArrayList<>();

        date1 = calendar.getSelectedDates().get(0);
        date2 = calendar.getSelectedDates().get(size-1);


        if( date1.getTime() > date2.getTime()            )
        {
            list.add(0,date2);
            list.add(1,date1);


        }
        else
        {
            list.add(0,date1);
            list.add(1,date2);


        }



        return list;




    }






}

