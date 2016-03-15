package com.example.mohamed.maps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class listViewHolder extends MainActivity {


    TextView resultText;
    TextView resultText1;
    TextView resultText2;

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

        resultText.setText(getIntent().getExtras().getString("name"));
        resultText1.setText(getIntent().getExtras().getString("local"));
    }




}
