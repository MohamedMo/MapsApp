package com.example.mohamed.maps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by Mohamed on 26/04/2016.
 */
public class Favourites extends MainActivity {


    ListView favouritelv;
    DBHandler handler;

    private static ArrayAdapter<String> adapter;
    private ArrayList<String> listItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.favourites_view, null, false);
        mDrawer.addView(contentView, 0);


        favouritelv = (ListView)
                findViewById(R.id.listViewFavourites);

        handler = new DBHandler(this, null ,null , 1);

        displayDB();



        favouritelv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) favouritelv.getItemAtPosition(position);
                handler.deleteItem(item);
                adapter.remove(item);
                adapter.notifyDataSetChanged();

                return false;
            }
        });
    }




    public void displayDB (){

        listItems= handler.databaseToString();

        adapter = new
                ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listItems);
        favouritelv = (ListView)
                findViewById(R.id.listViewFavourites);


        favouritelv.setAdapter(adapter);

    }
}
