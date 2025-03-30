package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.dish.Dish;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DishService {
    @GET("/api/v1/store/{storeId}/dish")
    Call<List<Dish>> getAllDish(@Path("storeId") String storeId);
    @GET("/api/v1/store/dish/{dishId}")
    Call<Dish> getDish(@Path("dishId") String dishId);
    @GET("/api/v1/store/dish/{dishId}/topping")
    Call<Dish> getToppingFromDish(@Path("dishId") String dishId);
}
