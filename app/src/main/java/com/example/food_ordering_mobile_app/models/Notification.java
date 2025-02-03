package com.example.food_ordering_mobile_app.models;

public class Notification {
    private String noti;
    private String time;
    private Boolean isRead;

    public Notification(String noti, String time, Boolean isRead) {
        this.noti = noti;
        this.time = time;
        this.isRead = isRead;
    }

    public String getNoti() {
        return noti;
    }

    public void setNoti(String noti) {
        this.noti = noti;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        isRead = isRead;
    }
}
