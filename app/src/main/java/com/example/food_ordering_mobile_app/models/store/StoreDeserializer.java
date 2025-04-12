package com.example.food_ordering_mobile_app.models.store;

import com.example.food_ordering_mobile_app.models.Image;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StoreDeserializer implements JsonDeserializer<Store> {

    @Override
    public Store deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Store store = new Store();

        if (json.isJsonPrimitive()) {
            // Nếu chỉ là ID
            store.setId(json.getAsString());
            return store;
        }

        JsonObject obj = json.getAsJsonObject();

        // id
        JsonElement idEl = obj.get("_id");
        if (idEl != null && !idEl.isJsonNull()) {
            store.setId(idEl.getAsString());
        }

        // name
        JsonElement nameEl = obj.get("name");
        if (nameEl != null && !nameEl.isJsonNull()) {
            store.setName(nameEl.getAsString());
        }

        // owner
        JsonElement ownerEl = obj.get("owner");
        if (ownerEl != null && !ownerEl.isJsonNull()) {
            store.setOwner(ownerEl.getAsString());
        }

        // description
        JsonElement descEl = obj.get("description");
        if (descEl != null && !descEl.isJsonNull()) {
            store.setDescription(descEl.getAsString());
        }

        // address
        JsonElement addressEl = obj.get("address");
        if (addressEl != null && !addressEl.isJsonNull()) {
            store.setAddress(context.deserialize(addressEl, StoreAddress.class));
        }

        // storeCategory
        JsonElement storeCatEl = obj.get("storeCategory");
        if (storeCatEl != null && storeCatEl.isJsonArray()) {
            List<FoodType> categories = new ArrayList<>();
            for (JsonElement cat : storeCatEl.getAsJsonArray()) {
                if (cat.isJsonPrimitive()) {
                    // Nếu là chuỗi, tạo FoodType và set name
                    FoodType foodType = new FoodType();
                    foodType.setName(cat.getAsString());
                    categories.add(foodType);
                } else {
                    // Nếu là object, deserialize như bình thường
                    categories.add(context.deserialize(cat, FoodType.class));
                }
            }
            store.setStoreCategory(categories);
        }


        // avatar
        JsonElement avatarEl = obj.get("avatar");
        if (avatarEl != null && !avatarEl.isJsonNull()) {
            store.setAvatar(context.deserialize(avatarEl, Image.class));
        }

        // cover
        JsonElement coverEl = obj.get("cover");
        if (coverEl != null && !coverEl.isJsonNull()) {
            store.setCover(context.deserialize(coverEl, Image.class));
        }

        // paperWork
        JsonElement paperEl = obj.get("paperWork");
        if (paperEl != null && !paperEl.isJsonNull()) {
            store.setPaperWork(context.deserialize(paperEl, PaperWork.class));
        }

        // avgRating
        JsonElement ratingEl = obj.get("avgRating");
        if (ratingEl != null && !ratingEl.isJsonNull()) {
            store.setAvgRating(ratingEl.getAsDouble());
        }

        // amountRating
        JsonElement amtRatingEl = obj.get("amountRating");
        if (amtRatingEl != null && !amtRatingEl.isJsonNull()) {
            store.setAmountRating(amtRatingEl.getAsInt());
        }

        return store;
    }
}
