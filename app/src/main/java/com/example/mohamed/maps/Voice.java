package com.example.mohamed.maps;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Mohamed on 23/01/2016.
 */
public class Voice extends Activity implements View.OnClickListener, TextToSpeech.OnInitListener {



   private TextView resultText;
    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice);
        resultText = (TextView)findViewById(R.id.textViewResult);

        //get a reference to the button element listed in the XML layout
        Button speakButton = (Button)findViewById(R.id.btnSayText);
        //listen for clicks
        speakButton.setOnClickListener(this);

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    public void onVoiceClick (View v){

        if (v.getId() == R.id.imageButton){

            promptSpeechInput();
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
            Toast.makeText(Voice.this,"Sorry",Toast.LENGTH_LONG).show();
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

        super.onActivityResult(request_code,result_code,i);

        switch (request_code){

            case 100: if (result_code == RESULT_OK && i != null){
                ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultText.setText(result.get(0));
            }
                break;
        }

    }


    public void onSayTextClick (View v){


    }

    @Override
    public void onClick(View v) {
        //get the text entered
        EditText enteredText = (EditText)findViewById(R.id.sayText);
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

}
