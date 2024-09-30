package com.example.asus.freingo.service;

import com.example.asus.freingo.remote.Common;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ASUS on 29/03/2019.
 */

public class MyFirebaseService extends FirebaseInstanceIdService{


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken= FirebaseInstanceId.getInstance().getToken();
        Common.curentToken=refreshedToken;
    }
}
