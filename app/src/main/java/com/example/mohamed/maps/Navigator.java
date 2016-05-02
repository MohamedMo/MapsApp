package com.example.mohamed.maps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Mohamed on 12/04/2016.
 */
public class Navigator extends MainActivity implements OnMapReadyCallback, TextToSpeech.OnInitListener {


    private TextView resultText;
    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    private int position = 0;
    String Longitude;
    String Latitude;
    private static String StartingLocation;
    private static String EndingLocation;
    private static String TravellingMethod;
    private static String URL;
    String method = "walking";
    private static ArrayList<String> Directions = new ArrayList<String>();
    private static ArrayList<String> DirectionssPolylines = new ArrayList<String>();
    private static final String TAG = "Demo";
    public static final String GoogleAPIKey = "AIzaSyC2MMB-7iqMzrccgD9voZHiGf2nY093Jlg";
    List<LatLng> list;
    Polyline line; //added
    LatLng latLng;
    GoogleMap mMap;
    Marker now;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private static ArrayList<Circle> circles = new ArrayList<Circle>();
    private ArrayList<Routes> routeList;
    private float bear;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.directions);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.navigator_layout, null, false);
        mDrawer.addView(contentView, 0);


        if (!isNetworkOnline()) {
            Toast.makeText(this, "No internet connection ", Toast.LENGTH_LONG).show();
        }


        routeList = new ArrayList<Routes>();


        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.navigatorMap);
        mapFragment.getMapAsync(this);


        LocationManager manager = (LocationManager) this.getSystemService((Context.LOCATION_SERVICE));
        
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                bear = location.getBearing();

                //   latitude = location.getLatitude();
                //  longitude = location.getLongitude();

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                //      String Longitude = "-0.066720";
                //       String Latitude = "51.526974";


                latLng = new LatLng
                        (latitude, longitude);

                if (now != null) {
                    now.remove();
                }


                now = mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));


                if (circles.isEmpty()) {

                } else {
                    float[] distance = new float[2];
                    for (int i = 0; i < circles.size(); i++) {
                        Location.distanceBetween(now.getPosition().latitude, now.getPosition().longitude,
                                circles.get(i).getCenter().latitude, circles.get(i).getCenter().longitude, distance);

                        if (distance[0] > circles.get(i).getRadius()) {
                            // Toast.makeText(getBaseContext(), "Outside", Toast.LENGTH_LONG).show();

                        } else {
                            //   Toast.makeText(getBaseContext(), "Inside", Toast.LENGTH_LONG).show();

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


    public void onActivityResult(int request_code, int result_code, Intent i) {


        if (request_code == MY_DATA_CHECK_CODE) {
            if (result_code == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            } else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }

        super.onActivityResult(request_code, result_code, i);

        switch (request_code) {

            case 100:
                if (result_code == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    resultText.setText(result.get(0));
                }
                break;
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


    public void onBtnCycle(View v) {



            circles.clear();
            Directions.clear();
            DirectionssPolylines.clear();
            mMap.clear();


            Button btn = (Button) findViewById(R.id.walk);
            String doubleLong = Double.toString(longitude);
            String doubleLat = Double.toString(latitude);


            Longitude = "-0.066720";
            Latitude = "51.526974";

            String latlng = doubleLat + "," + doubleLong;
            //String latlng = Latitude + "," + Longitude;
            method = "BICYCLING";

            EditText end = (EditText) findViewById(R.id.endLocation);
            if (end.getText().toString().matches("")) {
                Toast.makeText(this, "You did not enter a destination", Toast.LENGTH_SHORT).show();

            }
            else {
                // TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);

                String finishingPos = end.getText().toString();
                getDirections(latlng, finishingPos, method);
            }

    }


    public boolean isGPSOn(){
        boolean status = false;
        LocationManager manager = (LocationManager) this.getSystemService((Context.LOCATION_SERVICE));


        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            status = true;

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is disabled! Please enable")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            status = false;
            alert.show();
        }
        return status;
    }

    public void onBtnDrive(View v) {


            circles.clear();
            Directions.clear();
            DirectionssPolylines.clear();
            mMap.clear();


            Button btn = (Button) findViewById(R.id.walk);
            String doubleLong = Double.toString(longitude);
            String doubleLat = Double.toString(latitude);


            String latlng = doubleLat + "," + doubleLong;
            //String latlng = Latitude + "," + Longitude;
            method = "DRIVING";

            EditText end = (EditText) findViewById(R.id.endLocation);
            if (end.getText().toString().matches("")) {
                Toast.makeText(this, "You did not enter a destination", Toast.LENGTH_SHORT).show();

            }
            else {
                // TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);

                String finishingPos = end.getText().toString();
                getDirections(latlng, finishingPos, method);
            }

    }

    public void onWalkBtn(View v) {


            circles.clear();
            Directions.clear();
            DirectionssPolylines.clear();
            mMap.clear();


            Button btn = (Button) findViewById(R.id.walk);
            String doubleLong = Double.toString(longitude);
            String doubleLat = Double.toString(latitude);


            Longitude = "-0.066720";
            Latitude = "51.526974";

            String latlng = doubleLat + "," + doubleLong;
            //String latlng = Latitude + "," + Longitude;
            method = "WALKING";

            EditText end = (EditText) findViewById(R.id.endLocation);
            if (end.getText().toString().matches("")) {
                Toast.makeText(this, "You did not enter a destination", Toast.LENGTH_SHORT).show();

            }
            else {
                // TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);

                String finishingPos = end.getText().toString();
                getDirections(latlng, finishingPos, method);
            }

    }


    public void onClick(View v) {

    }

    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    public void getDirections(String start, String end, String method) {


        if (!isNetworkOnline()) {
            Toast.makeText(this, "No internet connection ", Toast.LENGTH_LONG).show();

        }
        else {
            if (!isGPSOn()) {
                Toast.makeText(this, "Please turn on GPS ", Toast.LENGTH_LONG).show();
            } else {

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

                URL = String
                        .format("https://maps.googleapis.com/maps/api/directions/xml?origin=%s&destination=%s&mode=%s&key=%s",
                                StartingLocation, EndingLocation, TravellingMethod,
                                GoogleAPIKey);

                System.out.println(URL);
                File dir = new File(this.getFilesDir() + "/Users/Mohamed/AndroidStudioProjects");
                dir.mkdirs(); //create folders where write files
                final File xmlFile = new File(dir, "DirectionsList.xml");

                //  File xmlFile = new File("DirectionsList.xml");

                try {
                    java.net.URL url = new URL(URL);
                    URLConnection con = url.openConnection();
                    InputStream is = con.getInputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    PrintWriter out = new PrintWriter(xmlFile);

                    String line = null;

                    while ((line = br.readLine()) != null) {
                        out.println(line);
                    }

                    out.close();
                    br.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "error url", e);
                }

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(xmlFile);
                    doc.getDocumentElement().normalize();
                    System.out.println("Root element :"
                            + doc.getDocumentElement().getNodeName());


                    String usr = doc.getElementsByTagName("status").item(0).getTextContent();


                    if (usr.equalsIgnoreCase("ok")) {


                        NodeList nodeList = doc.getElementsByTagName("route");
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            Node node = nodeList.item(i);


                        }


                        NodeList nList = doc.getElementsByTagName("step");

                        //  System.out.println(nList.getLength());

                        for (int temp = 0; temp < nList.getLength(); temp++) {

                            Node nNode = nList.item(temp);

                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                                Element eElement = (Element) nNode;

                                Directions.add(eElement
                                        .getElementsByTagName("html_instructions").item(0)
                                        .getTextContent());
                                DirectionssPolylines.add(eElement
                                        .getElementsByTagName("points").item(0)
                                        .getTextContent());

                            }

                            for (int i = 0; i < DirectionssPolylines.size(); i++) {

                                String polyline = "";
                                polyline = DirectionssPolylines.get(i);

                                list = decodePoly(polyline);
                            }


                            System.out.println("list size = " + list.size());


                            for (int j = 0; j < list.size(); j++) {
                                LatLng posisi = new LatLng(list.get(j).latitude, list.get(j).longitude);

                                options.add(posisi);


                            }


                            line = mMap.addPolyline(options);

                            double lo = Double.parseDouble(Longitude);
                            double la = Double.parseDouble(Latitude);

                            LatLng startcam = new LatLng(list.get(0).latitude, list.get(0).longitude);
                            LatLng begin = new LatLng(latitude, longitude);


                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(begin) // Sets the center of the map to
                                    .zoom(20)                   // Sets the zoom
                                    .bearing(0) // Sets the orientation of the camera to east
                                    .tilt(90)    // Sets the tilt of the camera
                                    .build();    // Creates a CameraPosition from the builder
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                    cameraPosition));


                            circles.add(mMap.addCircle(new CircleOptions()
                                    .center(startcam)
                                    .radius(30)
                                    .strokeColor(Color.RED)
                                    .fillColor(Color.TRANSPARENT)));


                        }
                    } else {
                        Toast.makeText(this, "Enter valid address", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
            if (Directions.isEmpty()) {
                Directions.add("No data returned");
            }
        }

      //  System.out.println("Directions" + Directions.size());
       // System.out.println("List" + list.size());
       // System.out.println("circles" + circles.size());
        // googleMap.addPolyline(polyLineOptions);

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
