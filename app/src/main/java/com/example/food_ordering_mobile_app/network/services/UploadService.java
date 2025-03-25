package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("/api/v1/upload/avatar")
    Call<ResponseBody> uploadAvatar(@Part MultipartBody.Part file);
}
