package com.example.mohamed.maps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohamed on 02/04/2016.
 */
public class BusListAdapter extends ArrayAdapter<String>{

    // declaring our ArrayList of items
    private ArrayList<String> start;
    private ArrayList<String> end;
    private ArrayList<String> duration;
    private ArrayList<String> line;

    public BusListAdapter(Context context, int textViewResourceId, ArrayList<String> start , ArrayList<String> end ,ArrayList<String> duration , ArrayList<String> line) {
        super(context, textViewResourceId, start);
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.line = line;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_bus_layout, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        String i = start.get(position);
        String o = end.get(position);
        String p = duration.get(position);
        String h = line.get(position);
        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView tt = (TextView) v.findViewById(R.id.stepNo);
            TextView dd = (TextView) v.findViewById(R.id.journeyBusText);
            TextView aa = (TextView) v.findViewById(R.id.timeDuration);
            TextView gg = (TextView) v.findViewById(R.id.method);


            // check to see if each individual textview is null.
            // if not, assign some text!
            if (tt != null){
                tt.setText(i);
            }

           dd.setText(o);

           aa.setText(p);


            gg.setText("Line - : " + h);


        }

        // the view must be returned to our activity
        return v;

    }

}
