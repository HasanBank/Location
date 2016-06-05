package com.example.mrbank.blackboxdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mrbank.blackboxdesign.Fragments.AppsFragment;
import com.example.mrbank.blackboxdesign.Fragments.LocationReportsFragment;
import com.example.mrbank.blackboxdesign.Fragments.MainFragment;
import com.example.mrbank.blackboxdesign.Services.MapService;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        startService(new Intent(MainActivity.this, MapService.class));  //Map Service


        final MainFragment fragmentMain = new MainFragment();
        //final SituationFragment fragmentSituation = new SituationFragment();
        final LocationReportsFragment fragmentLocationReport = new LocationReportsFragment();


        openFragment(fragmentMain);




        NavigationView navigationView = (NavigationView)findViewById(R.id.main_drawer);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with MainFragment Which is our Inbox View;
                    case R.id.item_home:
                       openFragment(fragmentMain);
                        return true;
                    case R.id.item_location:
                        openFragment(fragmentLocationReport);
                        return true;
                    case R.id.item_situation:
                        //openFragment(fragmentSituation);
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.item_Social:
                        Toast.makeText(getApplicationContext(),"Social Selected",Toast.LENGTH_SHORT).show();
                        //MainFragment fragment = new MainFragment();

                        //android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        //fragmentTransaction.replace(R.id.frame,fragment);
                        //fragmentTransaction.addToBackStack(null);
                        //fragmentTransaction.commit();


                        AppsFragment fragmentApps = new AppsFragment();
                        openFragment(fragmentApps);

                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });












    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar , R.string.drawer_open,
                R.string.drawer_close);

    mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }








    public void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }






}
