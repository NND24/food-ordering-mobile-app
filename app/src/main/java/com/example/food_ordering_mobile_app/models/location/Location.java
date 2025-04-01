package com.example.food_ordering_mobile_app.models.location;

import com.example.food_ordering_mobile_app.models.user.User;
import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("_id")
    private String id;
    private String user;
    private String name;
    private String address;
    private Double lat;
    private Double lon;
    private String detailAddress;
    private String contactName;
    private String note;
    private String contactPhonenumber;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContactPhonenumber() {
        return contactPhonenumber;
    }

    public void setContactPhonenumber(String contactPhonenumber) {
        this.contactPhonenumber = contactPhonenumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", detailAddress='" + detailAddress + '\'' +
                ", contactName='" + contactName + '\'' +
                ", note='" + note + '\'' +
                ", contactPhonenumber='" + contactPhonenumber + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
