package com.example.food_ordering_mobile_app.models.chat;

import com.example.food_ordering_mobile_app.models.Image;
import com.example.food_ordering_mobile_app.models.user.User;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class MessageDeserializer implements JsonDeserializer<Message> {

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Message message = new Message();

        // id
        if (jsonObject.has("_id") && !jsonObject.get("_id").isJsonNull()) {
            message.setId(jsonObject.get("_id").getAsString());
        }

        // sender (User object or String)
        if (jsonObject.has("sender") && !jsonObject.get("sender").isJsonNull()) {
            JsonElement senderElement = jsonObject.get("sender");
            if (senderElement.isJsonObject()) {
                User sender = context.deserialize(senderElement, User.class);
                message.setSender(sender);
            } else if (senderElement.isJsonPrimitive()) {
                // Nếu chỉ là chuỗi id, tạo một User với id đó
                String senderId = senderElement.getAsString();
                User sender = new User();
                sender.setId(senderId); // Đảm bảo User class có setId
                message.setSender(sender);
            }
        }

        // content
        if (jsonObject.has("content") && !jsonObject.get("content").isJsonNull()) {
            message.setContent(jsonObject.get("content").getAsString());
        }

        // image
        if (jsonObject.has("image") && !jsonObject.get("image").isJsonNull()) {
            Image image = context.deserialize(jsonObject.get("image"), Image.class);
            message.setImage(image);
        }

        // chat
        if (jsonObject.has("chat") && !jsonObject.get("chat").isJsonNull()) {
            message.setChat(jsonObject.get("chat").getAsString());
        }

        // updatedAt
        if (jsonObject.has("updatedAt") && !jsonObject.get("updatedAt").isJsonNull()) {
            String timestampStr = jsonObject.get("updatedAt").getAsString();
            message.setUpdatedAt(Timestamp.valueOf(timestampStr.replace("T", " ").replace("Z", ""))); // or parse with SimpleDateFormat
        }

        return message;
    }
}