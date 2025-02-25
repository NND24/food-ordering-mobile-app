package com.example.food_ordering_mobile_app.models;

import java.util.List;

public class OrderDateGroup {
    private String date;
    private List<StoreOrder> orders;

    public OrderDateGroup(String date, List<StoreOrder> orders) {
        this.date = date;
        this.orders = orders;
    }

    public String getDate() { return date; }
    public List<StoreOrder> getOrders() { return orders; }
}
