package com.example.food_ordering_mobile_app.models;

public class Message {
    private String name;
    private String message;
    private String createdTime;
    private String avatar;
    private boolean isSender;

    public Message(String name, String message, String createdTime, String avatar, boolean isSender) {
        this.name = name;
        this.message = message;
        this.createdTime = createdTime;
        this.avatar = avatar;
        this.isSender = isSender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }
}

