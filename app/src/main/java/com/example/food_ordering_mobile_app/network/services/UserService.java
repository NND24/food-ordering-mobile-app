package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.user.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("/api/v1/user/{id}")
    Call<User> getCurrentUser(@Path("id") String userId);

    @PUT("/api/v1/user/")
    Call<User> updateUser(@Body User user);
}
