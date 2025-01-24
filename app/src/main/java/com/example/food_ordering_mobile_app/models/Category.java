package com.example.food_ordering_mobile_app.models;

public class Category {
    private String imageUrl;
    private String name;

    public Category(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}