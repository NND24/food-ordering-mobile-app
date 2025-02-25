package com.example.food_ordering_mobile_app.models;

public class OrderDetail {
    private int orderDetailId;
    private String dishName;
    private int quantity;
    private long unitPrice;
    //    there will be a lot more stat


    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }
    public OrderDetail(int orderDetailId, String dishName, int quantity, long unitPrice) {
        this.orderDetailId = orderDetailId;
        this.dishName = dishName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
