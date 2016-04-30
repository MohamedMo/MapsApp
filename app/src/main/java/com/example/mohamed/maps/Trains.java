package com.example.mohamed.maps;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by Mohamed on 11/03/2016.
 */
public class Trains extends MainActivity {


    public static final String api_key = "8b06798f5abf775a2973fc9d9970674d";
    public static final String app_key = "91a0f395";
    private static String URL = "http://transportapi.com/v3/uk/bus/stops/near.json?lat=51.527789&lon=-0.102323&page=3&rpp=10&api_key=d9307fd91b0247c607e098d5effedc97&app_id=03bf8009";
    static JSONObject jObj = null;
    static String json = "";
    private String newURL;
    private static String StartingLocation;
    private static String EndingLocation;
    private static ArrayAdapter<String> adapter;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayList<String> localityItems = new ArrayList<String>();
    private ArrayList<String> journey = new ArrayList<String>();
    private ArrayList<String> endJourney = new ArrayList<String>();
    private ArrayList<String> durationArray = new ArrayList<String>();
    private ArrayList<String> busStops = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.train_transport, null, false);
        mDrawer.addView(contentView, 0);

        getNearby();


        final TabHost host = (TabHost)findViewById(R.id.tabHost);


        host.setup();


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Search");
        spec.setContent(R.id.listViewTrainJ);

        spec.setIndicator("Search");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Near Me");
        spec.setContent(R.id.listViewTrain);
        spec.setIndicator("Near Me");
        host.addTab(spec);

        // //  final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        //  mapFragment.getMapAsync(this);

        for(int i=0;i<host.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

    }










    public void getNearby() {
        if(!isNetworkOnline()){
            Toast.makeText(this, "No internet connection ", Toast.LENGTH_LONG).show();
        }else {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            URL = String
                    .format("http://transportapi.com/v3/uk/train/stations/near.json?lat=51.527789&lon=-0.102323&page=3&rpp=10&api_key=8b06798f5abf775a2973fc9d9970674d&app_id=91a0f395",
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
                JSONArray jsonMainNode = jsonResponse.optJSONArray("stations");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String name = jsonChildNode.optString("name");

                    String outPut = name;
                    listItems.add(outPut);


                }


                adapter = new
                        ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        listItems);
                ListView myList = (ListView)
                        findViewById(R.id.listViewTrain);
                myList.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }





    public void getJourney(String startbus, String endbus) {

        if(!isNetworkOnline()){
            Toast.makeText(this, "No internet connection ", Toast.LENGTH_LONG).show();
        }
        else {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            StartingLocation = startbus;
            EndingLocation = endbus;

            if (StartingLocation.equalsIgnoreCase("") || EndingLocation.equals("")) {

                Toast.makeText(Trains.this, "Please Enter Train Stations", Toast.LENGTH_SHORT).show();
            } else {


                newURL = String.format("http://transportapi.com/v3/uk/public/journey/from/%s/to/%s.json?app_id=03bf8009&app_key=d9307fd91b0247c607e098d5effedc97&modes=tube&region=tfl",
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
//        JSONArray jsonArray = new JSONArray(result);
//        for(int i =0;i<jsonArray.length();i++){
//            JSONObject jo = jsonArray.getJSONObject(i);
//            listItems.add(jo.getString("stops"));
//        }


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
                                            String linename = innerElem.optString("line_name");

                                            String startloc = start;
                                            String endloct = end;
                                            String durloc = duration;
                                            String line = linename;

                                            journey.add(startloc);
                                            endJourney.add(endloct);
                                            durationArray.add(durloc);
                                            busStops.add(line);


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
                    ListView myList = (ListView) findViewById(R.id.listViewTrainJ);
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
                    Toast.makeText(Trains.this, "Enter Valid Bus Station", Toast.LENGTH_SHORT).show();
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
    public void onBtnSearchTrain(View v){

        EditText start = (EditText)findViewById(R.id.startTrain);
        EditText end = (EditText)findViewById(R.id.endTrain);
        //TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);
        String startingPos = start.getText().toString();
        String finishingPos = end.getText().toString();
        getJourney(startingPos, finishingPos);
    }
}
