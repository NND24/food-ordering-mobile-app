package com.example.food_ordering_mobile_app.models.rating;

import java.util.List;

public class ListRatingResponse {
    private String success;
    private List<Rating> data;

    public String getSuccess() {
        return success;
    }

    public List<Rating> getData() {
        return data;
    }
}

