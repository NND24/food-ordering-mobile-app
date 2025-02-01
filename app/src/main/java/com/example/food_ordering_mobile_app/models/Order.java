package com.example.food_ordering_mobile_app.models;

import java.util.List;

public class Order {
    private String name;
    private int quantity;
    private int price;
    private String address;
    private String restaurantAvatar;
    private List<String> sideDish;

    public Order(String name, int quantity, String address, String restaurantAvatar) {
        this.name = name;
        this.quantity = quantity;
        this.address = address;
        this.restaurantAvatar = restaurantAvatar;
    }

    public Order(String name, int quantity, int price, List<String> sideDish) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.sideDish = sideDish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getSideDish() {
        return sideDish;
    }

    public void setSideDish(List<String> sideDish) {
        this.sideDish = sideDish;
    }
}

