package com.example.asus.freingo.models;

import java.io.Serializable;

public class Utilisateur implements Serializable {

    private int id;
    private String username,nom,prenom,email,password,longitude,latitude;

    public Utilisateur(int id, String username, String nom, String prenom, String email, String password, String longitude, String latitude) {
        this.id = id;
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Utilisateur(String username, String nom, String prenom, String email, String password, String longitude, String latitude) {
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Utilisateur() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
