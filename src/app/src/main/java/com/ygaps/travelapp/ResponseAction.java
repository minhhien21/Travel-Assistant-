package com.ygaps.travelapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.View.LoginActivity;
import com.ygaps.travelapp.View.MainActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseAction extends BroadcastReceiver {
    private String token = LoginActivity.TOKEN;
    private String tourId;
    private String message;
    @Override
    public void onReceive(Context context, Intent intent) {
        tourId = intent.getStringExtra("tourId");
        message = intent.getStringExtra("message");
        String action = intent.getStringExtra("action");
        Log.e("FROM:",tourId + message);
        if(action.equals("OK")){
            AcceptInvitation();

        }

        else if(action.equals("Cancel")){
            RefuseInvitation();
            Toast.makeText(context,"Canceled invitation",Toast.LENGTH_LONG).show();
        }
        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    private void RefuseInvitation() {

    }

    private void AcceptInvitation() {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .acceptTourInvitation(token,tourId, true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("ATAG", "" + response.body().toString());
                    Log.e("ATAG", "" + response.body().toString());
                }

                else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}
