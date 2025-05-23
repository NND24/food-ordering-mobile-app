package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.FoodTypeService;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodTypeRepository {
    private FoodTypeService foodTypeService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public FoodTypeRepository(Context context) {
        foodTypeService = RetrofitClient.getClient(context).create(FoodTypeService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public LiveData<Resource<List<FoodType>>> getAllFoodTypes() {
        Log.d("FoodTypeRepository", "getAllFoodTypes test");
        MutableLiveData<Resource<List<FoodType>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        foodTypeService.getAllFoodTypes().enqueue(new Callback<List<FoodType>>() {
            @Override
            public void onResponse(Call<List<FoodType>> call, Response<List<FoodType>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("FoodTypeRepository", "getAllFoodTypes: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                        Log.d("FoodTypeRepository", "getAllFoodTypes error: " + errorMessage);
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FoodType>> call, Throwable t) {
                Log.d("FoodTypeRepository", "getAllFoodTypes error: " + t.getMessage());
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
