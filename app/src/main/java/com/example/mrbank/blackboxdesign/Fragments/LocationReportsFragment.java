package com.example.mrbank.blackboxdesign.Fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mrbank.blackboxdesign.MainActivity;
import com.example.mrbank.blackboxdesign.R;
import com.squareup.timessquare.CalendarPickerView;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationReportsFragment extends Fragment {


    public final String TAGCALENDAR = "calendar";

    Button done;

    View v;


    CalendarPickerView calendar;


    public LocationReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_location_reports, container, false);




        Calendar previousMonth = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();

        previousMonth.add(Calendar.MONTH,-1);
        tomorrow.add(Calendar.DATE,1);



        calendar = (CalendarPickerView) v.findViewById(R.id.calendar_view);

        //Date today = new Date();



        calendar.init(previousMonth.getTime(),tomorrow.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);



        done = (Button) v.findViewById(R.id.calendar_Tick);
        done.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.i(TAGCALENDAR, calendar.getSelectedDates().toString());

                                          LocationHoursFragment dates = new LocationHoursFragment(calendar);
                                           openFragment(dates);





                                    }





        });


        return v;
    }


    public void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


}
