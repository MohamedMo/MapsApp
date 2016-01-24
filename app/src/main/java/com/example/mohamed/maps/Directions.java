package com.example.mohamed.maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
 * Created by Mohamed on 24/01/2016.
 */
public class Directions extends Activity{


    private static String StartingLocation;
    private static String EndingLocation;
    private static String TravellingMethod;
    private static String URL;
    String method = "driving";
    private static ArrayList<String> Directions = new ArrayList<String>();
    public static final String GoogleAPIKey = "AIzaSyC2MMB-7iqMzrccgD9voZHiGf2nY093Jlg";
    private TextView textViewList;

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions);
    }

    public static void getDirections(String start, String end, String method) {

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
        File xmlFile = new File("Directions.xml");

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
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("step");

            System.out.println(nList.getLength());

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    Directions.add(eElement
                            .getElementsByTagName("html_instructions").item(0)
                            .getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Directions.isEmpty()){
            Directions.add("No data returned");
        }
    }




    public void onDirectionsClick (View v){


            EditText startPos = (EditText)findViewById(R.id.startLocation);
            EditText endPos = (EditText)findViewById(R.id.endLocation);
            String startingPos = startPos.getText().toString();
            String finishingPos = endPos.getText().toString();
            method = "walking";
            getDirections(startingPos, finishingPos, method);
            String finalDirections = "<html>";
            for(String s : Directions)
                finalDirections = finalDirections + s + "<br>";
            finalDirections = finalDirections + "</html>";
            textViewList = (TextView)findViewById(R.id.textViewDirectionsList);
            textViewList.setText(finalDirections);


    }





}
