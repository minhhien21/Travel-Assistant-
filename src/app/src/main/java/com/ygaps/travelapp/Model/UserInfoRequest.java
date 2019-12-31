package com.ygaps.travelapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserInfoRequest {
    @SerializedName( "fullName")
    @Expose
    String fullName;
    @SerializedName( "email")
    @Expose
    String email;
    @SerializedName( "phone")
    @Expose
    String phone;
    @SerializedName( "gender")
    @Expose
    Number gender;
    @SerializedName( "dob")
    @Expose
    Date dob;

    public UserInfoRequest(){

    }

    public UserInfoRequest(String fullName, String email, String phone, Number gender, Date dob) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
    }

    public Number getGender() {
        return gender;
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

    public Date getDob() {
        return dob;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(Number gender) {
        this.gender = gender;
    }
}
