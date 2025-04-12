package com.example.food_ordering_mobile_app.models.dish;

import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.Store;

import java.io.Serializable;
import java.util.List;

public class DishGroupByCategory implements Serializable {
    private FoodCategory category;
    private List<Dish> dishes;

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
