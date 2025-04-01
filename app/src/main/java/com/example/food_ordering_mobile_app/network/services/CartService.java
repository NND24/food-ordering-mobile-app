package com.example.food_ordering_mobile_app.network.services;

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
    Call<Cart> updateCart(@Body Cart cart);
    @POST("/api/v1/cart/clear/item/{storeId}")
    Call<Cart> clearCartItem(@Path("storeId") String cartId);
    @POST("/api/v1/cart/clear/")
    Call<Cart> clearCart();
    @POST("/api/v1/cart/complete")
    Call<Cart> completeCart(@Body Cart cart);
    @POST("/api/v1/cart/re-order")
    Call<Cart> reOrder(@Body Cart cart);
}
