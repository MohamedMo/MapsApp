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
    private ArrayList<String> polyline;
    private ArrayList<String> circles;
    private List<LatLng> list;



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

    public ArrayList<String> getPolyline() {
        return polyline;
    }

    public void setPolyline(ArrayList<String> polyline) {
        this.polyline = polyline;
    }

    public ArrayList<String> getCircles() {
        return circles;
    }

    public void setCircles(ArrayList<String> circles) {
        this.circles = circles;
    }
}
