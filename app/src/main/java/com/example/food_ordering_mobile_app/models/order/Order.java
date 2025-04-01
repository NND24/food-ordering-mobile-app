package com.example.food_ordering_mobile_app.models.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("_id")
    private String id;
    private String customerName;
    private String customerPhonenumber;
    private String note;
    private OrderStore store;
    private List<OrderItem> items;
    private ShipLocation shipLocation;
    private String status;
    private String paymentMethod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhonenumber() {
        return customerPhonenumber;
    }

    public void setCustomerPhonenumber(String customerPhonenumber) {
        this.customerPhonenumber = customerPhonenumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public OrderStore getStore() {
        return store;
    }

    public void setStore(OrderStore store) {
        this.store = store;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public ShipLocation getShipLocation() {
        return shipLocation;
    }

    public void setShipLocation(ShipLocation shipLocation) {
        this.shipLocation = shipLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPhonenumber='" + customerPhonenumber + '\'' +
                ", note='" + note + '\'' +
                ", store=" + store +
                ", items=" + items +
                ", shipLocation=" + shipLocation +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
