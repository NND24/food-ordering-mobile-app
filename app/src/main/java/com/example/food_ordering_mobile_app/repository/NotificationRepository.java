package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.notification.ListNotificationResponse;
import com.example.food_ordering_mobile_app.models.notification.Notification;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.NotificationService;
import com.example.food_ordering_mobile_app.utils.PersistentCookieStore;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {
    private NotificationService notificationService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public NotificationRepository(Context context) {
        notificationService = RetrofitClient.getClient(context).create(NotificationService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);

        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();

        if (!(cookieManager.getCookieStore() instanceof PersistentCookieStore)) {
            Log.e("NotificationRepository", "CookieStore chưa đúng, thiết lập lại...");
            cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);
        } else {
            Log.d("NotificationRepository", "PersistentCookieStore đã được thiết lập!");
        }
    }

    public LiveData<Resource<ListNotificationResponse>> getAllNotifications() {
        MutableLiveData<Resource<ListNotificationResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        notificationService.getAllNotifications().enqueue(new Callback<ListNotificationResponse>() {
            @Override
            public void onResponse(Call<ListNotificationResponse> call, Response<ListNotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("NotificationRepository", "getAllNotifications: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ListNotificationResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<ListNotificationResponse>> updateNotificationStatus(String id) {
        MutableLiveData<Resource<ListNotificationResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        notificationService.updateNotificationStatus(id).enqueue(new Callback<ListNotificationResponse>() {
            @Override
            public void onResponse(Call<ListNotificationResponse> call, Response<ListNotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("NotificationRepository", "updateNotificationStatus: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ListNotificationResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
