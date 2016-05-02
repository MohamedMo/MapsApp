package com.example.mohamed.maps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    ArrayList<Routes> routes;
    private ArrayList<String> duration;
    private ArrayList<String> end;
    private ArrayList<String> line;
    private List<LatLng> list;
    private GoogleMap mMap;
    LayoutInflater vi;
    int Resource;
    String method;
    RecyclerView.ViewHolder holder;


    protected ListView mListView;

    public DirectionsAdapter(Context context, int resource, ArrayList<Routes> objects, String method) {
        super(context, resource, objects);
        this.duration = duration;
        this.list = list;
        Resource = resource;
        routes = objects;
        this.method = method;

    }


    public View getView(int position, View convertView, ViewGroup parent) {


        View v = convertView;


        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.directions_list_view, null);
        }


        TextView duration = (TextView) v.findViewById(R.id.duration);
        TextView locat = (TextView) v.findViewById(R.id.location);
        TextView distance = (TextView) v.findViewById(R.id.distance);


        duration.setText(routes.get(position).getDuration());


        locat.setText("via  " + routes.get(position).getSummary());


        distance.setText(routes.get(position).getDistance());


        if (method.equalsIgnoreCase("WALKING")) {


            ImageView image = (ImageView) v.findViewById(R.id.imageList);
            image.setImageResource(R.drawable.walking);

        }

        if (method.equalsIgnoreCase("BICYCLING")) {


            ImageView image = (ImageView) v.findViewById(R.id.imageList);
            image.setImageResource(R.drawable.cycle);

        }

        if (method.equalsIgnoreCase("DRIVING")) {


            ImageView image = (ImageView) v.findViewById(R.id.imageList);
            image.setImageResource(R.drawable.drive);

        }


        return v;

    }


}
