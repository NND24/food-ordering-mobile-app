package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.ListOrderResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderService {
    @GET("/api/v1/order/")
    Call<ListOrderResponse> getUserOrder();
    @GET("/api/v1/order/{id}")
    Call<Order> getOrderDetail(@Path("id") String orderId);

}
