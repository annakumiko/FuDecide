package com.mobdeve.s15.group14.fudecide;

public class ReviewModel {
    private String name, restoName, reviewText, datePosted;
    private double rating;

    public ReviewModel() { }

    public ReviewModel(String name, String restoName, String reviewText, String datePosted, double rating) {
        this.name = name;
        this.restoName = restoName;
        this.reviewText = reviewText;
        this.datePosted = datePosted;
        this.rating = rating;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getRestoName() { return restoName; }

    public void setRestoName(String restoName) { this.restoName = restoName; }

    public String getReviewText() { return reviewText; }

    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public String getDatePosted() { return datePosted; }

    public void setDatePosted(String datePosted) { this.datePosted = datePosted; }

    public double getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }
}