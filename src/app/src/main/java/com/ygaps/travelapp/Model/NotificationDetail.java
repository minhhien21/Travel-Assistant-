package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class NotificationDetail {
    @SerializedName("id")
    String tourId;
    @SerializedName("hostName")
    String hostName;
    @SerializedName("name")
    String name;
    @SerializedName("createdOn")
    String createdOn;

    public String getHostName() {
        return hostName;
    }

    public String getName() {
        return name;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getTourId() {
        return tourId;
    }
}
