package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishResponse;
import com.example.food_ordering_mobile_app.models.dish.ListDishResponse;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroupResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DishService {
    @GET("/api/v1/store/{store_id}/dish")
    Call<ListDishResponse> getAllDish(@Path("store_id") String storeId);
    @GET("/api/v1/store/dish/{dish_id}")
    Call<DishResponse> getDish(@Path("dish_id") String dishId);
    @GET("/api/v1/store/dish/{dish_id}/topping")
    Call<ToppingGroupResponse> getToppingFromDish(@Path("dish_id") String dishId);
}
