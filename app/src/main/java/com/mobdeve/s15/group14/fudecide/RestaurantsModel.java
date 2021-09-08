package com.mobdeve.s15.group14.fudecide;

import com.google.type.DateTime;

import java.util.ArrayList;

public class RestaurantsModel {
    private String openHours;
    private double latitude, longitude;
    private ArrayList<MenuModel> menu;
    private String overallRating;
    private String restoDescription, restoName, restoPhoto;
//    private ArrayList<ReviewsModel> reviews;

    public RestaurantsModel() { }

    public RestaurantsModel(String openHours, double latitude, double longitude, String overallRating, String restoDescription, String restoName, String restoPhoto) {
        this.openHours = openHours;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.menu = menu;
        this.overallRating = overallRating;
        this.restoDescription = restoDescription;
        this.restoName = restoName;
        this.restoPhoto = restoPhoto;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
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

    public ArrayList<MenuModel> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<MenuModel> menu) {
        this.menu = menu;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    public String getRestoDescription() {
        return restoDescription;
    }

    public void setRestoDescription(String restoDescription) {
        this.restoDescription = restoDescription;
    }

    public String getRestoName() {
        return restoName;
    }

    public void setRestoName(String restoName) {
        this.restoName = restoName;
    }

    public String getRestoPhoto() {
        return restoPhoto;
    }

    public void setRestoPhoto(String restoPhoto) {
        this.restoPhoto = restoPhoto;
    }

//    public ArrayList<ReviewsModel> getReviews(){
//        return reviews;
//    }
}