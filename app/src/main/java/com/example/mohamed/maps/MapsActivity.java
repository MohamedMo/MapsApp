package com.example.mohamed.maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void onSearch(View v){

        EditText location_tf = (EditText)findViewById(R.id.textAddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        float zoomLevel = (float) 16.0; //This goes up to 21
        if (location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
               addressList = geocoder.getFromLocationName(location , 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

        }



    }

    public void onBtnVoiceClick (View v){

        if (v.getId() == R.id.btnToVoice){

            Intent i = new Intent(MapsActivity.this, Voice.class);
            startActivity(i);
        }
    }

    public void onBtnDirectionsClick (View v){

        if (v.getId() == R.id.btnToDirections){

            Intent i = new Intent(MapsActivity.this, Directions.class);
            startActivity(i);
        }
    }

}
