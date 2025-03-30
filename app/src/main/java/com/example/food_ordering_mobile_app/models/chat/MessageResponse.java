package com.example.food_ordering_mobile_app.models.chat;

import java.util.List;

public class MessageResponse {
    private Chat chat;
    private List<Message> messages;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "chat=" + chat +
                ", messages=" + messages +
                '}';
    }
}
