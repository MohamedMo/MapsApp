package com.example.mohamed.maps;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed on 09/04/2016.
 */
public class DirectionsAdapter extends ArrayAdapter<Routes> {

    ArrayList<Routes>routes;
    private ArrayList<String> duration;
    private ArrayList<String> end;
    private ArrayList<String> line;
    private List<LatLng> list;
    private GoogleMap mMap;
    LayoutInflater vi;
    int Resource;
    RecyclerView.ViewHolder holder;


    protected ListView mListView;

    public DirectionsAdapter(Context context, int resource,  ArrayList<Routes> objects) {
        super(context, resource, objects);
        this.duration = duration;
        this.list=list;
        Resource = resource;
        routes = objects;
    }


    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.directions_list_view, null);
        }


        Button navigate = (Button) v.findViewById(R.id.btnNavigate);
        navigate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final int position = mListView.getPositionForView((View) v.getParent());

                routes.get(position);

            }
        });

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */



            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView duration = (TextView) v.findViewById(R.id.duration);
            TextView locat = (TextView) v.findViewById(R.id.location);
        TextView distance = (TextView) v.findViewById(R.id.distance);


        // check to see if each individual textview is null.
        // if not, assign some text!

        duration.setText(routes.get(position).getDuration());
        duration.setTextColor(Color.parseColor("#948CE8"));

        locat.setText("via  " + routes.get(position).getSummary());



        distance.setText(routes.get(position).getDistance());
        distance.setTextColor(Color.parseColor("#948CE8"));




        // the view must be returned to our activity
        return v;

    }


}
