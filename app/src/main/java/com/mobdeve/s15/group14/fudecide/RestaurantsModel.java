package com.mobdeve.s15.group14.fudecide;

import com.google.type.DateTime;

public class RestaurantsModel {
    private DateTime closeHour, openHour;
    private int latitude, longitude, overallRating;
    private String restoName, restoDescription, restoPhoto;
    // menu

    private RestaurantsModel() {}

    public RestaurantsModel(DateTime closeHour, DateTime openHour, int latitude, int longitude, int overallRating, String restoName, String restoDescription, String restoPhoto) {
        this.closeHour = closeHour;
        this.openHour = openHour;
        this.latitude = latitude;
        this.longitude = longitude;
        this.overallRating = overallRating;
        this.restoName = restoName;
        this.restoDescription = restoDescription;
        this.restoPhoto = restoPhoto;
    }

    public DateTime getCloseHour() {
        return closeHour;
    }

    public DateTime getOpenHour() {
        return openHour;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public String getRestoName() {
        return restoName;
    }

    public String getRestoDescription() {
        return restoDescription;
    }

    public String getRestoPhoto() {
        return restoPhoto;
    }
}

