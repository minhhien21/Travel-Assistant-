package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class Coordinate {
    @SerializedName("lat")
    Number latitude;
    @SerializedName("long")
    Number longtitude;

    public Coordinate(Number latitude, Number longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public Number getLatitude() {
        return latitude;
    }

    public void setLatitude(Number latitude) {
        this.latitude = latitude;
    }

    public Number getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Number longtitude) {
        this.longtitude = longtitude;
    }
}
