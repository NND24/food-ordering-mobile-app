package com.example.food_ordering_mobile_app.network.services;


import com.example.food_ordering_mobile_app.models.notification.ListNotificationResponse;
import com.example.food_ordering_mobile_app.models.notification.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {
    @GET("/api/v1/notification/get-all-notifications")
    Call<ListNotificationResponse> getAllNotifications();

    @PUT("/api/v1/notification/update-notification/{id}")
    Call<Notification> updateNotificationStatus(@Path("id") String id);
}
