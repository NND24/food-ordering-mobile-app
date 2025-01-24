package com.example.food_ordering_mobile_app.models;

public class Restaurant {
    private String name;
    private String type;
    private Double rating;
    private int amountOfRating;
    private String description;
    private String restaurantAvatar;

    public Restaurant(String name, String type, Double rating, int amountOfRating, String description, String restaurantAvatar) {
        this.name = name;
        this.type = type;
        this.rating = rating;
        this.amountOfRating = amountOfRating;
        this.description = description;
        this.restaurantAvatar = restaurantAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getAmountOfRating() {
        return amountOfRating;
    }

    public void setAmountOfRating(int amountOfRating) {
        this.amountOfRating = amountOfRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurantAvatar() {
        return restaurantAvatar;
    }

    public void setRestaurantAvatar(String restaurantAvatar) {
        this.restaurantAvatar = restaurantAvatar;
    }
}

