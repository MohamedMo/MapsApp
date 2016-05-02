package com.example.mohamed.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed on 24/01/2016.
 */
public class Directions extends MainActivity implements OnMapReadyCallback {



    private int MY_DATA_CHECK_CODE = 0;
    private int position = 0;

    private static String StartingLocation;
    private static String EndingLocation;
    private static String TravellingMethod;
    private static String URL;
    String method = "walking";
    private static ArrayList<String> Directions = new ArrayList<String>();
    private static ArrayList<String> DirectionssPolylines = new ArrayList<String>();
    public static final String GoogleAPIKey = "AIzaSyC2MMB-7iqMzrccgD9voZHiGf2nY093Jlg";
    List<LatLng> list;
    Polyline line;
    LatLng latLng;
    GoogleMap mMap;
    private double latitude = 0.0;
    private double longitude = 0.0;
   private ArrayList<Routes>routeList;
    private DirectionsAdapter adapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.directions, null, false);
        mDrawer.addView(contentView, 0);

        if(!isNetworkOnline()){
            Toast.makeText(this, "No internet connection ", Toast.LENGTH_LONG).show();
        }
        routeList = new ArrayList<Routes>();

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);






        ListView directionsList = (ListView)
                findViewById(R.id.listViewDir);

        directionsList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                ArrayList<String> instruct = new ArrayList<String>();
                ArrayList<String> lines = new ArrayList<String>();

                instruct = routeList.get(position).getInstructions();
                lines = routeList.get(position).getList();
                System.out.println(instruct);



                Intent anotherActivityIntent = new Intent(Directions.this, MapHolder.class);

                anotherActivityIntent.putExtra("instruct", instruct);
                anotherActivityIntent.putExtra("lines" , lines);

                startActivity(anotherActivityIntent);



            }
        });



        LocationManager manager = (LocationManager) this.getSystemService((Context.LOCATION_SERVICE));
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {




                latitude = location.getLatitude();
                longitude = location.getLongitude();

//                String Longitude = "-0.066720";
//                String Latitude = "51.526974";


                latLng = new LatLng
                        (latitude, longitude);





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









    public void onBtnCycle(View v){


            routeList.clear();
            Directions.clear();
            DirectionssPolylines.clear();


            method = "BICYCLING";
            EditText start = (EditText) findViewById(R.id.startLocation);
            EditText end = (EditText) findViewById(R.id.endLocation);
            String startingPos = start.getText().toString();
            String finishingPos = end.getText().toString();
            getNearby(startingPos, finishingPos, method);

    }

    public void onBtnDrive(View v){

            routeList.clear();
            Directions.clear();
            DirectionssPolylines.clear();


            method = "DRIVING";
            EditText start = (EditText) findViewById(R.id.startLocation);
            EditText end = (EditText) findViewById(R.id.endLocation);
            String startingPos = start.getText().toString();
            String finishingPos = end.getText().toString();
            getNearby(startingPos, finishingPos, method);

    }

    public void onWalkBtn(View v){


      
            routeList.clear();
            Directions.clear();
            DirectionssPolylines.clear();




            method = "WALKING";
            EditText start = (EditText) findViewById(R.id.startLocation);
            EditText end = (EditText) findViewById(R.id.endLocation);
            String startingPos = start.getText().toString();
            String finishingPos = end.getText().toString();
            getNearby(startingPos, finishingPos, method);
        
    }






    public void getNearby(String start, String end, String method) {

        if(!isNetworkOnline()){
            Toast.makeText(this, "No internet connection ", Toast.LENGTH_LONG).show();
        }
        else {

            Routes routeNum;

            JSONObject leg = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Directions = new ArrayList<String>();
            try {
                start = start.replaceAll("\\s", "");
                end = end.replaceAll("\\s", "");
                method = method.replaceAll("\\s", "");
            } catch (Exception e) {

            }

            StartingLocation = start;
            EndingLocation = end;
            TravellingMethod = method.toLowerCase();

            if (StartingLocation.equals("") || EndingLocation.equals("")) {
                Toast.makeText(this, "Please enter locations", Toast.LENGTH_LONG).show();


            } else {


                URL = String
                        .format("https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&mode=%s&alternatives=true&key=%s",
                                StartingLocation, EndingLocation, TravellingMethod,
                                GoogleAPIKey);
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

                    Toast.makeText(this, "Enter valid address", Toast.LENGTH_LONG).show();


                }


                try {



                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.getString("status");


                    if (error.equalsIgnoreCase("ok")) {


                        // routesArray contains ALL routes
                        JSONArray routesArray = jsonObject.getJSONArray("routes");


                        for (int i = 0; i < routesArray.length(); i++) {

                            routeNum = new Routes();
                            ArrayList<String> inst = new ArrayList<>();
                            ArrayList<String> routelines = new ArrayList<>();
                            List<LatLng> routepointLines = new ArrayList<>();
                            // Grab the first route
                            JSONObject route = routesArray.getJSONObject(i);

                            String summary = route.getString("summary");
                            routeNum.setSummary(summary);
                            //  System.out.println("summary = " + summary);

                            // Take all legs from the route
                            JSONArray legs = route.getJSONArray("legs");

                            // Grab first leg
                            leg = legs.getJSONObject(0);

                            JSONObject durationObject = leg.getJSONObject("duration");
                            String duration = durationObject.getString("text");
                            routeNum.setDuration(duration);

                            JSONObject distanceObject = leg.getJSONObject("distance");
                            String distance = distanceObject.getString("text");
                            routeNum.setDistance(distance);



                            JSONArray steps = leg.getJSONArray("steps");




                            for (int h = 0; h < steps.length(); h++) {


                                JSONObject step = steps.getJSONObject(h);
                                String instruc = step.getString("html_instructions");
                                String reformattedInstruc = Html.fromHtml(instruc).toString();

                                JSONObject lines = step.getJSONObject("polyline");
                                routelines.add(lines.getString("points"));


                                inst.add(reformattedInstruc);



                                routeNum.setInstructions(inst);


                                routeNum.setList(routelines);

                            }



                            System.out.println(routelines);
                            routeList.add(routeNum);



                            adapter = new DirectionsAdapter(this, R.layout.directions_list_view, routeList, TravellingMethod);
                            ListView myList = (ListView) findViewById(R.id.listViewDir);
                            myList.setAdapter(adapter);


                        }
                    } else {
                        Toast.makeText(this, "Enter valid address", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

        }
    }

    public boolean isNetworkOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()== NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }


    private List<LatLng> decodePoly(String encoded) {

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
        }

        return poly;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

    }





}