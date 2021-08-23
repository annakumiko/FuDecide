package com.mobdeve.s15.group14.fudecide;

import android.view.Menu;

import com.google.type.DateTime;

import java.util.ArrayList;

public class RestaurantsModel {
    private String restoName, restoDescription, restoPhoto;
    private int overallRating;
    private Double latitude, longitude;
    private DateTime openHour, closeHour;
    private ArrayList<Menu> menu;

    public String getRestoName() { return restoName; }
    public String getRestoDescription() { return restoDescription; }
    public String getRestoPhoto() { return restoPhoto; }

    public int getOverallRating() { return overallRating; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }

    public DateTime getOpenHour() { return openHour; }
    public DateTime getCloseHour() { return closeHour; }

    public ArrayList<Menu> getMenu(){ return menu; }
}

