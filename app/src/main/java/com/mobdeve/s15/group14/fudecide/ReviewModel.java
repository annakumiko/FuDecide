package com.mobdeve.s15.group14.fudecide;

public class ReviewModel {
    private String userName, restoName, reviewText, datePosted;
    private double rating;

    public ReviewModel() { }

    public ReviewModel(String userName, String restoName, String reviewText, String datePosted, double rating) {
        this.userName = userName;
        this.restoName = restoName;
        this.reviewText = reviewText;
        this.datePosted = datePosted;
        this.rating = rating;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getRestoName() { return restoName; }

    public void setRestoName(String restoName) { this.restoName = restoName; }

    public String getReviewText() { return reviewText; }

    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public String getDatePosted() { return datePosted; }

    public void setDatePosted(String datePosted) { this.datePosted = datePosted; }

    public double getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }
}
