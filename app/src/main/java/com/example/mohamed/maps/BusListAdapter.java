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
public class BusListAdapter extends ArrayAdapter<String> {

    // declaring our ArrayList of items
    private ArrayList<String> start;
    private ArrayList<String> end;
    private ArrayList<String> duration;
    private ArrayList<String> line;

    public BusListAdapter(Context context, int textViewResourceId, ArrayList<String> start, ArrayList<String> end, ArrayList<String> duration, ArrayList<String> line) {
        super(context, textViewResourceId, start);
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.line = line;
    }


    public View getView(int position, View convertView, ViewGroup parent) {


        View v = convertView;


        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_bus_layout, null);
        }


        String i = start.get(position);
        String o = end.get(position);
        String p = duration.get(position);
        String h = line.get(position);
        if (i != null) {


            TextView tt = (TextView) v.findViewById(R.id.stepNo);
            TextView dd = (TextView) v.findViewById(R.id.journeyBusText);
            TextView aa = (TextView) v.findViewById(R.id.timeDuration);
            TextView gg = (TextView) v.findViewById(R.id.method);


            if (tt != null) {
                tt.setText(i);
            }

            dd.setText(o);

            aa.setText(p);


            gg.setText("Line - : " + h);


        }

        return v;

    }

}
