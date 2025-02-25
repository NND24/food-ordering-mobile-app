package com.example.food_ordering_mobile_app.models;

import java.util.List;

public class OrderDateStatusGroup {
    private String date;
    private String status;
    private List<StoreOrder> orders;

    public OrderDateStatusGroup(String date, String status, List<StoreOrder> orders) {
        this.date = date;
        this.orders = orders;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    public String getDate() { return date; }
    public List<StoreOrder> getOrders() { return orders; }
}
