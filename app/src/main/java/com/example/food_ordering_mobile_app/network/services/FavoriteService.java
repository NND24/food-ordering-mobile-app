package com.example.food_ordering_mobile_app.network.services;

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
    @POST("/api/v1/favorite/add")
    Call<Favorite> addFavorite(@Body Favorite favorite);
    @DELETE("/api/v1/favorite/remove/{id}")
    Call<Favorite> removeFavorite(@Path("id") String id);
    @DELETE("/api/v1/favorite/remove-all")
    Call<Favorite> removeAllFavorite();
}
