package com.example.food_ordering_mobile_app.models.cart;

public class CartResponse {
    private String success;
    private Cart data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String status) {
        this.success = status;
    }

    public Cart getData() {
        return data;
    }

    public void setData(Cart data) {
        this.data = data;
    }
}
