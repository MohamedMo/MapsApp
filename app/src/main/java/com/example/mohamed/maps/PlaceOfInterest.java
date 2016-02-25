package com.example.mohamed.maps;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private static ArrayList<String> POFplace = new ArrayList<String>();
    private static ArrayAdapter<String> adapter;
   private File xmlFile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.place_of_interest, null, false);
        mDrawer.addView(contentView, 0);
        File dir = new File(this.getFilesDir() + "/Users/Mohamed/AndroidStudioProjects");
        dir.mkdirs(); //create folders where write files
      xmlFile = new File(dir, "NearMe.xml");
    }


    private String getCurrentLocation() {
        String Longitude = "-0.066720";
        String Latitude = "51.526974";
        return String.format("location=%s,%s&radius=500&", Latitude, Longitude);
    }



    public void onBtnShop(View v)  {

   getNearby("shopping_mall");


    }

    public void onBtnBar(View v)  {

        getNearby("bar");


    }

    public void onBtnGym(View v)  {

        getNearby("gym");


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


        displayResults();

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
        String finalDirections = "<html>";
        for(String s : POF)
            finalDirections = finalDirections + s;
        finalDirections = finalDirections + "</html>";
        String result = Html.fromHtml(finalDirections).toString();
        //resultDirections.setText(result);
       // resultPOF.setText(result);
        adapter=new
                ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                POF);
        ListView myList=(ListView)
                findViewById(R.id.listView);
        myList.setAdapter(adapter);


    }




}
