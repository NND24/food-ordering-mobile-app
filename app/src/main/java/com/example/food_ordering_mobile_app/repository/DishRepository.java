package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroup;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.DishService;
import com.example.food_ordering_mobile_app.utils.PersistentCookieStore;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishRepository {
    private DishService dishService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public DishRepository(Context context) {
        dishService = RetrofitClient.getClient(context).create(DishService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);

        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();

        if (!(cookieManager.getCookieStore() instanceof PersistentCookieStore)) {
            cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);
        }
    }

    public LiveData<Resource<ApiResponse<List<Dish>>>> getAllDish(String storeId) {
        MutableLiveData<Resource<ApiResponse<List<Dish>>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        dishService.getAllDish(storeId).enqueue(new Callback<ApiResponse<List<Dish>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Dish>>> call, Response<ApiResponse<List<Dish>>> response) {
                if (response.isSuccessful() && response.body() != null) {
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
            public void onFailure(Call<ApiResponse<List<Dish>>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<ApiResponse<Dish>>> getDish(String dishId) {
        MutableLiveData<Resource<ApiResponse<Dish>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        dishService.getDish(dishId).enqueue(new Callback<ApiResponse<Dish>>() {
            @Override
            public void onResponse(Call<ApiResponse<Dish>> call, Response<ApiResponse<Dish>> response) {
                if (response.isSuccessful() && response.body() != null) {
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
            public void onFailure(Call<ApiResponse<Dish>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<ApiResponse<List<ToppingGroup>>>> getToppingFromDish(String dishId) {
        MutableLiveData<Resource<ApiResponse<List<ToppingGroup>>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        dishService.getToppingFromDish(dishId).enqueue(new Callback<ApiResponse<List<ToppingGroup>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ToppingGroup>>> call, Response<ApiResponse<List<ToppingGroup>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("DishRepository", "getToppingFromDish: " + response.body());
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
            public void onFailure(Call<ApiResponse<List<ToppingGroup>>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
