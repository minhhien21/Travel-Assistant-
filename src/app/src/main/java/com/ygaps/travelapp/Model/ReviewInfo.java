package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class ReviewInfo {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("avatar")
    String avatar;
    @SerializedName("review")
    String review;
    @SerializedName("point")
    int point;
    @SerializedName("createdOn")
    String createdOn;

    public ReviewInfo(int aId, String aName, String aComment, String aAvatar)
    {
        id = aId;
        name = aName;
        review = aComment;
        avatar = "";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getReview() {
        return review;
    }

    public int getPoint() {
        return point;
    }

    public String getCreatedOn() {
        return createdOn;
    }
}
