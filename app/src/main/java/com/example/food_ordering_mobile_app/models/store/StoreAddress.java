package com.example.food_ordering_mobile_app.models.store;

import java.io.Serializable;

public class StoreAddress implements Serializable {
    private String full_name;
    private Double lat;
    private Double lon;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "StoreAddress{" +
                "full_name='" + full_name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
