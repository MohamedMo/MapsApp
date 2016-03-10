package com.example.mohamed.maps;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class Bus extends MainActivity {


    public static final String api_key = "8b06798f5abf775a2973fc9d9970674d";
    public static final String app_key = "91a0f395";
    private static String URL = "http://transportapi.com/v3/uk/bus/stops/near.json?lat=51.527789&lon=-0.102323&page=3&rpp=10&api_key=d9307fd91b0247c607e098d5effedc97&app_id=03bf8009";
    static JSONObject jObj = null;
    static String json = "";
    private ArrayList<String> listItems = new ArrayList<String>();
    private static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.bus_transport, null, false);
        mDrawer.addView(contentView, 0);

        getNearby();

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

                String outPut = name;
                listItems.add(outPut);


            }


            adapter = new
                    ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    listItems);
            ListView myList = (ListView)
                    findViewById(R.id.listView2);
            myList.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
