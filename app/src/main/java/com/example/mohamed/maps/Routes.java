package com.example.mohamed.maps;

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Mohamed on 09/04/2016.
 */
public class Routes {


    private String duration;
    private String distance;
    private ArrayList<String> instructions;
    private ArrayList<String> list;
    private String summary;
    private PolylineOptions options;



    public Routes(){

    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }



    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public PolylineOptions getOptions() {
        return options;
    }

    public void setOptions(PolylineOptions options) {
        this.options = options;
    }
}
