package com.example.mohamed.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * Created by Mohamed on 10/03/2016.
 */
public class Bus extends MainActivity implements OnMapReadyCallback {


    public static final String api_key = "8b06798f5abf775a2973fc9d9970674d";
    public static final String app_key = "91a0f395";
    private static String URL = "http://transportapi.com/v3/uk/bus/stops/near.json?lat=51.527789&lon=-0.102323&page=3&rpp=10&api_key=d9307fd91b0247c607e098d5effedc97&app_id=03bf8009";
    private String newURL;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayList<String> localityItems = new ArrayList<String>();
    private ArrayList<String> journey = new ArrayList<String>();
    private ArrayList<String> endJourney = new ArrayList<String>();
    private ArrayList<String> durationArray = new ArrayList<String>();
    private ArrayList<String> busStops = new ArrayList<String>();
    private static String StartingLocation;
    private static String EndingLocation;
    private static ArrayAdapter<String> adapter;
    float zoomLevel = (float) 16.0;
    LatLng latLng;
    private ListView myList;
    private double latitude = 0.0;
    private double longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.bus_transport, null, false);
        mDrawer.addView(contentView, 0);


        if (!isNetworkOnline()) {
            Toast.makeText(this, "No internet connection ", Toast.LENGTH_LONG).show();
        }


        final TabHost host = (TabHost) findViewById(R.id.tabHost);


        host.setup();


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Search");
        spec.setContent(R.id.listViewBusJ);

        spec.setIndicator("Search");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Near Me");
        spec.setContent(R.id.listViewNear);
        spec.setIndicator("Near Me");
        host.addTab(spec);

        // //  final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        //  mapFragment.getMapAsync(this);

        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

        myList = (ListView)
                findViewById(R.id.listViewNear);
        myList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String item = (String) myList.getItemAtPosition(position);
                String localities = localityItems.get(position);


                Intent anotherActivityIntent = new Intent(Bus.this, PlacesHolder.class);
                anotherActivityIntent.putExtra("name", item);
                anotherActivityIntent.putExtra("local", localities);
                startActivity(anotherActivityIntent);


                System.out.println(item);
            }
        });


        LocationManager manager = (LocationManager) this.getSystemService((Context.LOCATION_SERVICE));
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //This goes up to 21
                //   latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //  mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                latitude = location.getLatitude();
                longitude = location.getLongitude();


                getNearby();


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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);


    }


    public void getNearby() {

        if (!isNetworkOnline()) {
            Toast.makeText(this, "No internet connection ", Toast.LENGTH_LONG).show();
        } else {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            String doubleLong = Double.toString(longitude);
            String doubleLat = Double.toString(latitude);

            URL = String
                    .format("http://transportapi.com/v3/uk/bus/stops/near.json?lat=%s&lon=%s&page=3&rpp=10&api_key=%s&app_id=%s", doubleLat, doubleLong,
                            api_key, app_key);
            System.out.println(URL);
            String result = null;


            try {
                java.net.URL url = new URL(URL);
                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();


                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sc = new StringBuilder();


                String line = null;

                while ((line = br.readLine()) != null) {

                    sc.append(line + "\n");

                }

                result = sc.toString();


                br.close();
                is.close();


            } catch (Exception e) {


            }


            try {


                JSONObject jsonResponse = new JSONObject(result);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("stops");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String name = jsonChildNode.optString("name");
                    String locality = jsonChildNode.optString("locality");

                    String outPut = name;
                    listItems.add(outPut);
                    localityItems.add(locality);


                }


                adapter = new
                        ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        listItems);
                myList = (ListView)
                        findViewById(R.id.listViewNear);


                myList.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }

    public void onBtnSearchBus(View v) {

        if (!isNetworkOnline()) {
            Toast.makeText(this, "No internet connection ", Toast.LENGTH_LONG).show();
        } else {
            EditText start = (EditText) findViewById(R.id.startBus);
            EditText end = (EditText) findViewById(R.id.endBus);
            //TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);
            String startingPos = start.getText().toString();
            String finishingPos = end.getText().toString();
            getJourney(startingPos, finishingPos);
        }
    }


    public void getJourney(String startbus, String endbus) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StartingLocation = startbus;
        EndingLocation = endbus;

        if (StartingLocation.equalsIgnoreCase("") || EndingLocation.equals("")) {

            Toast.makeText(Bus.this, "Please Enter Bus Stations", Toast.LENGTH_SHORT).show();
        } else {


            newURL = String.format("http://transportapi.com/v3/uk/public/journey/from/%s/to/%s.json?app_id=03bf8009&app_key=d9307fd91b0247c607e098d5effedc97&modes=bus&region=tfl",
                    StartingLocation, EndingLocation);

            newURL = newURL.replaceAll(" ", "%20");
            System.out.println(newURL);
            String result = null;


            try {
                java.net.URL url = new URL(newURL);
                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();


                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sc = new StringBuilder();


                String line = null;

                while ((line = br.readLine()) != null) {

                    sc.append(line + "\n");

                }

                result = sc.toString();


                br.close();
                is.close();


            } catch (Exception e) {

            }


            try {


                JSONObject mainObj = new JSONObject(result);
                if (mainObj != null) {
                    JSONArray list = mainObj.getJSONArray("routes");
                    if (list != null) {

                        JSONObject elem = list.getJSONObject(0);
                        if (elem != null) {
                            JSONArray prods = elem.getJSONArray("route_parts");
                            if (prods != null) {
                                for (int j = 0; j < prods.length(); j++) {
                                    JSONObject innerElem = prods.getJSONObject(j);
                                    if (innerElem != null) {
                                        String start = innerElem.optString("from_point_name");
                                        String end = innerElem.optString("to_point_name");
                                        String duration = innerElem.optString("duration");
                                        String busLine = innerElem.optString("line_name");

                                        String startloc = start;
                                        String endloct = end;
                                        String durloc = duration;
                                        String busL = busLine;

                                        journey.add(startloc);
                                        endJourney.add(endloct);
                                        durationArray.add(durloc);
                                        busStops.add(busL);


                                        System.out.println(startloc);
                                        System.out.println(endloct);
                                        System.out.println(durloc);
                                    }
                                }
                            }
                        }

                    }
                }


                adapter = new BusListAdapter(this, R.layout.custom_bus_layout, journey, endJourney, durationArray, busStops);
                ListView myList = (ListView) findViewById(R.id.listViewBusJ);
                myList.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Bus.this, "Enter Valid Bus Station", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
        latLng = new LatLng(51.5072, 0.1275);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }
}
