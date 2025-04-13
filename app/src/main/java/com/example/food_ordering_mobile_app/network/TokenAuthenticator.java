package com.example.food_ordering_mobile_app.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.network.services.AuthService;
import com.example.food_ordering_mobile_app.ui.common.LoginActivity;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokenAuthenticator implements Authenticator {
    private final Context context;
    private final SharedPreferencesHelper sharedPreferencesHelper;
    private final AuthService authService;

    public TokenAuthenticator(Context context) {
        this.context = context;
        this.sharedPreferencesHelper = new SharedPreferencesHelper(context);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.57:5000/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.authService = retrofit.create(AuthService.class);
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {
        // Nếu đã retry mà vẫn 401 thì không retry nữa
        if (responseCount(response) >= 2) {
            return null;
        }

        String refreshToken = sharedPreferencesHelper.getRefreshToken(); // Lấy refresh token
        Log.d("TokenAuthenticator", "refreshToken: " + refreshToken);

        if (refreshToken == null || refreshToken.isEmpty()) {
            sharedPreferencesHelper.clearAll(); // Xoá hết access/refresh token

            // Chuyển sang LoginActivity (bắt buộc phải chạy trong main thread)
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xoá toàn bộ backstack
            context.startActivity(intent);

            return null;
        }

        retrofit2.Response<User> tokenResponse = authService.refreshTokenMobile(refreshToken).execute();
        Log.d("TokenAuthenticator", "Refresh Token Response: " + tokenResponse);

        if (tokenResponse.isSuccessful() && tokenResponse.body() != null) {
            String newAccessToken = tokenResponse.body().getAccessToken();
            sharedPreferencesHelper.saveAccessToken(newAccessToken); // Lưu lại accessToken
            Log.d("TokenAuthenticator", "New Access Token: " + newAccessToken);
            // Retry request với accessToken mới
            return response.request().newBuilder()
                    .header("Authorization", "Bearer " + newAccessToken)
                    .build();
        } else {
            sharedPreferencesHelper.clearAll(); // Xoá hết access/refresh token

            // Chuyển sang LoginActivity (bắt buộc phải chạy trong main thread)
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xoá toàn bộ backstack
            context.startActivity(intent);

            return null;
        }
    }

    private int responseCount(Response response) {
        int count = 1;
        while ((response = response.priorResponse()) != null) {
            count++;
        }
        return count;
    }
}
