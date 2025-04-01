package com.example.food_ordering_mobile_app.models.store;

import com.example.food_ordering_mobile_app.models.foodType.FoodType;

import java.util.List;

public class StoreGroupByCategory {
    private FoodType category;
    private List<Store> stores;

    public FoodType getCategory() {
        return category;
    }

    public void setCategory(FoodType category) {
        this.category = category;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    @Override
    public String toString() {
        return "CategoryWithStores{" +
                "category=" + category +
                ", stores=" + stores +
                '}';
    }
}
