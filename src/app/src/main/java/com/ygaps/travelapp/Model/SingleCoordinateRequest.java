package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class SingleCoordinateRequest {
    @SerializedName("hasOneCoordinate")
    boolean hasOneCoordinate;
    @SerializedName("coordList")
    Coordinate coordList;

    public SingleCoordinateRequest( Coordinate coordList) {
        this.hasOneCoordinate = true;
        this.coordList = coordList;
    }

    public Coordinate getCoordList() {
        return coordList;
    }

    public void setCoordList(Coordinate coordList) {
        this.coordList = coordList;
    }
}