package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.cart.Cart;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartService {
    @GET("/api/v1/cart/")
    Call<ApiResponse<List<Cart>>> getUserCart();
    @GET("/api/v1/cart/{storeId}")
    Call<ApiResponse<Cart>> getUserCartInStore(@Path("storeId") String storeId);
    @GET("/api/v1/cart/{cartId}")
    Call<Cart> getDetailCart(@Path("cartId") String cartId);
    @POST("/api/v1/cart/update")
    Call<Cart> updateCart(@Body Map<String, Object> data);
    @DELETE("/api/v1/cart/clear/item/{storeId}")
    Call<ApiResponse<String>> clearCartItem(@Path("storeId") String cartId);
    @DELETE("/api/v1/cart/clear/")
    Call<ApiResponse<String>> clearCart();
    @POST("/api/v1/cart/complete")
    Call<ApiResponse<String>> completeCart(@Body Map<String, Object> data);
    @POST("/api/v1/cart/re-order")
    Call<Cart> reOrder(@Body Map<String, Object> data);
}
