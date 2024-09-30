package com.example.asus.friengo.Models;

/**
 * Created by ASUS on 10/02/2019.
 */

public class Item {
    private String title,desc;
    private int rating,img;

    public Item(String title, String desc, int img, int rating) {
        this.title = title;
        this.desc = desc;
        this.img = img;
        this.rating = rating;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public Item() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
