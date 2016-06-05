package com.example.mrbank.blackboxdesign;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by mrbank on 12/04/16.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private int layout;

    private final String[] itemname;
    private final Integer[] imgid;

    private final String dates[];
    private final String details;
    private final String dailyReport;



    public CustomListAdapter(Activity context,String dates[],String details,int layout,String dailyReport ){

        super(context,R.layout.locationrepotslistelement,dates);


        itemname = null;
        imgid = null;

        this.dailyReport = dailyReport;
        this.layout = layout;
        this.context = context;
        this.dates = dates;
        this.details = details;

    }



    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid,int layout) {
        super(context, R.layout.social, itemname);
        // TODO Auto-generated constructor stub

        dates = null;
        details = null;
        dailyReport = null;


        this.layout = layout;
        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(layout, null, true);


        if (layout == R.layout.social) {
            TextView txtTitle = (TextView) rowView.findViewById(R.id.name_of_app);
            ImageButton imageView = (ImageButton) rowView.findViewById(R.id.icon_app);


            txtTitle.setText(itemname[position]);
            imageView.setImageResource(imgid[position]);

        } else {
            if (layout == R.layout.locationrepotslistelement) {
                TextView txtDate = (TextView) rowView.findViewById(R.id.dateName);
                TextView txtDetail = (TextView) rowView.findViewById(R.id.dateDetail);
                TextView txtDaily = (TextView) rowView.findViewById(R.id.dateDaily);

                txtDate.setText(dates[position]);
                //txtDetail.setText(details[position]);
                txtDetail.setText(details);
                txtDaily.setText(dailyReport);



            }
        }
        return rowView;

    }
}