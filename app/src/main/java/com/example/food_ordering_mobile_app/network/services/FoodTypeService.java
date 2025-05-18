package com.example.food_ordering_mobile_app.network.services;


import com.example.food_ordering_mobile_app.models.foodType.FoodType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodTypeService {
    @GET("/api/v1/foodType/")
    Call<List<FoodType>> getAllFoodTypes();
}
