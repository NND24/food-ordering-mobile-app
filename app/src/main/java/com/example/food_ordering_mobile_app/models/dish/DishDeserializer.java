package com.example.food_ordering_mobile_app.models.dish;

import com.example.food_ordering_mobile_app.models.Image;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DishDeserializer implements JsonDeserializer<Dish> {
    @Override
    public Dish deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Dish dish = new Dish();

        // _id
        JsonElement idEl = obj.get("_id");
        if (idEl != null && !idEl.isJsonNull()) {
            dish.setId(idEl.getAsString());
        }

        // name
        JsonElement nameEl = obj.get("name");
        if (nameEl != null && !nameEl.isJsonNull()) {
            dish.setName(nameEl.getAsString());
        }

        // price
        JsonElement priceEl = obj.get("price");
        if (priceEl != null && !priceEl.isJsonNull()) {
            dish.setPrice(priceEl.getAsInt());
        }

        // description
        JsonElement descEl = obj.get("description");
        if (descEl != null && !descEl.isJsonNull()) {
            dish.setDescription(descEl.getAsString());
        }

        // category
        JsonElement categoryEl = obj.get("category");
        if (categoryEl != null && !categoryEl.isJsonNull()) {
            if (categoryEl.isJsonPrimitive()) {
                FoodCategory cat = new FoodCategory();
                cat.setName(categoryEl.getAsString());
                dish.setCategory(cat);
            } else {
                dish.setCategory(context.deserialize(categoryEl, FoodCategory.class));
            }
        }

        // store
        JsonElement storeEl = obj.get("store");
        if (storeEl != null && !storeEl.isJsonNull()) {
            if (storeEl.isJsonPrimitive()) {
                Store store = new Store();
                store.setId(storeEl.getAsString());
                dish.setStore(store);
            } else {
                dish.setStore(context.deserialize(storeEl, Store.class));
            }
        }

        // image
        JsonElement imageEl = obj.get("image");
        if (imageEl != null && !imageEl.isJsonNull()) {
            dish.setImage(context.deserialize(imageEl, Image.class));
        }

        // toppingGroups
        JsonElement toppingGroupsEl = obj.get("toppingGroups");
        if (toppingGroupsEl != null && toppingGroupsEl.isJsonArray()) {
            JsonArray toppingArray = toppingGroupsEl.getAsJsonArray();
            List<ToppingGroup> toppings = new ArrayList<>();
            for (JsonElement topping : toppingArray) {
                if (topping.isJsonPrimitive()) {
                    ToppingGroup tg = new ToppingGroup();
                    tg.setId(topping.getAsString());
                    toppings.add(tg);
                } else {
                    toppings.add(context.deserialize(topping, ToppingGroup.class));
                }
            }
            dish.setToppingGroups(toppings);
        }

        return dish;
    }
}
