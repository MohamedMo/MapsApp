package com.example.mohamed.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.Locale;

/**
 * Created by Mohamed on 24/01/2016.
 */
public class Directions extends MainActivity implements View.OnClickListener, TextToSpeech.OnInitListener, OnMapReadyCallback {


    private TextView resultText;
    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    private int position = 0;

    private static String StartingLocation;
    private static String EndingLocation;
    private static String TravellingMethod;
    private static String URL;
    String method = "walking";
    private static ArrayList<String> Directions = new ArrayList<String>();
    private static ArrayList<String> DirectionssPolylines = new ArrayList<String>();
    private static final String urlLink ="https://maps.googleapis.com/maps/api/directions/xml?origin=Pelterstreet&destination=Pelterstreet&mode=walking&key=AIzaSyC2MMB-7iqMzrccgD9voZHiGf2nY093Jlg";
    private TextView textViewList;
    private static final String TAG = "Demo";
    public static final String GoogleAPIKey = "AIzaSyC2MMB-7iqMzrccgD9voZHiGf2nY093Jlg";
    List<LatLng> list;
    Polyline line; //added
    LatLng latLng;
    LatLng test;
    GoogleMap mMap;
    Marker now;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private static ArrayList<Circle> circles = new ArrayList<Circle>();
   private ArrayList<Routes>routeList;
    private DirectionsAdapter adapter;
    private ListView myList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.directions);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.directions, null, false);
        mDrawer.addView(contentView, 0);
        routeList = new ArrayList<Routes>();
        //get a reference to the button element listed in the XML layout
        //Button speakButton = (Button)findViewById(R.id.btnReadText);
        //listen for clicks
       // speakButton.setO
       // nClickListener(this);

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

       // final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment2);
      //  mapFragment.getMapAsync(this);

       // mMap.getUiSettings().setMyLocationButtonEnabled(true);





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
                // String item = (String) finalMyList.getItemAtPosition(position);
                //  String localities = localityItems.get(position);


                Intent anotherActivityIntent = new Intent(Directions.this, MapHolder.class);
                // anotherActivityIntent.putExtra("name", item);
              //  Bundle bundle=new Bundle();
              //  bundle.putSerializable("object", (Serializable) lines);
              //  anotherActivityIntent.putExtra("bundle", bundle);
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

                if(now!=null){
                    now.remove();
                }

                //This goes up to 21
               // latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //  mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
              //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            //  now =  mMap.addMarker(new MarkerOptions().position(test).title("You are here"));
                now =  mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));



                if(circles.isEmpty()){

                }
                else {
                    float[] distance = new float[2];
                    for (int i = 0; i < circles.size(); i++) {
                        Location.distanceBetween(now.getPosition().latitude, now.getPosition().longitude,
                                circles.get(i).getCenter().latitude, circles.get(i).getCenter().longitude, distance);

                        if (distance[0] > circles.get(i).getRadius()) {
                            // Toast.makeText(getBaseContext(), "Outside", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getBaseContext(), "Inside", Toast.LENGTH_LONG).show();

                            String result = Html.fromHtml(Directions.get(i)).toString();
                            speakWords(result);
                        }
                    }
                }







                //   redrawLine(); //added

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


    public void onActivityResult(int request_code, int result_code, Intent i){


        if (request_code == MY_DATA_CHECK_CODE) {
            if (result_code == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }

        super.onActivityResult(request_code, result_code, i);

        switch (request_code){

            case 100: if (result_code == RESULT_OK && i != null){
                ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultText.setText(result.get(0));
            }
                break;
        }

    }


    public void onDirectionsClick(View v)  {



        mMap.clear();
        //System.out.println("hello");

        EditText start = (EditText)findViewById(R.id.startLocation);
        EditText end = (EditText)findViewById(R.id.endLocation);
      // TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);
        String startingPos = start.getText().toString();
        String finishingPos = end.getText().toString();

        String finalDirections = "<html>";
//        for(String s : Directions)
//            finalDirections = finalDirections + s + "<p>";
//        finalDirections = finalDirections + "</html>";
//        String result = Html.fromHtml(finalDirections).toString();
//        resultDirections.setText(result);

        //Directions.get(1);
        String result = Html.fromHtml(Directions.get(1)).toString();
      //  resultDirections.setText(result);


        }

    public void btnPreviousClick (View v){

//        test = new LatLng
//                (51.529030, -0.07469);
     //   TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);
        if(position > 0){
            position--;
            String result = Html.fromHtml(Directions.get(position)).toString();
           // resultDirections.setText(result);
            // show the data here
        }
    }

    public void btnNextClick(View v) {

//        test = new LatLng
//                (51.5281664, -0.0745294);

        //TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);


        if (position < Directions.size() - 1){
            position++;
            String result = Html.fromHtml(Directions.get(position)).toString();
          //  resultDirections.setText(result);
            // show the data here
        }

    }


    public void onBtnStart (View v){


        String currentLocation = Double.toString(latitude) + "," + Double.toString(longitude);
        EditText end = (EditText)findViewById(R.id.endLocation);
        String finishingPos = end.getText().toString();



    }

    public void onBtnCycle(View v){


        Directions.clear();
        DirectionssPolylines.clear();
        mMap.clear();


        method = "BICYCLING";
        EditText start = (EditText)findViewById(R.id.startLocation);
        EditText end = (EditText)findViewById(R.id.endLocation);
        // TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);
        String startingPos = start.getText().toString();
        String finishingPos = end.getText().toString();

    }

    public void onBtnDrive(View v){


        Directions.clear();
        DirectionssPolylines.clear();
        mMap.clear();


        method = "DRIVING";
        EditText start = (EditText)findViewById(R.id.startLocation);
        EditText end = (EditText)findViewById(R.id.endLocation);
        // TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);
        String startingPos = start.getText().toString();
        String finishingPos = end.getText().toString();

    }

    public void onWalkBtn(View v){


        Directions.clear();
        DirectionssPolylines.clear();
   //     mMap.clear();


        Button btn = (Button) findViewById(R.id.walk);


        method = "WALKING";
        EditText start = (EditText)findViewById(R.id.startLocation);
        EditText end = (EditText)findViewById(R.id.endLocation);
        // TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);
        String startingPos = start.getText().toString();
        String finishingPos = end.getText().toString();
        getNearby(startingPos, finishingPos, method);

    }

    @Override
    public void onClick(View v) {
        //get the text entered
     //   TextView enteredText = (TextView)findViewById(R.id.textViewDirectionsList);
     //   String words = enteredText.getText().toString();
     //   speakWords(words);
    }

    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }




    public void getNearby(String start, String end, String method) {

        Routes routeNum;

        JSONObject leg = null;
        ArrayList<LatLng> point1 = null;
        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        List<LatLng> points = new ArrayList<LatLng>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        PolylineOptions polyLineOptions = null;
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

        if (StartingLocation.equals("") || EndingLocation.equals("")  ){
            Toast.makeText(this, "Please enter locations", Toast.LENGTH_LONG).show();


        }
        else {


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


//        JSONArray jsonArray = new JSONArray(result);
//        for(int i =0;i<jsonArray.length();i++){
//            JSONObject jo = jsonArray.getJSONObject(i);
//            listItems.add(jo.getString("stops"));
//        }
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

                    //  System.out.println(duration);
                    //    System.out.println(distance);
// Take all legs from the route
                    JSONArray steps = leg.getJSONArray("steps");


                    //  System.out.println("steps size= " + steps.length());

                    for (int h = 0; h < steps.length(); h++) {


                        JSONObject step = steps.getJSONObject(h);
                        String instruc = step.getString("html_instructions");
                        String reformattedInstruc = Html.fromHtml(instruc).toString();

                        JSONObject lines = step.getJSONObject("polyline");
                        routelines.add(lines.getString("points"));


                        inst.add(reformattedInstruc);
//                    routelines.add(poly);

//
//                    for (int p = 0; p < routelines.size(); p++) {
//
//                        String polyline;
//                        polyline = routelines.get(p);
//
//                        routepointLines = decodePoly(polyline);
//                      //  System.out.println("Route pass data = " + routepointLines);
//
//
//                    }
//
//
//                    for (int j = 0; j < routepointLines.size(); j++) {
//                    LatLng posisi = new LatLng(routepointLines.get(j).latitude, routepointLines.get(j).longitude);
//                        if (point1 != null) {
//                            point1.add(posisi);
//                        }
//
//                    }
//                    System.out.println(point1);


//ut.println("routepointlines = " + routepointLines.size());


                        routeNum.setInstructions(inst);

                        //  System.out.println(routelines);
                        routeNum.setList(routelines);

                        // System.out.println("json routes = " + routepointLines);
                    }

                    //     System.out.println("Route lines size = " + routelines.size());

                    // System.out.println("Route pass data end = " + routepointLines);


                    //System.out.println("Route polyline size = " + routepointLines.size());
                    //  System.out.println("Route pass data = " + routepointLines);
                    //System.out.println(list);


                    System.out.println(routelines);
                    routeList.add(routeNum);

                    // System.out.println(routeNum);

                    //   System.out.println("routes=");
                    // System.out.println(routeList.get(0).getInstructions());


                    adapter = new DirectionsAdapter(this, R.layout.directions_list_view, routeList);
                    ListView myList = (ListView) findViewById(R.id.listViewDir);
                    myList.setAdapter(adapter);


                }
            }

                else{
                    Toast.makeText(this, "Enter valid address", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
      //  System.out.println(routeList.get(0).getInstructions());

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