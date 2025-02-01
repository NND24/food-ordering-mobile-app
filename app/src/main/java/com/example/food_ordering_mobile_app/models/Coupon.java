package com.example.food_ordering_mobile_app.models;

public class Coupon {
    private String name;
    private String description;
    private String couponImage;

    public Coupon(String name, String description, String couponImage) {
        this.name = name;
        this.description = description;
        this.couponImage = couponImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouponImage() {
        return couponImage;
    }

    public void setCouponImage(String couponImage) {
        this.couponImage = couponImage;
    }
}
