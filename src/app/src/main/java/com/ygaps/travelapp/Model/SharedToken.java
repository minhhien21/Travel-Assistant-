package com.ygaps.travelapp.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SharedToken {

    private Context context;

    public SharedToken(Context context) {
        this.context = context;
    }


    public void saveToken(Context context, TokenInfo token){
        SharedPreferences mShared = context.getSharedPreferences( "token",MODE_PRIVATE );
        SharedPreferences.Editor editor = mShared.edit();
        Gson gson = new Gson();
        String json = gson.toJson(token);
        editor.putString( "TokenInfo",json );
        editor.commit();
    }

    public String getToken(Context context){
        SharedPreferences mShared = context.getSharedPreferences( "token",MODE_PRIVATE );
        Gson gson = new Gson();
        String json =  mShared.getString( "TokenInfo","" );
        TokenInfo obj = gson.fromJson(json,TokenInfo.class);
        return obj.getToken();
    }
}
