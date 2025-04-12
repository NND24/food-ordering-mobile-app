package com.example.food_ordering_mobile_app.models.favorite;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Favorite implements Serializable {
    @SerializedName("_id")
    private String id;
    private String user;
    private List<Store> store;

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

    public List<Store> getStore() {
        return store;
    }

    public void setStore(List<Store> store) {
        this.store = store;
    }
}
