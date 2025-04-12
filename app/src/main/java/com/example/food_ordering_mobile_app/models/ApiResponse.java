package com.example.food_ordering_mobile_app.models;

import java.io.Serializable;

public class ApiResponse<T> implements Serializable {
    private String success;
    private T data;

    public String getSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success='" + success + '\'' +
                ", data=" + data +
                '}';
    }
}
