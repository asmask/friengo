package com.example.asus.freingo.Adapter;

public class Card {
    private String img,title,adresse,phone,time;
    private double rating;

    public Card(String img, String title, String adresse, String phone, String time, double rating) {
        this.img = img;
        this.title = title;
        this.adresse = adresse;
        this.phone = phone;
        this.time = time;
        this.rating = rating;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
