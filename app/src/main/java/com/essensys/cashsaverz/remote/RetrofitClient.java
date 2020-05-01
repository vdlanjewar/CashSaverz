package com.essensys.cashsaverz.remote;

import com.essensys.cashsaverz.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static Retrofit retrofithome = null;

    public static Retrofit getClient(String baseUrl) {
        Gson gson = new GsonBuilder().setLenient().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_URL+baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        return retrofit;
    }

    public static Retrofit getClientHome(String baseUrl) {
        Gson gson = new GsonBuilder().setLenient().create();

        if (retrofithome==null) {
            retrofithome = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofithome;
    }
}