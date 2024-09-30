package com.example.asus.freingo.remote;

import retrofit2.Call;

import com.example.asus.freingo.models.MyPlaces;
import com.example.asus.freingo.models.PlaceDetail;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ASUS on 27/03/2019.
 */

public interface IGoogleAPIService {

    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);

    @GET
    Call<PlaceDetail> getDetailPlace(@Url String url);

    @GET("maps/api/directions/json")
    Call<String> getDirections(@Query("origin") String origin,@Query("destination") String destination,@Query("mode") String mode);

    @GET
    Call<String> getDirectionsWithModeTransport(@Url String url);

}
