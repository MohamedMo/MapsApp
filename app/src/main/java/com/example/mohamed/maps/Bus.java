package com.example.mohamed.maps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
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
    static JSONObject jObj = null;
    private String newURL;
    static String json = "";
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.bus_transport, null, false);
        mDrawer.addView(contentView, 0);



        final TabHost host = (TabHost)findViewById(R.id.tabHost);


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

        myList = (ListView)
                findViewById(R.id.listViewNear);
        myList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String item = (String) myList.getItemAtPosition(position);
                String localities = localityItems.get(position);


                Intent anotherActivityIntent = new Intent(Bus.this, listViewHolder.class);
                anotherActivityIntent.putExtra("name", item);
                anotherActivityIntent.putExtra("local", localities);
                startActivity(anotherActivityIntent);


                System.out.println(item);
            }
        });

        getNearby();

//getJourney();
    }


    public void getNearby() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        URL = String
                .format("http://transportapi.com/v3/uk/bus/stops/near.json?lat=51.527789&lon=-0.102323&page=3&rpp=10&api_key=%s&app_id=%s",
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
//        JSONArray jsonArray = new JSONArray(result);
//        for(int i =0;i<jsonArray.length();i++){
//            JSONObject jo = jsonArray.getJSONObject(i);
//            listItems.add(jo.getString("stops"));
//        }


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


public void onBtnSearchBus(View v){

    EditText start = (EditText)findViewById(R.id.startBus);
    EditText end = (EditText)findViewById(R.id.endBus);
    //TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);
    String startingPos = start.getText().toString();
    String finishingPos = end.getText().toString();
    getJourney(startingPos, finishingPos);
}


    public void getJourney(String startbus, String endbus) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        StartingLocation = startbus;
        EndingLocation = endbus;

        newURL = String.format("http://transportapi.com/v3/uk/public/journey/from/%s/to/%s.json?app_id=03bf8009&app_key=d9307fd91b0247c607e098d5effedc97&modes=bus&region=tfl",
                StartingLocation , EndingLocation);

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
//        JSONArray jsonArray = new JSONArray(result);
//        for(int i =0;i<jsonArray.length();i++){
//            JSONObject jo = jsonArray.getJSONObject(i);
//            listItems.add(jo.getString("stops"));
//        }


            JSONObject mainObj = new JSONObject(result);
            if(mainObj != null){
                JSONArray list = mainObj.getJSONArray("routes");
                if(list != null){

                        JSONObject elem = list.getJSONObject(0);
                        if(elem != null){
                            JSONArray prods = elem.getJSONArray("route_parts");
                            if(prods != null){
                                for(int j = 0; j < prods.length();j++){
                                    JSONObject innerElem = prods.getJSONObject(j);
                                    if(innerElem != null){
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


            adapter = new BusListAdapter(this, R.layout.custom_bus_layout, journey , endJourney ,durationArray,busStops );
            ListView myList=(ListView) findViewById(R.id.listViewBusJ);
            myList.setAdapter(adapter);
//            JSONObject jsonResponse = new JSONObject(result);
//            //JSONObject jsonMainNode = jsonResponse.getJSONObject("routes");
//
//
//            JSONArray  routes = jsonResponse.optJSONArray("routes");
//            JSONArray  js = jsonMainNode.optJSONArray("route_parts");
//
//            for (int i = 0; i < js.length(); i++) {
//                JSONObject jsonChildNode = js.getJSONObject(i);
//                String start = jsonChildNode.optString("from_point_name");
//                String end = jsonChildNode.optString("to_point_name");
//                String duration = jsonChildNode.optString("duration");
//
//                String startloc = start;
//                String endloct = end;
//                String durloc = duration;
//
//
//
//                System.out.println(startloc);
//                System.out.println(endloct);
//                System.out.println(durloc);
//
//              //  listItems.add(outPut);
//              //  localityItems.add(end);
//
//
//
//
//            }



        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Bus.this, "Enter Valid Bus Station", Toast.LENGTH_SHORT).show();
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
