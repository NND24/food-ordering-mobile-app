package com.example.food_ordering_mobile_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.food_ordering_mobile_app.models.User;
import com.google.gson.Gson;

public class SharedPreferencesHelper {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_INFO = "user_info";

    private static SharedPreferencesHelper instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson(); // Initialize Gson

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context.getApplicationContext());
        }
        return instance;
    }

    public void saveUserData(String accessToken, String userId) {
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public void saveCurrentUser(User user) {
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_INFO, userJson);
        editor.apply();
    }

    public User getCurrentUser() {
        String userJson = sharedPreferences.getString(KEY_USER_INFO, null);
        return userJson != null ? gson.fromJson(userJson, User.class) : null;
    }

    public void clearUserData() {
        editor.clear();
        editor.apply();
    }
}
