package com.example.mohamed.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TabHost;
import android.widget.TextView;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Mohamed on 22/02/2016.
 */
public class PlaceOfInterest extends MainActivity {

    private static final String urlLink ="https://maps.googleapis.com/maps/api/directions/xml?origin=Pelterstreet&destination=Pelterstreet&mode=walking&key=AIzaSyC2MMB-7iqMzrccgD9voZHiGf2nY093Jlg";

    public static final String GoogleAPIKey = "AIzaSyArLu8S4h8epvmM_K8aWPRY_TS9XST_7yw";
    private static String URL;
    private static final String TAG = "Demo";
    private static ArrayList<String> POF = new ArrayList<String>();
    private static ArrayList<String> Photos = new ArrayList<String>();
    private static ArrayList<String> POFplace = new ArrayList<String>();
    private static ArrayList<String> photosLink = new ArrayList<String>();
    private static ArrayAdapter<String> adapter;
   private File xmlFile;
    private double latitude = 0.0;
    private double longitude = 0.0;
    TabHost tabHost;
    private String urlphoto;
    private ListView listy;


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.place_of_interest, null, false);
        mDrawer.addView(contentView, 0);
        File dir = new File(this.getFilesDir() + "/Users/Mohamed/AndroidStudioProjects");
        dir.mkdirs(); //create folders where write files
      xmlFile = new File(dir, "NearMe.xml");


        final TabHost host = (TabHost)findViewById(R.id.tabHost);


        host.setup();


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Shopping");
        spec.setContent(R.id.listViewShop);
        spec.setIndicator("Shopping");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Bar");
        spec.setContent(R.id.listViewBar);
        spec.setIndicator("Bar");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Gym");
        spec.setContent(R.id.listViewGym);
        spec.setIndicator("Gym");
        host.addTab(spec);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {


                if (host.getCurrentTab() == 0){
                    getNearby("shopping_mall");

                }
                if (host.getCurrentTab() == 1){
                    getNearby("bar");
                }
                if (host.getCurrentTab() == 2){
                    getNearby("gym");
                }
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



                TextView longi = (TextView)findViewById(R.id.textViewLong);
                TextView lati = (TextView)findViewById(R.id.textViewLat);
                String doubleLong = Double.toString(longitude);
                String doubleLat = Double.toString(latitude);
                longi.setText(doubleLong);
                lati.setText(doubleLat);





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






      listy=(ListView) findViewById(R.id.listViewBar);
        listy.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String item = (String) listy.getItemAtPosition(position);
               // String urls = photosLink.get(position);


                Intent anotherActivityIntent = new Intent(PlaceOfInterest.this, listViewHolder.class);
                anotherActivityIntent.putExtra("name", item);
              //  anotherActivityIntent.putExtra("urls", urls);
                startActivity(anotherActivityIntent);


                System.out.println(item);
            }
        });
    }




    private String getCurrentLocation() {
        String Longitude = "-0.066720";
       String Latitude = "51.526974";
//        String Longitude = Double.toString(longitude);
//        String Latitude = Double.toString(latitude);

        TextView longi = (TextView)findViewById(R.id.textViewLong);
        TextView lati = (TextView)findViewById(R.id.textViewLat);

      //  String Longitude = longi.getText().toString();
       // String Latitude = lati.getText().toString();

        return String.format("location=%s,%s&radius=500&", Latitude, Longitude);
    }



    public void onBtnShop(View v)  {

   getNearby("shopping_mall");
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, POF);
        ListView myList=(ListView) findViewById(R.id.listViewShop);
        myList.setAdapter(adapter);

    }

    public void onBtnBar(View v)  {

        getNearby("bar");
        adapter=new
                ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                POF);
        ListView myList=(ListView)
                findViewById(R.id.listViewBar);
        myList.setAdapter(adapter);

    }

    public void onBtnGym(View v)  {

        getNearby("gym");


    }


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


        if (type.equalsIgnoreCase("shopping_mall")){

            displayResults();


//            for(int i = 0; i<Photos.size();i++){
//                getPhotos(Photos.get(i));
//            }


            adapter = new ListAdapter(this, R.layout.custom_listview, POF);
            ListView myList=(ListView) findViewById(R.id.listViewShop);
            myList.setAdapter(adapter);
       //     myList.setClickable(true);


//            adapter = new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, POF);
//            ListView myList=(ListView) findViewById(R.id.listViewShop);
//            myList.setAdapter(adapter);
        }


        if (type.equalsIgnoreCase("bar")){

            displayResults();

//            for(int i = 0; i<Photos.size();i++){
//                getPhotos(Photos.get(i));
//            }
            adapter = new ListAdapter(this, R.layout.custom_listview, POF );
            ListView myList=(ListView) findViewById(R.id.listViewBar);
            myList.setAdapter(adapter);
       //     myList.setClickable(true);

//            adapter=new
//                    ArrayAdapter<String>(
//                    this,
//                    android.R.layout.simple_list_item_1,
//                    POF);
//            ListView myList=(ListView)
//                    findViewById(R.id.listViewBar);
//            myList.setAdapter(adapter);
        }


        if (type.equalsIgnoreCase("gym")){

            displayResults();

//            for(int i = 0; i<Photos.size();i++){
//                getPhotos(Photos.get(i));
//            }
            adapter = new ListAdapter(this, R.layout.custom_listview, POF );
            ListView myList=(ListView) findViewById(R.id.listViewGym);

            myList.setAdapter(adapter);
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




}
