package com.example.mohamed.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class PlacesHolder extends MainActivity {


    TextView resultText;
    TextView resultText1;
    TextView resultText2;
    ImageView i;
    DBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_list_view_holder, null, false);
        mDrawer.addView(contentView, 0);

        handler = new DBHandler(this, null, null, 1);

        resultText = (TextView) findViewById(R.id.textView4);
        resultText1 = (TextView) findViewById(R.id.textView5);
        resultText2 = (TextView) findViewById(R.id.ratings);
        i = (ImageView) findViewById(R.id.image1);

        String urlphoto = (getIntent().getExtras().getString("image"));
        System.out.println(urlphoto);

        if (urlphoto != null ? urlphoto.equalsIgnoreCase("null") : false) {

            i.setImageResource(R.drawable.no_image);
        } else {
            try {
                i = (ImageView) findViewById(R.id.image1);
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(urlphoto).getContent());
                i.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        resultText.setText(getIntent().getExtras().getString("name"));
        resultText1.setText(getIntent().getExtras().getString("vicinity"));
        resultText2.setText(Double.toString(getIntent().getExtras().getDouble("ratings")));
        //     resultText1.setText(getIntent().getExtras().getString("local"));


    }


    public void addToFavourites(View v) {


        String name = getIntent().getExtras().getString("name");
        if (handler.check(name)) {
            Toast.makeText(this, "Already in favourites ", Toast.LENGTH_LONG).show();
        } else {
            String vicinity = getIntent().getExtras().getString("vicinity");
            Double ratings = getIntent().getExtras().getDouble("ratings");
            String urlPhoto = getIntent().getExtras().getString("image");
            handler.addToDatabase(name, vicinity, ratings, urlPhoto);

            Toast.makeText(this, "Added to favourites ", Toast.LENGTH_LONG).show();
        }
    }

}
