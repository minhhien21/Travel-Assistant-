
package com.ygaps.travelapp.Network;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {
    private Retrofit retrofit;
    private static RetrofitClient instance;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://35.197.153.192:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public UserService getUserService() {
        return retrofit.create(UserService.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
