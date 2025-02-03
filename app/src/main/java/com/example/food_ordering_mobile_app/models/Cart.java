package com.example.food_ordering_mobile_app.models;

public class Cart extends Restaurant {
    private int quantity;

    public Cart(String name, String type, Double rating, int amountOfRating, String description, String restaurantAvatar, int quantity) {
        super(name, type, rating, amountOfRating, description, restaurantAvatar);
        this.quantity = quantity;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

