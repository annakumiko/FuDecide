package com.mobdeve.s15.group14.fudecide;

import java.io.Serializable;


public class RestaurantDist implements Serializable {
    float distance;
    RestaurantsModel restaurant;

    public RestaurantDist(float distance, RestaurantsModel restroom) {
        this.distance = distance;
        this.restaurant = restroom;
    }

    public float getDistance() {
        return distance;
    }

    public RestaurantsModel getRestaurant() {
        return restaurant;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setRestaurant(RestaurantsModel restaurant) {
        this.restaurant = restaurant;
    }
}
