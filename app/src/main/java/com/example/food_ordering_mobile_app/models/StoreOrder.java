package com.example.food_ordering_mobile_app.models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class StoreOrder {
    private int id;
    private int numberOrder;
    private List<OrderDetail> orderItems;
    private String customerName;
    private String orderTime;
    private String pickUpTime;      

    private double distance;

    private String  orderDate;

    private String  pickupDate;

    public StoreOrder(int id, int numberOrder, List<OrderDetail> orderItems, String customerName, String orderTime, String pickUpTime, double distance, String orderDate, String pickupDate) {
        this.id = id;
        this.numberOrder = numberOrder;
        this.orderItems = orderItems;
        this.customerName = customerName;
        this.orderTime = orderTime;
        this.pickUpTime = pickUpTime;
        this.distance = distance;
        this.orderDate = orderDate;
        this.pickupDate = pickupDate;
    }

    public StoreOrder(int id, int numberOrder, List<OrderDetail> orderItems, String customerName, String orderTime, String pickUpTime) {
        this.id = id;
        this.numberOrder = numberOrder;
        this.orderItems = orderItems;
        this.customerName = customerName;
        this.orderTime = orderTime;
        this.pickUpTime = pickUpTime;
    }

    public StoreOrder(int id, String customerName, List<OrderDetail> orderItems, String orderTime, double distance) {
        this.customerName = customerName;
        this.id = id;
        this.orderTime = orderTime;
        this.distance = distance;
        this.orderItems = orderItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(int numberOrder) {
        this.numberOrder = numberOrder;
    }

    public List<OrderDetail> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderDetail> orderItems) {
        this.orderItems = orderItems;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getOrderDetail(){
        StringBuilder result = new StringBuilder();
        for (OrderDetail order: orderItems){
            result.append(order.getQuantity()).append(" x ");
            result.append(order.getDishName());
            result.append("\n");
        }
        return result.toString();
    }

    public int getTotalItems(){
        int result = 0;
        for (OrderDetail order: orderItems){
            result += order.getQuantity();
        }
        return result;
    }

    public long getTotalPrice(){
        long result = 0;
        for (OrderDetail order: orderItems){
            result += order.getUnitPrice() * order.getQuantity();
        }
        return result;
    }

    public String getOrderSummary(){
        StringBuilder result = new StringBuilder();
        result.append(getTotalItems()).append(" Món").append(" / ").append(getTotalPrice()).append("₫");
        return result.toString();
    }
}
