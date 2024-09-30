package com.example.asus.freingo.Adapter;


import android.content.Context;

import com.ramotion.expandingcollection.ECCardData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.example.asus.freingo.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONObject;

public class CardDataImpl implements ECCardData<String> {

    private String cardTitle;
    private Integer mainBackgroundResource;
    private Integer headBackgroundResource;
    private RequestCreator mainBackground;
    private RequestCreator headBackground;
    private List<String> listItems;
    private String phone,adresse,openning_time,url;
    private double rating;
    private int place_id;

    Context context;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getPhone() {
        return phone;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getOpenning_time() {
        return openning_time;
    }

    public double getRating() {
        return rating;
    }

    public CardDataImpl(String cardTitle, Integer mainBackgroundResource, Integer headBackgroundResource,
                        String phone, String adresse, String openning_time, double rating,String url
    ) {
        this.mainBackgroundResource = mainBackgroundResource;
        this.headBackgroundResource = headBackgroundResource;
        this.cardTitle = cardTitle;
        this.phone = phone;
        this.adresse = adresse;
        this.openning_time = openning_time;
        this.rating = rating;
        this.url = url;

    }

    @Override
    public Integer getMainBackgroundResource() {
        return mainBackgroundResource;
    }

    @Override
    public Integer getHeadBackgroundResource() {
        return headBackgroundResource;
    }

    @Override
    public List<String> getListItems() {
        return listItems;
    }

    public String getCardTitle() {
        return cardTitle;
    }



}