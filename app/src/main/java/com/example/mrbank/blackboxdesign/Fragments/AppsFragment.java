package com.example.mrbank.blackboxdesign.Fragments;

import android.app.ListFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.example.mrbank.blackboxdesign.CustomListAdapter;
import com.example.mrbank.blackboxdesign.R;

import java.util.List;

/**
 * Created by mrbank on 12/04/16.
 */
public class AppsFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.apps_grid_view, container, false);

        GridView listView = (GridView)v.findViewById(R.id.list);
        CustomListAdapter adapter=new CustomListAdapter(getActivity(), itemname, imgid,R.layout.social);

        listView.setAdapter(adapter);


        return v;
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Social, android.R.layout.simple_list_item_1);




        CustomListAdapter adapter=new CustomListAdapter(getActivity(), itemname, imgid);
        setListAdapter(adapter);



        //list=(ListView)findViewById(R.id.list);
        //list.setAdapter(adapter);





    } */




    //Custom

    String[] itemname = {"08h:42m:05s","04h:32m:15s"};
    Integer[] imgid = {R.drawable.facebook,R.drawable.twitter};
}
