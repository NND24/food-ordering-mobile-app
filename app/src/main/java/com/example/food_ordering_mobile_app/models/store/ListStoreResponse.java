package com.example.food_ordering_mobile_app.models.store;

import java.util.List;

public class ListStoreResponse {
    private String status;
    private List<Store> data;

    public String getStatus() {
        return status;
    }

    public List<Store> getData() {
        return data;
    }
}

