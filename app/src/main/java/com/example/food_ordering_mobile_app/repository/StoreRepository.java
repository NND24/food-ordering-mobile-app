package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.store.ListStoreResponse;
import com.example.food_ordering_mobile_app.models.store.StoreResponse;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.StoreService;
import com.example.food_ordering_mobile_app.utils.PersistentCookieStore;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreRepository {
    private StoreService storeService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public StoreRepository(Context context) {
        storeService = RetrofitClient.getClient(context).create(StoreService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);

        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();

        if (!(cookieManager.getCookieStore() instanceof PersistentCookieStore)) {
            Log.e("StoreRepository", "CookieStore chưa đúng, thiết lập lại...");
            cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);
        } else {
            Log.d("StoreRepository", "PersistentCookieStore đã được thiết lập!");
        }
    }

    public LiveData<Resource<ListStoreResponse>> getAllStore(Map<String, String> queryParams) {
        MutableLiveData<Resource<ListStoreResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        storeService.getAllStore(queryParams).enqueue(new Callback<ListStoreResponse>() {
            @Override
            public void onResponse(Call<ListStoreResponse> call, Response<ListStoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("StoreRepository", "getAllStore: " + response.body().toString());
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
            public void onFailure(Call<ListStoreResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<StoreResponse>> getStoreInformation(String storeId) {
        MutableLiveData<Resource<StoreResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        storeService.getStoreInformation(storeId).enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("StoreRepository", "getStoreInformation: " + response.body().toString());
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
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
