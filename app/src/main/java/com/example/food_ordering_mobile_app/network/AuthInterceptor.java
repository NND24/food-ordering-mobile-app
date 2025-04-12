package com.example.food_ordering_mobile_app.network;

import android.content.Context;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private final SharedPreferencesHelper sharedPreferencesHelper;

    public AuthInterceptor(Context context) {
        this.sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Lấy accessToken từ SharedPreferences
        String accessToken = sharedPreferencesHelper.getAccessToken();

        // Tạo request mới với Authorization header
        Request.Builder newRequest = originalRequest.newBuilder();

        if (accessToken != null && !accessToken.isEmpty()) {
            newRequest.addHeader("Authorization", "Bearer " + accessToken);
        }

        return chain.proceed(newRequest.build());
    }
}
