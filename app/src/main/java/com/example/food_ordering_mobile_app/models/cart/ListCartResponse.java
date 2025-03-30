package com.example.food_ordering_mobile_app.models.cart;

import java.util.List;

public class ListCartResponse {
    private String status;
    private List<Cart> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Cart> getData() {
        return data;
    }

    public void setData(List<Cart> data) {
        this.data = data;
    }
}
