package com.example.food_ordering_mobile_app.models;

public class Review {
    private String customerName;
    private String customerAvatar;
    private int numberOfCustomerReview;
    private Double rating;
    private String review;
    private String reviewTime;
    private String answer;
    private String answerTime;
    private String orderFood;
    private String reviewImage;


    public Review(String customerName, Double rating, String review) {
        this.customerName = customerName;
        this.rating = rating;
        this.review = review;
    }

    public Review(String customerName, int numberOfCustomerReview, String customerAvatar, Double rating, String review, String reviewTime, String answer, String answerTime, String orderFood, String reviewImage) {
        this.customerName = customerName;
        this.numberOfCustomerReview = numberOfCustomerReview;
        this.customerAvatar = customerAvatar;
        this.rating = rating;
        this.review = review;
        this.reviewTime = reviewTime;
        this.answer = answer;
        this.answerTime = answerTime;
        this.orderFood = orderFood;
        this.reviewImage = reviewImage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAvatar() {
        return customerAvatar;
    }

    public void setCustomerAvatar(String customerAvatar) {
        this.customerAvatar = customerAvatar;
    }

    public int getNumberOfCustomerReview() {
        return numberOfCustomerReview;
    }

    public void setNumberOfCustomerReview(int numberOfCustomerReview) {
        this.numberOfCustomerReview = numberOfCustomerReview;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    public String getOrderFood() {
        return orderFood;
    }

    public void setOrderFood(String orderFood) {
        this.orderFood = orderFood;
    }

    public String getReviewImage() {
        return reviewImage;
    }

    public void setReviewImage(String reviewImage) {
        this.reviewImage = reviewImage;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
