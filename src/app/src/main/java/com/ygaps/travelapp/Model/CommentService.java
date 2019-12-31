package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class CommentService {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("point")
    private String point;
    @SerializedName("feedback")
    private String feedback;
    @SerializedName("createdOn")
    private String createdOn;
    @SerializedName("avatar")
    private String avatar;

    public CommentService(int id, String name, String phone, String point, String feedback, String createdOn, String avatar) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.point = point;
        this.feedback = feedback;
        this.createdOn = createdOn;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPoint() {
        return point;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getAvatar() {
        return avatar;
    }
}
