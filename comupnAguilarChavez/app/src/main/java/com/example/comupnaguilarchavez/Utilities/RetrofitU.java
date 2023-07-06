package com.example.comupnaguilarchavez.Utilities;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitU {
    public static Retrofit build() {
        return new Retrofit.Builder()
                .baseUrl("https://64a6b013096b3f0fcc80508f.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
