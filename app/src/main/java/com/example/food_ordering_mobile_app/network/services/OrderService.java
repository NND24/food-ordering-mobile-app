package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.order.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderService {
    @GET("/api/v1/order/")
    Call<ApiResponse<List<Order>>> getUserOrder();
    @GET("/api/v1/order/{id}")
    Call<ApiResponse<Order>> getOrderDetail(@Path("id") String orderId);

    @PUT("/api/v1/order/{id}/cancel-order")
    Call<ApiResponse<String>> cancelOrder(@Path("id") String orderId);
}
