package com.example.mrbank.blackboxdesign.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrbank.blackboxdesign.MainActivity;
import com.example.mrbank.blackboxdesign.R;
import com.example.mrbank.blackboxdesign.Services.SituationService;

/**
 * Created by mrbank on 02/04/16.
 */
public class MainFragment extends Fragment{

    private Button addLocation;

    final LocationFragment locationFragment = new LocationFragment();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mainfragment,container,false);

        addLocation = (Button) v.findViewById(R.id.addLocation);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(locationFragment);

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
