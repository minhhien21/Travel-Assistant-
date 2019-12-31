package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationListResponse {
    @SerializedName("total")
    String total;
    @SerializedName("tours")
    ArrayList<NotificationDetail> notificationDetailList;

    public String getName() {
        return total;
    }

    public ArrayList<NotificationDetail> getNotificationDetailList() {
        return notificationDetailList;
    }
}
