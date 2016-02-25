package com.example.mohamed.maps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Mohamed on 25/01/2016.
 */
public class GPS_Location extends MainActivity implements LocationListener {

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.gps_location, null, false);
        mDrawer.addView(contentView, 0);
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);


       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {


        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onGPSBtnClick (View v){

        TextView longi = (TextView)findViewById(R.id.textViewLongitude);
        TextView lati = (TextView)findViewById(R.id.textViewLatitude);
        longi.setText("hello");
        lati.setText("ge");

    }





}
