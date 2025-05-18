package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.store.Store;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface StoreService {
    @GET("/api/v1/customerStore/")
    Call<ApiResponse<List<Store>>> getAllStore(@QueryMap Map<String, String> options);

    @GET("/api/v1/customerStore/{store_id}")
    Call<ApiResponse<Store>> getStoreInformation(@Path("store_id") String storeId);
}
