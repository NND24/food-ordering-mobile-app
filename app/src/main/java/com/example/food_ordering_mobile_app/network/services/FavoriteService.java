package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.MessageResponse;
import com.example.food_ordering_mobile_app.models.favorite.Favorite;
import com.example.food_ordering_mobile_app.models.favorite.FavoriteResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteService {
    @GET("/api/v1/favorite/")
    Call<FavoriteResponse> getUserFavorite();
    @POST("/api/v1/favorite/add/{id}")
    Call<MessageResponse> addFavorite(@Path("id") String storeId);
    @DELETE("/api/v1/favorite/remove/{id}")
    Call<MessageResponse> removeFavorite(@Path("id") String id);
    @DELETE("/api/v1/favorite/remove-all")
    Call<MessageResponse> removeAllFavorite();
}
