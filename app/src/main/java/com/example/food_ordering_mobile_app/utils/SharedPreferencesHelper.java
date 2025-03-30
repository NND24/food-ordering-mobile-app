package com.example.food_ordering_mobile_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;


public class SharedPreferencesHelper {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_INFO = "user_info";
    private static final String KEY_FOOD_TYPE_LIST = "food_type_list";
    private static final String KEY_STORE_LIST = "store_list";


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

    public void saveFoodTypes(List<FoodType> foodTypes) {
        String json = gson.toJson(foodTypes);
        editor.putString(KEY_FOOD_TYPE_LIST, json).apply();
    }

    public List<FoodType> getSavedFoodTypes() {
        String json = sharedPreferences.getString(KEY_FOOD_TYPE_LIST, null);
        if (json != null) {
            Type type = new TypeToken<List<FoodType>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }


    public void saveStores(String key, List<Store> stores) {
        Gson gson = new Gson();
        String json = gson.toJson(stores);
        sharedPreferences.edit().putString(key, json).apply();
    }

    public List<Store> getSavedStores(String key) {
        String json = sharedPreferences.getString(key, null);
        if (json != null) {
            Type type = new TypeToken<List<Store>>() {}.getType();
            return new Gson().fromJson(json, type);
        }
        return new ArrayList<>();
    }


    public void clearUserData() {
        editor.clear();
        editor.apply();
    }
}
