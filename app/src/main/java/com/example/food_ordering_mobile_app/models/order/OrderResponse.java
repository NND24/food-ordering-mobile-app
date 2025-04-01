package com.example.food_ordering_mobile_app.models.order;

public class OrderResponse {
    private String success;
    private Order data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String status) {
        this.success = status;
    }

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }
}
