package com.example.food_ordering_mobile_app.models.dish;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ToppingGroup implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private String store;
    private List<Topping> toppings;

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

    public List<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }
}
