package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class UserSearchResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("gender")
    private String gender;
    @SerializedName("dob")
    private String dob;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("typeLogin")
    private String typeLogin;

    public UserSearchResponse(int id, String fullName, String email, String phone, String gender, String dob, String avatar, String typeLogin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.avatar = avatar;
        this.typeLogin = typeLogin;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTypeLogin() {
        return typeLogin;
    }
}
