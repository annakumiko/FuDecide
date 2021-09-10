package com.mobdeve.s15.group14.fudecide;

public class ReviewsModel {
    private String reviewText;
    private int rating;
    private String datePosted;

    public ReviewsModel(String reviewText, int rating, String datePosted) {
        this.reviewText = reviewText;
        this.rating = rating;
        this.datePosted = datePosted;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }
}
