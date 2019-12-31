package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName( "userId" )
    private String userId;
    @SerializedName( "token" )
    private String token;
    @SerializedName( "message" )
    private String message;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
