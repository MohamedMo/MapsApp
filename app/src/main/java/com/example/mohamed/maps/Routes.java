package com.example.mohamed.maps;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed on 09/04/2016.
 */
public class Routes {


    private String duration;
    private String distance;
    private ArrayList<String> instructions;
    private List<LatLng> list;
    private String summary;



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


    public List<LatLng> getList() {
        return list;
    }

    public void setList(List<LatLng> list) {
        this.list = list;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
