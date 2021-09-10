package com.mobdeve.s15.group14.fudecide;

public class MenuModel {
    private String itemDescription, itemName, itemPhoto;
    private int itemPrice;

    public MenuModel() {}

    public MenuModel(String itemName, String itemPhoto, int itemPrice) {
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

    public int getItemPrice() {
        return itemPrice;
    }
}
