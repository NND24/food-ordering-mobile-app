package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishResponse;
import com.example.food_ordering_mobile_app.models.dish.ListDishResponse;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroupResponse;
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

    public LiveData<Resource<ListDishResponse>> getAllDish(String storeId) {
        MutableLiveData<Resource<ListDishResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        dishService.getAllDish(storeId).enqueue(new Callback<ListDishResponse>() {
            @Override
            public void onResponse(Call<ListDishResponse> call, Response<ListDishResponse> response) {
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
            public void onFailure(Call<ListDishResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<DishResponse>> getDish(String dishId) {
        MutableLiveData<Resource<DishResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        dishService.getDish(dishId).enqueue(new Callback<DishResponse>() {
            @Override
            public void onResponse(Call<DishResponse> call, Response<DishResponse> response) {
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
            public void onFailure(Call<DishResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<ToppingGroupResponse>> getToppingFromDish(String dishId) {
        MutableLiveData<Resource<ToppingGroupResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        dishService.getToppingFromDish(dishId).enqueue(new Callback<ToppingGroupResponse>() {
            @Override
            public void onResponse(Call<ToppingGroupResponse> call, Response<ToppingGroupResponse> response) {
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
            public void onFailure(Call<ToppingGroupResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
