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
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Mohamed on 22/02/2016.
 */
public class PlaceOfInterest extends MainActivity implements OnMapReadyCallback, OnInfoWindowClickListener{

    private static final String urlLink ="https://maps.googleapis.com/maps/api/directions/xml?origin=Pelterstreet&destination=Pelterstreet&mode=walking&key=AIzaSyC2MMB-7iqMzrccgD9voZHiGf2nY093Jlg";

    public static final String GoogleAPIKey = "AIzaSyArLu8S4h8epvmM_K8aWPRY_TS9XST_7yw";
    private static String URL;
    private static final String TAG = "Demo";
    private static ArrayList<String> POF = new ArrayList<String>();
    private static ArrayList<String> Photos = new ArrayList<String>();
    private static ArrayList<PlacesList> arrayOfPlaces;
    private static ArrayList<String> photosLink = new ArrayList<String>();
    private static ArrayAdapter<String> adapter;
   private File xmlFile;
    private double latitude = 0.0;
    private double longitude = 0.0;
    TabHost tabHost;
    private String urlphoto;
    private ListView listy;
    private Spinner spinner;
    private PlacesList places;
    private GoogleMap mMap;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();

    HashMap <String, Integer> mMarkers = new HashMap<String, Integer>();


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.place_of_interest, null, false);
        mDrawer.addView(contentView, 0);
        File dir = new File(this.getFilesDir() + "/Users/Mohamed/AndroidStudioProjects");
        dir.mkdirs(); //create folders where write files
        xmlFile = new File(dir, "NearMe.xml");


         final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapPOF);
          mapFragment.getMapAsync(this);
        arrayOfPlaces = new ArrayList<PlacesList>();
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                String Text = parent.getSelectedItem().toString();
                if (Text.equals("Shopping")) {
                    mMap.clear();
                    arrayOfPlaces.clear();
                  getNear("shopping_mall");
                    addmarkers(120);


                } else if (Text.equals("Bar")) {
                    mMap.clear();
                    arrayOfPlaces.clear();
                    getNear("bar");
                    addmarkers(30);

                } else if (Text.equals("Gym")) {
                    mMap.clear();
                    arrayOfPlaces.clear();
                    getNear("gym");
                    addmarkers(240);

                } else if (Text.equals("Restaurant")) {
                    mMap.clear();
                    arrayOfPlaces.clear();
                    getNear("restaurant");
                    addmarkers(30);

                } else if (Text.equals("Hospital")) {
                    mMap.clear();
                    arrayOfPlaces.clear();
                    getNear("hospital");
                    addmarkers(30);

                } else if (Text.equals("Bank")) {
                    mMap.clear();
                    arrayOfPlaces.clear();
                    getNear("bank");
                    addmarkers(30);


                } else if (Text.equals("Library")) {
                    mMap.clear();
                    arrayOfPlaces.clear();
                    getNear("library");
                    addmarkers(30);


                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
//
//        final TabHost host = (TabHost)findViewById(R.id.tabHost);
//
//
//        host.setup();
//
//
//        //Tab 1
//        TabHost.TabSpec spec = host.newTabSpec("Shopping");
//        spec.setContent(R.id.listViewShop);
//        spec.setIndicator("Shopping");
//        host.addTab(spec);
//
//        //Tab 2
//        spec = host.newTabSpec("Bar");
//        spec.setContent(R.id.listViewBar);
//        spec.setIndicator("Bar");
//        host.addTab(spec);
//
//        //Tab 3
//        spec = host.newTabSpec("Gym");
//        spec.setContent(R.id.listViewGym);
//        spec.setIndicator("Gym");
//        host.addTab(spec);
//
//        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String arg0) {
//
//
//                if (host.getCurrentTab() == 0){
//                    getNearby("shopping_mall");
//
//                }
//                if (host.getCurrentTab() == 1){
//                    getNearby("bar");
//                }
//                if (host.getCurrentTab() == 2){
//                    getNearby("gym");
//                }
//            }
//        });

        LocationManager manager = (LocationManager) this.getSystemService((Context.LOCATION_SERVICE));


        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is disabled!, please enable")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }





        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //This goes up to 21
                //   latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //  mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                latitude = location.getLatitude();
                longitude = location.getLongitude();


//                TextView longi = (TextView) findViewById(R.id.textViewLong);
//                TextView lati = (TextView) findViewById(R.id.textViewLat);
//                String doubleLong = Double.toString(longitude);
//                String doubleLat = Double.toString(latitude);
//                longi.setText(doubleLong);
//                lati.setText(doubleLat);


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



//      listy=(ListView) findViewById(R.id.listViewBar);
//        listy.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                String item = (String) listy.getItemAtPosition(position);
//               // String urls = photosLink.get(position);
//
//
//                Intent anotherActivityIntent = new Intent(PlaceOfInterest.this, listViewHolder.class);
//                anotherActivityIntent.putExtra("name", item);
//              //  anotherActivityIntent.putExtra("urls", urls);
//                startActivity(anotherActivityIntent);
//
//
//                System.out.println(item);
//            }
//        });
//    }
//



    private String getCurrentLocation() {
       //String Longitude = "-0.066720";
     // String Latitude = "51.526974";
        String Longitude = Double.toString(longitude);
        String Latitude = Double.toString(latitude);

        TextView longi = (TextView)findViewById(R.id.textViewLong);
        TextView lati = (TextView)findViewById(R.id.textViewLat);



        return String.format("location=%s,%s&radius=500&", Latitude, Longitude);
    }



//    public void onBtnShop(View v)  {
//
//   getNearby("shopping_mall");
//        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, POF);
//        ListView myList=(ListView) findViewById(R.id.listViewShop);
//        myList.setAdapter(adapter);
//
//    }
//
//    public void onBtnBar(View v)  {
//
//        getNearby("bar");
//        adapter=new
//                ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                POF);
//        ListView myList=(ListView)
//                findViewById(R.id.listViewBar);
//        myList.setAdapter(adapter);
//
//    }
//
//    public void onBtnGym(View v)  {
//
//        getNearby("gym");
//
//
//    }


    public void getPhotos(String ref){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        POF = new ArrayList<String>();
        urlphoto = String
                .format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                       ref, GoogleAPIKey);


        photosLink.add(urlphoto);

    //    System.out.println(urlphoto);
//        try {
//            ImageView i = (ImageView)findViewById(R.id.image1);
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(urlphoto).getContent());
//            i.setImageBitmap(bitmap);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    public void getNearby(String type) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        POF = new ArrayList<String>();
        URL = String
                .format("https://maps.googleapis.com/maps/api/place/nearbysearch/xml?%stype=%s&key=%s",
                        getCurrentLocation(), type, GoogleAPIKey);

        System.out.println(URL);


        //  File xmlFile = new File("DirectionsList.xml");

        try {
            URL url = new URL(URL);
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

//
//        if (type.equalsIgnoreCase("shopping_mall")){
//
    displayResults();


//            for(int i = 0; i<Photos.size();i++){
//                getPhotos(Photos.get(i));
//            }


//            adapter = new ListAdapter(this, R.layout.custom_listview, POF);
//            ListView myList=(ListView) findViewById(R.id.listViewShop);
//            myList.setAdapter(adapter);
       //     myList.setClickable(true);


//            adapter = new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, POF);
//            ListView myList=(ListView) findViewById(R.id.listViewShop);
//            myList.setAdapter(adapter);
//        }
//
//
//        if (type.equalsIgnoreCase("bar")){
//
//            displayResults();

//            for(int i = 0; i<Photos.size();i++){
//                getPhotos(Photos.get(i));
//            }
//            adapter = new ListAdapter(this, R.layout.custom_listview, POF );
//            ListView myList=(ListView) findViewById(R.id.listViewBar);
//            myList.setAdapter(adapter);
       //     myList.setClickable(true);

//            adapter=new
//                    ArrayAdapter<String>(
//                    this,
//                    android.R.layout.simple_list_item_1,
//                    POF);
//            ListView myList=(ListView)
//                    findViewById(R.id.listViewBar);
//            myList.setAdapter(adapter);
//        }


//        if (type.equalsIgnoreCase("gym")){
//
//            displayResults();

//            for(int i = 0; i<Photos.size();i++){
//                getPhotos(Photos.get(i));
//            }
//            adapter = new ListAdapter(this, R.layout.custom_listview, POF );
//            ListView myList=(ListView) findViewById(R.id.listViewGym);
//
//            myList.setAdapter(adapter);
         //   myList.setClickable(true);
//            adapter=new
//                    ArrayAdapter<String>(
//                    this,
//                    android.R.layout.simple_list_item_1,
//                    POF);
//            ListView myList=(ListView)
//                    findViewById(R.id.listViewGym);
//            myList.setAdapter(adapter);
        }





    private void displayResults() {
//
     //   File dir = new File(this.getFilesDir() + "/Users/Mohamed/AndroidStudioProjects");
     //   dir.mkdirs(); //create folders where write files
     //   final File xmlFile = new File(dir, "PlacesOfInterest.xml");

       // StringBuilder builder = new StringBuilder();

        //builder.append("<html>");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("result");

            System.out.println(nList.getLength());

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    POF.add(eElement.getElementsByTagName("name")
                                    .item(0).getTextContent() + "  " + eElement.getElementsByTagName("vicinity").item(0).getTextContent()
                    );





                 //   Photos.add(eElement.getElementsByTagName("photo_reference").item(0).getTextContent());


                 //  urlphoto = eElement.getElementsByTagName("photo_reference").item(0).getTextContent();
                  //  Photos.add(urlphoto);

                 //   System.out.println(urlphoto);
                 //   getPhotos(Photos.get(1));
//                    POF.add(eElement.getElementsByTagName("vicinity")
//                                    .item(0).getTextContent()
//                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

     //   builder.append("</html>");


      //  TextView resultPOF = (TextView)findViewById(R.id.placeOfInterestView);
        //resultPOF.setText(builder.toString());

        //resultDirections.setText(result);
       // resultPOF.setText(result);





    }


    public void getNear(String type) {


        Routes routeNum;

        JSONObject leg = null;
        ArrayList<LatLng> point1 = null;
        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        List<LatLng> points = new ArrayList<LatLng>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        PolylineOptions polyLineOptions = null;
        StrictMode.setThreadPolicy(policy);


        URL = String
                .format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?%stype=%s&key=%s",
                        getCurrentLocation(), type, GoogleAPIKey);

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




            JSONObject jsonObject = new JSONObject(result);


            JSONArray placeArray = jsonObject.getJSONArray("results");

            String error = jsonObject.getString("status");



            if (error.equalsIgnoreCase("ok")) {


                for (int i = 0; i < placeArray.length(); i++) {

                    places = new PlacesList();
                    JSONObject results = placeArray.getJSONObject(i);

                    String name = results.getString("name");
                    places.setName(name);
                    System.out.println("result" + results.getString("name"));

                    if (results.has("rating")){


                    Double ratings = results.getDouble("rating");
                    places.setRatings(ratings);
                }
                    else{
                        places.setRatings(0.0);
                    }

                    String vicinity = results.getString("vicinity");
                    places.setVicinity(vicinity);
                    System.out.println("vicinity" + results.getString("vicinity"));


                    JSONObject jsonLocation = results.getJSONObject("geometry").getJSONObject("location");

                    Double lat = jsonLocation.getDouble("lat");
                    places.setLat(lat);
                    Double lng = jsonLocation.getDouble("lng");
                    places.setLng(lng);


                    if (results.has("photos")) {
                        JSONArray jsonImage = results.getJSONArray("photos");
                        JSONObject imageO = jsonImage.getJSONObject(0);
                        String photoref = imageO.getString("photo_reference");

                        urlphoto = String
                                .format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                                        photoref, GoogleAPIKey);
                        places.setImage(urlphoto);
                        System.out.println("photo" + urlphoto);
                    } else {
                        places.setImage("null");
                        System.out.println("null");
                    }


                    arrayOfPlaces.add(places);

                }


            }

            else{

                Toast.makeText(this, "No places found", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //  System.out.println(routeList.get(0).getInstructions());

      //  addmarkers();

    }


    public void addmarkers(float hue){

        for(int i = 0;i<arrayOfPlaces.size();i++){

            LatLng latLng = new LatLng(arrayOfPlaces.get(i).getLat(),arrayOfPlaces.get(i).getLng());
            final String name = arrayOfPlaces.get(i).getName();
            final String vicinity = arrayOfPlaces.get(i).getVicinity();



            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name).snippet(vicinity).icon(BitmapDescriptorFactory.defaultMarker(hue)));
            mMarkers.put(marker.getId(), i);
         //   mHashMap.put(marker, i);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng) // Sets the center of the map to
                    .zoom(14)                   // Sets the zoom
                    .bearing(0) // Sets the orientation of the camera to east
                    .tilt(0)    // Sets the tilt of the camera to 30 degrees
                    .build();    // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    cameraPosition));


            mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {


                    Log.i("pressed", "pressed");
                    int id = mMarkers.get(marker.getId());

                    final String name = arrayOfPlaces.get(id).getName();
                    final String vicinity = arrayOfPlaces.get(id).getVicinity();
                    final String image = arrayOfPlaces.get(id).getImage();
                    final Double ratings = arrayOfPlaces.get(id).getRatings();
                    Intent intent = new Intent(PlaceOfInterest.this, listViewHolder.class);
                    intent.putExtra("name", name);
                    intent.putExtra("vicinity", vicinity);
                    intent.putExtra("image", image);
                    intent.putExtra("ratings", ratings);

                    startActivity(intent);


                }
            });
        }




    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        int id = mMarkers.get(marker.getId());

        final String name = arrayOfPlaces.get(id).getName();
        final String vicinity = arrayOfPlaces.get(id).getVicinity();
        final String image = arrayOfPlaces.get(id).getImage();
        Intent intent = new Intent(PlaceOfInterest.this, listViewHolder.class);
        intent.putExtra("name", name);
        intent.putExtra("vicinity", vicinity);
        intent.putExtra("image", image);


        startActivity(intent);

    }
}
