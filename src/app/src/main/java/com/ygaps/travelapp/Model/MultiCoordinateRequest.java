package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MultiCoordinateRequest {
    @SerializedName("hasOneCoordinate")
    boolean hasOneCoordinate;
    @SerializedName("coordList")
    ArrayList<CoordinateSet> coordList;

    public MultiCoordinateRequest(ArrayList<CoordinateSet> coordList) {
        this.hasOneCoordinate = false;
        this.coordList = coordList;
    }

    public ArrayList<CoordinateSet> getCoordList() {
        return coordList;
    }

    public void setCoordList(ArrayList<CoordinateSet> coordList) {
        this.coordList = coordList;
    }
}