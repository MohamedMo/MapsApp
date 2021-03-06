package com.example.mohamed.maps;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by Mohamed on 25/02/2016.
 */
public class MainActivity extends AppCompatActivity {


    protected DrawerLayout mDrawer;
    private DrawerLayout dlDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        dlDrawer.setDrawerListener(drawerToggle);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setupDrawerContent(nvDrawer);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {


        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                Intent intent = new Intent(this, MapsActivity.class);
                this.startActivity(intent);
                break;
            case R.id.nav_second_fragment:
                Intent intent1 = new Intent(this, GPS_Location.class);
                this.startActivity(intent1);
                break;
            case R.id.nav_third_fragment:
                Intent intent2 = new Intent(this, PlaceOfInterest.class);
                this.startActivity(intent2);
                break;
            case R.id.nav_fourth_fragment:
                Intent intent3 = new Intent(this, Bus.class);
                this.startActivity(intent3);
                break;
            case R.id.nav_fifth_fragment:
                Intent intent4 = new Intent(this, Trains.class);
                this.startActivity(intent4);
                break;
            case R.id.directions:
                Intent intent5 = new Intent(this, Directions.class);
                this.startActivity(intent5);
                break;
            case R.id.nav_sixth_fragment:
                Intent intent6 = new Intent(this, Status.class);
                this.startActivity(intent6);
                break;
            case R.id.nav_seventh_fragment:
                Intent intent7 = new Intent(this, Navigator.class);
                this.startActivity(intent7);
                break;
            case R.id.favourites_nav:
                Intent intent8 = new Intent(this, Favourites.class);
                this.startActivity(intent8);
                break;


        }


        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    public boolean onOptionsItemSelected(MenuItem item) {


        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


}