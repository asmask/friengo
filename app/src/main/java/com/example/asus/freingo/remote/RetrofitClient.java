package com.example.asus.freingo.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ASUS on 27/03/2019.
 */

public class RetrofitClient {

    private static Retrofit retrofit=null;

    public static  Retrofit getClient(String baseUrl){
        if(retrofit == null){
            return retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return  retrofit;
    }

}
