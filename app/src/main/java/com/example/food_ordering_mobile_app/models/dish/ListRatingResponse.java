package com.example.food_ordering_mobile_app.models.dish;

import java.util.List;

public class ListRatingResponse {
    private String status;
    private List<Rating> data;

    public String getStatus() {
        return status;
    }

    public List<Rating> getData() {
        return data;
    }
}

