package com.example.mohamed.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mohamed on 25/01/2016.
 */
public class GPS_Location extends MainActivity {

    private double latitude = 0.0;
    private double longitude = 0.0;
    LatLng latLng;
    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.gps_location, null, false);
        mDrawer.addView(contentView, 0);


        LocationManager manager = (LocationManager) this.getSystemService((Context.LOCATION_SERVICE));
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                latitude = location.getLatitude();
                longitude = location.getLongitude();

                TextView longi = (TextView) findViewById(R.id.textViewLong);
                TextView lati = (TextView) findViewById(R.id.textViewLat);
                String doubleLong = Double.toString(longitude);
                String doubleLat = Double.toString(latitude);
                longi.setText(doubleLong);
                lati.setText(doubleLat);

                ProgressBar pb = ((ProgressBar) findViewById(R.id.pb1));
                ProgressBar pb2 = ((ProgressBar) findViewById(R.id.pb2));

                pb.setVisibility(View.GONE);
                pb2.setVisibility(View.GONE);


                String doub = Double.toString(longitude);
                String doub2 = Double.toString(latitude);

                longi.setText(doub);
                lati.setText(doub2);
                getCompleteAddressString();

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
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
    }


    public void getCompleteAddressString() {


        TextView addressText = (TextView) findViewById(R.id.gps_address);
        TextView cityText = (TextView) findViewById(R.id.gps_city);
        TextView postcodeText = (TextView) findViewById(R.id.gps_postal);
        TextView countryText = (TextView) findViewById(R.id.gps_country);
        String s = "ff";
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String Address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();


        addressText.setText(Address);
        cityText.setText(city);
        postcodeText.setText(postalCode);
        countryText.setText(country);


    }


}
