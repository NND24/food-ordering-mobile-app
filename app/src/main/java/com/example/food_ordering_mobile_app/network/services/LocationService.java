package com.example.food_ordering_mobile_app.network.services;


import com.example.food_ordering_mobile_app.models.location.Location;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LocationService {
    @POST("/api/v1/location/add-location")
    Call<Location> addLocation(@Body Location location);
    @GET("/api/v1/location/get-location/{id}")
    Call<Location> getLocation(@Path("id") String id);
    @GET("/api/v1/location/get-user-locations")
    Call<Location> getUserLocations();
    @PUT("/api/v1/location/update-location/{id}")
    Call<Location> updateLocation(@Path("id") String id);
    @DELETE("/api/v1/location/delete-location/{id}")
    Call<Location> deleteLocation(@Path("id") String id);
}
