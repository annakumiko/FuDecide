package com.mobdeve.s15.group14.fudecide;

public class MenuModel {
    private MenuModel() {}; //wait need ba ng semi-colon dito...

    private String itemName, itemDescription, itemPhoto;
    private int itemPrice;

    public MenuModel(String itemName, String itemDescription, String itemPhoto, int itemPrice){
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPhoto = itemPhoto;
        this.itemPrice = itemPrice;
    }

    public String getItemName(){ return itemName; }
    public String getItemDescription() { return itemDescription; }
    public String getItemPhoto() { return itemPhoto; }
    public int getItemPrice() { return itemPrice; }
}
