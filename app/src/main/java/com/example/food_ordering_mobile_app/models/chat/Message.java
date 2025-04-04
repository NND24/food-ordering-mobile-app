package com.example.food_ordering_mobile_app.models.chat;

import com.example.food_ordering_mobile_app.models.user.User;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.Date;

public class Message {
    @SerializedName("_id")
    private String id;
    private User sender;
    private String content;
    private MessageImage image;
    private String chat;
    private Timestamp updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageImage getImage() {
        return image;
    }

    public void setImage(MessageImage image) {
        this.image = image;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
