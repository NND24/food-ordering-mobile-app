package com.example.food_ordering_mobile_app.models.notification;

import java.util.List;

public class ListNotificationResponse {
    private String status;
    private List<Notification> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Notification> getData() {
        return data;
    }

    public void setData(List<Notification> data) {
        this.data = data;
    }
}
