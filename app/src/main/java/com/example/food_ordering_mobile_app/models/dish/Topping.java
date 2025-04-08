package com.example.food_ordering_mobile_app.models.dish;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Topping implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private Integer price;
    private String toppingGroup;

    public Topping(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getToppingGroup() {
        return toppingGroup;
    }

    public void setToppingGroup(String toppingGroup) {
        this.toppingGroup = toppingGroup;
    }
}
