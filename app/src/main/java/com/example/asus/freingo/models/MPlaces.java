package com.example.asus.freingo.models;

import java.util.Date;
import java.util.List;

public class MPlaces {

        public int id;
        public String place_name ;
        public double latitude;
        public double longitude;
        public String place_adresse;
        public double rating;
        public String url ;
        public String website ;
        public String opening_time;
        public String phone_number ;
        public String img,distance,duration;


    public MPlaces(int id, String place_name, double latitude, double longitude, String place_adresse, double rating, String url, String website, String opening_time, String phone_number) {
        this.id = id;
        this.place_name = place_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.place_adresse = place_adresse;
        this.rating = rating;
        this.url = url;
        this.website = website;
        this.opening_time = opening_time;
        this.phone_number = phone_number;
    }

    public MPlaces(int id, String place_name, double latitude, double longitude, String place_adresse, double rating, String url, String website, String opening_time, String phone_number, String img, String distance, String duration) {
        this.id = id;
        this.place_name = place_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.place_adresse = place_adresse;
        this.rating = rating;
        this.url = url;
        this.website = website;
        this.opening_time = opening_time;
        this.phone_number = phone_number;
        this.img = img;
        this.distance = distance;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "MPlaces{" +
                "id=" + id +
                ", place_name='" + place_name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", place_adresse='" + place_adresse + '\'' +
                ", rating=" + rating +
                ", url='" + url + '\'' +
                ", website='" + website + '\'' +
                ", opening_time='" + opening_time + '\'' +
                ", phone_number='" + phone_number + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlace_adresse() {
        return place_adresse;
    }

    public void setPlace_adresse(String place_adresse) {
        this.place_adresse = place_adresse;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
