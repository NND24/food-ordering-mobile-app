package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.utils.PersistentCookieStore;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.ui.customer.MainCustomerActivity;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        boolean isFirstLaunch = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getBoolean(KEY_FIRST_LAUNCH, true);

        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(this), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        CookieStore cookieStore = cookieManager.getCookieStore();
        List<HttpCookie> cookies = cookieStore.getCookies();

        new Handler().postDelayed(() -> {
            if (isFirstLaunch) {
                // First-time app launch -> Navigate to IntroduceActivity
                startActivity(new Intent(SplashActivity.this, IntroduceActivity.class));

                // Save the flag to indicate the app has been launched before
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putBoolean(KEY_FIRST_LAUNCH, false)
                        .apply();
            } else {
                // Check if the user is logged in
                String userId = sharedPreferencesHelper.getUserId();
                String accessToken = sharedPreferencesHelper.getAccessToken();

                if (hasRefreshToken() && userId != null && accessToken != null) {
                    // If logged in -> Navigate to MainCustomerActivity
                    startActivity(new Intent(SplashActivity.this, MainCustomerActivity.class));
                } else {
                    // If not logged in -> Navigate to LoginActivity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
            finish();
        }, 2000);
    }

    private boolean hasRefreshToken() {
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(this), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        if (cookieManager != null) {
            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
            for (HttpCookie cookie : cookies) {
                Log.d("Cookie Splash Activity", "Cookie Name: " + cookie.getName() + ", Value: " + cookie.getValue());
                if (cookie.getName().equals("refreshToken")) {
                    return true;
                }
            }
        }
        return false;
    }
}
