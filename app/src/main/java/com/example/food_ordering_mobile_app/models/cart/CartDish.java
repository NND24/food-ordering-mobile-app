package com.example.food_ordering_mobile_app.models.cart;

import com.example.food_ordering_mobile_app.models.dish.DishImage;
import com.example.food_ordering_mobile_app.models.dish.FoodCategory;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroup;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CartDish implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private Integer price;
    private String category;
    private String store;
    private DishImage image;
    private List<String> toppingGroups;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public DishImage getImage() {
        return image;
    }

    public void setImage(DishImage image) {
        this.image = image;
    }

    public List<String> getToppingGroups() {
        return toppingGroups;
    }

    public void setToppingGroups(List<String> toppingGroups) {
        this.toppingGroups = toppingGroups;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
