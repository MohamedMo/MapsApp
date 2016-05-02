package com.example.mohamed.maps;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed on 09/04/2016.
 */
public class MapHolder extends MainActivity implements OnMapReadyCallback {

    Polyline line; //added
    private ArrayAdapter<String> adapter;
    private ArrayList<String> list = new ArrayList<String>();
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.map_holder_view, null, false);
        mDrawer.addView(contentView, 0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDirections);
        mapFragment.getMapAsync(this);


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getIntent().getExtras().getStringArrayList("instruct"));
        ListView listView = (ListView) findViewById(R.id.listViewinstruct);
        listView.setAdapter(adapter);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        polylines();

    }

    public void polylines() {


        list = getIntent().getExtras().getStringArrayList("lines");

        System.out.println("map holder size" + list.size());

        for (int i = 0; i < list.size(); i++) {

            String polyline;
            polyline = list.get(i);
            decoderPoly(polyline);

        }


    }


    private void decoderPoly(String encoded) {

        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));


            poly.add(p);
            System.out.println("p =" + poly);
        }
        for (int j = 0; j < poly.size(); j++) {
            LatLng posisi = new LatLng(poly.get(j).latitude, poly.get(j).longitude);
            options.add(posisi);


        }

        line = mMap.addPolyline(options);


        LatLng startcam = new LatLng(poly.get(0).latitude, poly.get(0).longitude);


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(startcam) // Sets the center of the map to
                .zoom(14)                   // Sets the zoom
                .bearing(0) // Sets the orientation of the camera to east
                .tilt(0)    // Sets the tilt of the camera
                .build();    // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                cameraPosition));


    }

}
