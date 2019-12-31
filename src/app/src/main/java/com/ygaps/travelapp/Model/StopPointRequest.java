package com.ygaps.travelapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StopPointRequest {
    @SerializedName( "tourId")
    @Expose
    String tourId;
    @SerializedName( "stopPoints" )
    @Expose
    ArrayList<StopPointInfos> stopPoints;

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public ArrayList<StopPointInfos> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(ArrayList<StopPointInfos> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public StopPointRequest(String tourId, ArrayList<StopPointInfos> stopPoints) {
        this.tourId = tourId;
        this.stopPoints = stopPoints;
    }
}
