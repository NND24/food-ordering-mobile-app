package com.example.food_ordering_mobile_app.network.services;


import com.example.food_ordering_mobile_app.models.MessageResponse;
import com.example.food_ordering_mobile_app.models.rating.ListRatingResponse;
import com.example.food_ordering_mobile_app.models.rating.Rating;
import com.example.food_ordering_mobile_app.models.rating.RatingDetailResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RatingService {
    @GET("/api/v1/rating/{storeId}")
    Call<ListRatingResponse> getAllStoreRating(@Path("storeId") String storeId, @QueryMap Map<String, String> options);
    @GET("/api/v1/rating/detail/{ratingId}")
    Call<RatingDetailResponse> getDetailRating(@Path("ratingId") String ratingId);
    @POST("/api/v1/rating/{storeId}")
    Call<String> addStoreRating(@Path("storeId") String storeId, @Body Map<String, Object> data);
    @PUT("/api/v1/rating/{ratingId}")
    Call<MessageResponse> editStoreRating(@Path("ratingId") String ratingId, @Body Map<String, Object> data);
    @DELETE("/api/v1/rating/{ratingId}")
    Call<MessageResponse> deleteStoreRating(@Path("ratingId") String ratingId);
}
