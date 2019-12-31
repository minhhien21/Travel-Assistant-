package com.ygaps.travelapp.Model;
import com.google.gson.annotations.SerializedName;
public class RegisterResponse {

    @SerializedName("fullName")
    String _fullName;
    @SerializedName("createdOn")
    String _createdOn;
    @SerializedName("password")
    String _password;
    @SerializedName("email")
    String _email;
    @SerializedName("phone")
    String _phone;
    @SerializedName("id")
    String _id;

    public String get_fullName() {
        return _fullName;
    }

    public String get_createdOn() {
        return _createdOn;
    }

    public String get_password() {
        return _password;
    }

    public String get_email() {
        return _email;
    }

    public String get_phone() {
        return _phone;
    }

    public String get_id() {
        return _id;
    }
}
