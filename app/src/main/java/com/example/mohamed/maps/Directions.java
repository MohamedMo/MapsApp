package com.example.mohamed.maps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Mohamed on 24/01/2016.
 */
public class Directions extends Activity implements View.OnClickListener, TextToSpeech.OnInitListener{


    private TextView resultText;
    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;

    private static String StartingLocation;
    private static String EndingLocation;
    private static String TravellingMethod;
    private static String URL;
    String method = "driving";
    private static ArrayList<String> Directions = new ArrayList<String>();
    private static final String urlLink ="https://maps.googleapis.com/maps/api/directions/xml?origin=Pelterstreet&destination=Pelterstreet&mode=walking&key=AIzaSyC2MMB-7iqMzrccgD9voZHiGf2nY093Jlg";
    private TextView textViewList;
    private static final String TAG = "Demo";
    public static final String GoogleAPIKey = "AIzaSyC2MMB-7iqMzrccgD9voZHiGf2nY093Jlg";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions);
        //get a reference to the button element listed in the XML layout
        Button speakButton = (Button)findViewById(R.id.btnReadText);
        //listen for clicks
        speakButton.setOnClickListener(this);

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
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
                resultText.setText(result.get(0));
            }
                break;
        }

    }


    public void onDirectionsClick(View v)  {

        System.out.println("hello");

        EditText start = (EditText)findViewById(R.id.startLocation);
        EditText end = (EditText)findViewById(R.id.endLocation);
       TextView resultDirections = (TextView)findViewById(R.id.textViewDirectionsList);
        String startingPos = start.getText().toString();
        String finishingPos = end.getText().toString();
        getDirections(startingPos, finishingPos, method);
        String finalDirections = "<html>";
        for(String s : Directions)
            finalDirections = finalDirections + s + "<p>";
        finalDirections = finalDirections + "</html>";
        String result = Html.fromHtml(finalDirections).toString();
        resultDirections.setText(result);



        }


    public void onReadText (View v){

    }

    @Override
    public void onClick(View v) {
        //get the text entered
        TextView enteredText = (TextView)findViewById(R.id.textViewDirectionsList);
        String words = enteredText.getText().toString();
        speakWords(words);
    }

    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
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

    public void getDirections(String start, String end, String method) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

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
            Log.d(TAG,"error url",e);
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

}