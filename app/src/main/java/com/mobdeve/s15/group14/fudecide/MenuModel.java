package com.mobdeve.s15.group14.fudecide;

import java.io.Serializable;

public class MenuModel implements Serializable {
    private String itemName, itemPhoto, itemPrice;

    public MenuModel() { }

    public MenuModel(String itemName, String itemPhoto, String itemPrice) {
        this.itemName = itemName;
        this.itemPhoto = itemPhoto;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPhoto() {
        return itemPhoto;
    }

    public String getItemPrice() {
        return itemPrice;
    }
}
