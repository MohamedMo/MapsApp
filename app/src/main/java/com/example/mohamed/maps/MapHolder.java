package com.example.mohamed.maps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Mohamed on 09/04/2016.
 */
public class MapHolder extends MainActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.map_holder_view, null, false);
        mDrawer.addView(contentView, 0);




    }




}
