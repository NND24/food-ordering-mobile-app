package com.example.food_ordering_mobile_app.models.dish;

import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.Store;

import java.util.List;

public class DishGroupByCategory {
    private FoodCategory category;
    private List<DishStore> dishes;

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public List<DishStore> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishStore> dishes) {
        this.dishes = dishes;
    }
}
