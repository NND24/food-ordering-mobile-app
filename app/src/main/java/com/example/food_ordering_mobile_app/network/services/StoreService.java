package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.store.ListStoreResponse;
import com.example.food_ordering_mobile_app.models.store.StoreResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface StoreService {
    @GET("/api/v1/store/")
    Call<ListStoreResponse> getAllStore(@QueryMap Map<String, String> options);

    @GET("/api/v1/store/{store_id}")
    Call<StoreResponse> getStoreInformation(@Path("store_id") String storeId);
}
