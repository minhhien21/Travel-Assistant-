package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PointStatResponse {
    @SerializedName("pointStats")
    ArrayList<PointStatInfo> pointStats;

    public ArrayList<PointStatInfo> getPointStats() {
        return pointStats;
    }
}
