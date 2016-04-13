package com.example.mohamed.maps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class listViewHolder extends MainActivity {


    TextView resultText;
    TextView resultText1;
    TextView resultText2;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_list_view_holder, null, false);
        mDrawer.addView(contentView, 0);

        resultText = (TextView) findViewById(R.id.textView4);
        resultText1 = (TextView) findViewById(R.id.textView5);
        resultText2 = (TextView) findViewById(R.id.textView6);


//        String urlphoto = (getIntent().getExtras().getString("urls"));
//
//        try {
//            ImageView i = (ImageView)findViewById(R.id.imagesForPlace);
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(urlphoto).getContent());
//            i.setImageBitmap(bitmap);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        resultText.setText(getIntent().getExtras().getString("name"));
        resultText1.setText(getIntent().getExtras().getString("vicinity"));
   //     resultText1.setText(getIntent().getExtras().getString("local"));


    }




}
