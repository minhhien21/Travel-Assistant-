package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllReviewResponse {

    @SerializedName("reviewList")
    ArrayList<ReviewInfo> reviewList;
    public ArrayList<ReviewInfo> getReviewList() {
        return reviewList;
    }
}
