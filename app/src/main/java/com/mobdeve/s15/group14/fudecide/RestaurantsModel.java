package com.mobdeve.s15.group14.fudecide;

import java.io.Serializable;

import java.util.ArrayList;

public class RestaurantsModel implements Serializable{
    private double latitude, longitude;
    private ArrayList<MenuModel> menu;
    private String overallRating;
    private String restoDescription, restoName, openHours, restoPhoto, restoIcon;

    public RestaurantsModel() { }

    public RestaurantsModel(String openHours, double latitude, double longitude, String overallRating, String restoDescription, String restoName, String restoPhoto, String restoIcon) {
        this.openHours = openHours;
        this.latitude = latitude;
        this.longitude = longitude;
        this.overallRating = overallRating;
        this.restoDescription = restoDescription;
        this.restoName = restoName;
        this.restoPhoto = restoPhoto;
        this.restoIcon = restoIcon;
    }

    // getters
    public String getOpenHours() { return openHours; }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getOverallRating() {
        return overallRating;
    }
    public String getRestoDescription() { return restoDescription; }
    public String getRestoName() {
        return restoName;
    }
    public String getRestoPhoto() { return restoPhoto; }

    public String getRestoIcon() {
        return restoIcon;
    }
    public ArrayList<MenuModel> getMenu() { return menu; }

    // setters
    public void setOpenHours(String openHours) { this.openHours = openHours; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setOverallRating(String overallRating) { this.overallRating = overallRating; }
    public void setRestoDescription(String restoDescription) { this.restoDescription = restoDescription; }
    public void setRestoName(String restoName) {
        this.restoName = restoName;
    }
    public void setRestoPhoto(String restoPhoto) {
        this.restoPhoto = restoPhoto;
    }

    public void setRestoIcon(String restoIcon) {
        this.restoPhoto = restoIcon;
    }
    public void setMenu(ArrayList<MenuModel> menu) { this.menu = menu; }
}