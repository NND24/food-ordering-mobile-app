package com.example.food_ordering_mobile_app.models;

public class Restaurant {
    private String name;
    private String description;

    public Restaurant(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

