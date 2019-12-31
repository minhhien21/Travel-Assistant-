package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class TokenInfo {
    @SerializedName( "id" )
    private String id;
    @SerializedName( "username" )
    private String username;
    @SerializedName( "token" )
    private String token;

    public TokenInfo(){

    }

    public TokenInfo(String id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
