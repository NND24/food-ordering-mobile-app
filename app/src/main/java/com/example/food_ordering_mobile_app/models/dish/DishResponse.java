package com.example.food_ordering_mobile_app.models.dish;

public class DishResponse {
    private String success;
    private DishDetail data;

    public String getSuccess() {
        return success;
    }

    public DishDetail getData() {
        return data;
    }

    @Override
    public String toString() {
        return "DishResponse{" +
                "success='" + success + '\'' +
                ", data=" + data +
                '}';
    }
}

