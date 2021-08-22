package com.mobdeve.s15.group14.fudecide;

import com.google.type.DateTime;

public class RestaurantsModel {
    private String restoName, restoDescription, restoPhoto;
    private int overallRating, latitude, longitude;
    private DateTime openHour, closeHour;
    private MenuModel menu[];

    private RestaurantsModel() {}

    private RestaurantsModel(String restoName, String restoDescription, String restoPhoto,
                             int overallRating, int latitude, int longitude,
                             DateTime openHour, DateTime closeHour,
                             MenuModel[] menu) {

        this.restoName = restoName;
        this.restoDescription = restoDescription;
        this.restoPhoto = restoPhoto;

        this.overallRating = overallRating;
        this.latitude = latitude;
        this.longitude = longitude;

        this.openHour = openHour;
        this.closeHour = closeHour;

        this.menu = menu;

    }

    public String getRestoName() { return restoName; }
    public String getRestoDescription() { return restoDescription; }
    public String getRestoPhoto() { return restoPhoto; }

    public int getOverallRating() { return overallRating; }
    public int getLatitude() { return latitude; }
    public int getLongitude() { return longitude; }

    public DateTime getOpenHour() { return openHour; }
    public DateTime getCloseHour() { return closeHour; }

    public MenuModel[] getMenu() { return menu; }
}

