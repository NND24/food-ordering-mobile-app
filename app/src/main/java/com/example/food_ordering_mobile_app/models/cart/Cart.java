package com.example.food_ordering_mobile_app.models.cart;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    @SerializedName("_id")
    private String id;
    private String user;
    private Store store;
    private List<CartItem> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", store=" + store +
                ", items=" + items +
                '}';
    }
}

