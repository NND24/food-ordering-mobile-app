package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.ui.customer.MainCustomerActivity;

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
                String refreshToken = sharedPreferencesHelper.getRefreshToken();

                if (refreshToken != null && userId != null && accessToken != null) {
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
}
