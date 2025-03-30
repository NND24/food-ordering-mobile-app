package com.example.food_ordering_mobile_app.models.dish;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.google.gson.annotations.SerializedName;

public class FoodCategory {
    @SerializedName("_id")
    private String id;
    private String name;
    private Store store;
    private Dish[] dish;

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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Dish[] getDish() {
        return dish;
    }

    public void setDish(Dish[] dish) {
        this.dish = dish;
    }
}
