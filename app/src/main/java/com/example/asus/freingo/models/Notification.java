package com.example.asus.freingo.models;

/**
 * Created by ASUS on 29/03/2019.
 */

public class Notification {

    private int id;
    private String title,text,image;

    public Notification() {
    }

    public Notification(String title, String text) {
        this.title = title;
        this.text = text;

    }
    public Notification(String title, String text,String image) {
        this.title = title;
        this.text = text;
        this.image=image;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
