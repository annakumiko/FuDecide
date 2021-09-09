package com.mobdeve.s15.group14.fudecide;

public class MenuModel {
    private String itemDescription, itemName, itemPhoto;
    private int itemPrice;

    public MenuModel() {}

    public MenuModel(String itemDescription, String itemName, String itemPhoto, int itemPrice) {
        this.itemDescription = itemDescription;
        this.itemName = itemName;
        this.itemPhoto = itemPhoto;
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPhoto() {
        return itemPhoto;
    }

    public int getItemPrice() {
        return itemPrice;
    }
}
