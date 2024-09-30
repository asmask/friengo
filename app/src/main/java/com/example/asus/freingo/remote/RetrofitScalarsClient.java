package com.example.asus.freingo.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitScalarsClient {

    private static Retrofit retrofit=null;

    public static  Retrofit getScalarsClient(String baseUrl){
        if(retrofit == null){
            return retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }

}
