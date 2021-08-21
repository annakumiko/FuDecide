package com.mobdeve.s15.group14.fudecide;

import com.google.type.DateTime;

import java.util.ArrayList;

public class RestaurantsModel {
    private DateTime closeHour;
    private int latitude, longitude;
    private MenuModel menu[];
    private DateTime openHour;
    private int overallRating;
    private String restoDescription, restoName, restoPhoto;

    private RestaurantsModel() {}

    public RestaurantsModel(DateTime closeHour, int latitude, int longitude, MenuModel[] menu, DateTime openHour, int overallRating, String restoDescription, String restoName, String restoPhoto) {
        this.closeHour = closeHour;
        this.latitude = latitude;
        this.longitude = longitude;
        this.menu = menu;
        this.openHour = openHour;
        this.overallRating = overallRating;
        this.restoDescription = restoDescription;
        this.restoName = restoName;
        this.restoPhoto = restoPhoto;
    }

    public DateTime getCloseHour() {
        return closeHour;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public MenuModel[] getMenu() {
        return menu;
    }

    public DateTime getOpenHour() {
        return openHour;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public String getRestoDescription() {
        return restoDescription;
    }

    public String getRestoName() {
        return restoName;
    }

    public String getRestoPhoto() {
        return restoPhoto;
    }
}