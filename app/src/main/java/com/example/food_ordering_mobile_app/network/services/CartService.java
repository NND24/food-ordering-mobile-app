package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.MessageResponse;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.cart.CartResponse;
import com.example.food_ordering_mobile_app.models.cart.ListCartResponse;
import com.example.food_ordering_mobile_app.models.user.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartService {
    @GET("/api/v1/cart/")
    Call<ListCartResponse> getUserCart();
    @GET("/api/v1/cart/{storeId}")
    Call<CartResponse> getUserCartInStore(@Path("storeId") String storeId);
    @GET("/api/v1/cart/{cartId}")
    Call<Cart> getDetailCart(@Path("cartId") String cartId);
    @POST("/api/v1/cart/update")
    Call<Cart> updateCart(@Body Map<String, Object> data);
    @POST("/api/v1/cart/clear/item/{storeId}")
    Call<MessageResponse> clearCartItem(@Path("storeId") String cartId);
    @POST("/api/v1/cart/clear/")
    Call<MessageResponse> clearCart();
    @POST("/api/v1/cart/complete")
    Call<MessageResponse> completeCart(@Body Map<String, Object> data);
    @POST("/api/v1/cart/re-order")
    Call<Cart> reOrder(@Body Map<String, Object> data);
}
