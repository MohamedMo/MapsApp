package com.example.mohamed.maps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Mohamed on 09/04/2016.
 */
public class MapHolder extends MainActivity {


    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.map_holder_view, null, false);
        mDrawer.addView(contentView, 0);




        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getIntent().getExtras().getStringArrayList("instruct"));
        ListView listView = (ListView)findViewById(R.id.listViewinstruct);
        listView.setAdapter(adapter);


    }




}
