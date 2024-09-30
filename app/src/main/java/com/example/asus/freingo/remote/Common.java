package com.example.asus.freingo.remote;

import com.example.asus.freingo.models.MyPlaces;
import com.example.asus.freingo.models.Results;
import com.example.asus.freingo.remote.IGoogleAPIService;
import com.example.asus.freingo.remote.RetrofitClient;

/**
 * Created by ASUS on 27/03/2019.
 */

public class Common {

    public static String curentToken="";

    //
    public static Results currentResult;

    public static final String GOOGLE_API_URL= "https://maps.googleapis.com";

    public static IGoogleAPIService getGoogleAPIService(){

        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);

    }
    public static IGoogleAPIService getGoogleAPIServiceScalars(){

        return RetrofitScalarsClient.getScalarsClient(GOOGLE_API_URL).create(IGoogleAPIService.class);

    }
}
