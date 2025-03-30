package com.example.food_ordering_mobile_app.models.order;

import java.util.List;

public class ListOrderResponse {
    private String status;
    private List<Order> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
