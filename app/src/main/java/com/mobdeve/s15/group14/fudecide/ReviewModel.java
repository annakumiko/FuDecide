package com.mobdeve.s15.group14.fudecide;

import java.io.Serializable;

public class ReviewModel implements Serializable {
    private String reviewID, name, restoName, reviewText, datePosted;
    private double rating;

    public ReviewModel() { }

    public ReviewModel(String reviewID, String name, String restoName, String reviewText, String datePosted, double rating) {
        this.reviewID = reviewID;
        this.name = name;
        this.restoName = restoName;
        this.reviewText = reviewText;
        this.datePosted = datePosted;
        this.rating = rating;
    }

    // getters
    public String getReviewID() { return reviewID; }
    public String getName() { return name; }
    public String getRestoName() { return restoName; }
    public String getReviewText() { return reviewText; }
    public String getDatePosted() { return datePosted; }
    public double getRating() { return rating; }

    // setters
    public void setReviewID(String reviewID) { this.reviewID = reviewID; }
    public void setName(String name) { this.name = name; }
    public void setRestoName(String restoName) { this.restoName = restoName; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    public void setDatePosted(String datePosted) { this.datePosted = datePosted; }
    public void setRating(int rating) { this.rating = rating; }
}
