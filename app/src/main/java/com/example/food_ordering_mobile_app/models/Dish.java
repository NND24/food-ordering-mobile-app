package com.example.food_ordering_mobile_app.models;

public class Dish {
    String name;
    String description;
    int price;
    String dishAvatar;

    public Dish(String name, String description, int price, String dishAvatar) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.dishAvatar = dishAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDishAvatar() {
        return dishAvatar;
    }

    public void setDishAvatar(String dishAvatar) {
        this.dishAvatar = dishAvatar;
    }
}
