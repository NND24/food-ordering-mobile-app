package com.example.food_ordering_mobile_app.models;

public class Order {
    private String name;
    private int amountOfDish;
    private String address;
    private String restaurantAvatar;

    public Order(String name, int amountOfDish, String address, String restaurantAvatar) {
        this.name = name;
        this.amountOfDish = amountOfDish;
        this.address = address;
        this.restaurantAvatar = restaurantAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmountOfDish() {
        return amountOfDish;
    }

    public void setAmountOfDish(int amountOfDish) {
        this.amountOfDish = amountOfDish;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRestaurantAvatar() {
        return restaurantAvatar;
    }

    public void setRestaurantAvatar(String restaurantAvatar) {
        this.restaurantAvatar = restaurantAvatar;
    }
}

