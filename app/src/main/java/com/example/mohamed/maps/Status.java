package com.example.mohamed.maps;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

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
 * Created by Mohamed on 28/03/2016.
 */
public class Status extends MainActivity {

    private static ArrayList<String> trainName = new ArrayList<String>();
    private static ArrayList<String> trainStatus = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.train_status, null, false);
        mDrawer.addView(contentView, 0);


        final TabHost host = (TabHost)findViewById(R.id.tabHost);


        host.setup();


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Now");
        spec.setContent(R.id.statusBakerloo);
        spec.setIndicator("Now");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("This Weekend");
       spec.setContent(R.id.List2);
        spec.setIndicator("This Weekend");
        host.addTab(spec);



        getStatus();


   //     final ListView lv = (ListView) findViewById(R.id.listViewStatus);

    }





    public void getStatus() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);



       String URL = "https://api.tfl.gov.uk/Line/Mode/tube/Status?detail=False&app_id=4cab813c&app_key=1506b972931bcbfa200a60b96cfd7b9a";

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


            JSONArray jsonResponse = new JSONArray(result);
           // JSONArray jsonMainNode = jsonResponse.getJSONArray()
            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject js = jsonResponse.getJSONObject(i);
                JSONArray jsonMainNode = js.getJSONArray("lineStatuses");
                JSONObject newJs = jsonMainNode.getJSONObject(0);
                String name = js.getString("name");
                String status = newJs.getString("statusSeverityDescription");
                System.out.println(name);
                System.out.println(status);


                trainName.add(name);
                trainStatus.add(status);
            }










        } catch (JSONException e) {
            e.printStackTrace();

        }


        TextView bakerloo = (TextView)findViewById(R.id.statusBakerloo);
        TextView central = (TextView)findViewById(R.id.statusCentral);
        TextView circle = (TextView)findViewById(R.id.statusCircle);
        TextView district = (TextView)findViewById(R.id.statusDistrict);
        TextView ham = (TextView)findViewById(R.id.statusHam);
        TextView jubilee = (TextView)findViewById(R.id.statusJubilee);
        TextView metro = (TextView)findViewById(R.id.statusMet);
        TextView northern = (TextView)findViewById(R.id.statusNorthern);
        TextView pica = (TextView)findViewById(R.id.statusPic);
        TextView victoria = (TextView)findViewById(R.id.statusVictoria);
        TextView waterloo = (TextView)findViewById(R.id.statusWater);



        bakerloo.setText(trainStatus.get(0));
        central.setText(trainStatus.get(1));
        circle.setText(trainStatus.get(2));
        district.setText(trainStatus.get(3));
        ham.setText(trainStatus.get(4));
        jubilee.setText(trainStatus.get(5));
        metro.setText(trainStatus.get(6));
        northern.setText(trainStatus.get(7));
        pica.setText(trainStatus.get(8));
        victoria.setText(trainStatus.get(8));
        waterloo.setText(trainStatus.get(9));




    }




}
