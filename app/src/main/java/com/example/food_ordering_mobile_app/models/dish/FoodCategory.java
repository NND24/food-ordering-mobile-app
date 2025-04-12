package com.example.food_ordering_mobile_app.models.dish;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodCategory implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private String store;
    private String dish;

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

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }
}
