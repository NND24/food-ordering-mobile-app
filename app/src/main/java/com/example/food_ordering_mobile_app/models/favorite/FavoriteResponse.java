package com.example.food_ordering_mobile_app.models.favorite;

import com.example.food_ordering_mobile_app.models.store.Store;

import java.util.List;

public class FavoriteResponse {
    private String status;
    private Favorite data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Favorite getData() {
        return data;
    }

    public void setData(Favorite data) {
        this.data = data;
    }
}
