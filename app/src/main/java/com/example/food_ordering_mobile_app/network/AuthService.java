package com.example.food_ordering_mobile_app.network;

import com.example.food_ordering_mobile_app.models.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthService {
    @POST("/api/v1/auth/register")
    Call<User> register(@Body User user);
    @POST("/api/v1/auth/login")
    Call<User> login(@Body User user);
    @POST("/api/v1/auth/login/google/mobile")
    Call<User> loginWithGoogle(@Body Map<String, String> data);
    @POST("/api/v1/auth/forgot-password")
    Call<String> forgotPassword(@Body Map<String, String> data);
    @POST("/api/v1/auth/check-otp")
    Call<String> checkOTP(@Body Map<String, String> data);
    @POST("/api/v1/auth/logout")
    Call<String> logout();

    @GET("/api/v1/auth/refresh")
    Call<User> refreshToken();

   @PUT("/api/v1/auth/change-password")
    Call<String> changePassword(@Body Map<String, String> data);
    @PUT("/api/v1/auth/reset-password")
    Call<String> resetPassword(@Body Map<String, String> data);
}
