package com.example.food_ordering_mobile_app.models.dish;

import java.util.List;

public class ListDishResponse {
    private String success;
    private List<DishStore> data;

    public String getSuccess() {
        return success;
    }

    public List<DishStore> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ListDishResponse{" +
                "status='" + success + '\'' +
                ", data=" + data +
                '}';
    }
}

