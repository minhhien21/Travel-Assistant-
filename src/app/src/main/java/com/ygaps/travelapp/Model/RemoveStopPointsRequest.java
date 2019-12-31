package com.ygaps.travelapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RemoveStopPointsRequest {
    @SerializedName("tourId")
    @Expose
    private String tourId;
    @SerializedName("deleteIds")
    @Expose
    ArrayList<Number> deleteIds;

    public RemoveStopPointsRequest(String tourId, ArrayList<Number> deleteIds) {
        this.tourId = tourId;
        this.deleteIds = deleteIds;
    }

    public RemoveStopPointsRequest(){
        this.deleteIds = new ArrayList<>();
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public ArrayList<Number> getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(ArrayList<Number> deleteIds) {
        this.deleteIds = deleteIds;
    }
}
