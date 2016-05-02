package com.example.mohamed.maps;

import android.content.Context;
import android.content.Intent;
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
    Double ratings;

    private static ArrayAdapter<String> adapter;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayList<String> list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.favourites_view, null, false);
        mDrawer.addView(contentView, 0);


        favouritelv = (ListView)
                findViewById(R.id.listViewFavourites);

        handler = new DBHandler(this, null, null, 1);

        displayDB();


        favouritelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> instruct = new ArrayList<String>();
                ArrayList<String> lines = new ArrayList<String>();


                String item = (String) favouritelv.getItemAtPosition(position);
                ratings = handler.getRating(item);
                list = handler.getList(item);

                final String name = list.get(0);
                final String vicinity = list.get(1);
                //final String ratings = list.get(2);
                final String image = list.get(2);


                System.out.println("name =" + name);
                System.out.println("vicinity =" + vicinity);
                System.out.println("image " + name);
                Intent intent = new Intent(Favourites.this, PlacesHolder.class);
                intent.putExtra("name", name);
                intent.putExtra("vicinity", vicinity);
                intent.putExtra("image", image);
                intent.putExtra("ratings", ratings);


                startActivity(intent);

            }
        });


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


    public void displayDB() {

        listItems = handler.databaseToString();

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
