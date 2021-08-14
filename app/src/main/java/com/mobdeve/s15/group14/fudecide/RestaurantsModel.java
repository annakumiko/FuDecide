package com.mobdeve.s15.group14.fudecide;

public class RestaurantsModel {
    private String restName;
    private String desc;

    private RestaurantsModel() {}

    private RestaurantsModel(String restName, String desc) {
        this.restName = restName;
        this.desc = desc;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

