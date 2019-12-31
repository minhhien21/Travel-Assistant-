package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CoordinateSet {
    @SerializedName("coordinateSet")
    ArrayList<Coordinate> coordinates;

    public CoordinateSet(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
}