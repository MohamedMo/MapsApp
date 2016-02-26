package com.example.mohamed.maps;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends MainActivity implements OnMapReadyCallback, TextToSpeech.OnInitListener {

    private GoogleMap mMap;
    private int longi;
    private int lati;
    private TextView resultText;
    private String command;
    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
   private EditText location_tf;
    private String location;
    private Button searchButton;
    private View v;
    private ArrayList<LatLng> points; //added
    Polyline line; //added
    LatLng latLng;
    float zoomLevel = (float) 16.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_maps, null, false);
        mDrawer.addView(contentView, 0);
        resultText = (TextView)findViewById(R.id.textView3);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        points = new ArrayList<LatLng>(); //added




        LocationManager manager = (LocationManager) this.getSystemService((Context.LOCATION_SERVICE));
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                 //This goes up to 21
                 latLng = new LatLng(location.getLatitude(), location.getLongitude());
              //  mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                points.add(latLng); //added

                redrawLine(); //added

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    public void onSearch(View v){



       location_tf = (EditText)findViewById(R.id.textAddress);
         location = location_tf.getText().toString();
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

           // Intent i = new Intent(MapsActivity.this, Voice.class);
           // startActivity(i);
            promptSpeechInput();


        }
    }

    public void onBtnDirectionsClick (View v){

        if (v.getId() == R.id.btnToDirections){

            Intent i = new Intent(MapsActivity.this, Directions.class);
            startActivity(i);
        }
    }

    public void onPOFClick (View v){

        if (v.getId() == R.id.btnPOF){

            Intent i = new Intent(MapsActivity.this, PlaceOfInterest.class);
            startActivity(i);
        }
    }


    public void onBtnGPS (View v){

        if (v.getId() == R.id.btnGPS){

            Intent i = new Intent(MapsActivity.this, GPS_Location.class);
            startActivity(i);
        }
    }


    public void promptSpeechInput (){

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");


        try {
            startActivityForResult(i, 100);
        }
        catch (ActivityNotFoundException a)
        {
            Toast.makeText(MapsActivity.this, "Sorry", Toast.LENGTH_LONG).show();
        }
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
                // resultText.setText(result.get(0));
                command = result.get(0);
                resultText.setText(result.get(0));
                 respond(v);
            }
                break;
        }

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

    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void respond(View v){

        if(command.toString().equals("hello")){
            speakWords("hi");
        }
        if(command.toString().equals("where am I")){
            speakWords("how would I know");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        }

        if(command.toString().equals("go")){

            speakWords("go where");
        }

        if(command.toString().contains("go"))
        {
            speakWords("Yes sir");
            String seperate =  "\\s*\\bgo\\b\\s*";
            command = command.replaceAll(seperate, "");

         //   String parts[] = command.split(" ");
          //  String loc = parts[1];

            location_tf = (EditText)findViewById(R.id.textAddress);
            location_tf.setText(command);
            onSearch(v);

        }

        if(command.toString().equals("zoom in")) {
            speakWords("ok");
            mMap.moveCamera(CameraUpdateFactory.zoomIn());
        }
        if(command.toString().equals("zoom out")){

            speakWords("say please next time");
            mMap.moveCamera(CameraUpdateFactory.zoomOut());
        }

        else{
          //  speakWords("please repeat that");
        }
    }

    private void redrawLine(){

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
       // addMarker(); //add Marker in current position
        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));

        line = mMap.addPolyline(options); //add Polyline
    }




}
