package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.MessageResponse;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishImage;
import com.example.food_ordering_mobile_app.models.rating.ListRatingResponse;
import com.example.food_ordering_mobile_app.models.rating.Rating;
import com.example.food_ordering_mobile_app.models.rating.RatingDetailResponse;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.RatingService;
import com.example.food_ordering_mobile_app.utils.PersistentCookieStore;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class RatingRepository {
    private RatingService ratingService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public RatingRepository(Context context) {
        ratingService = RetrofitClient.getClient(context).create(RatingService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);

        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();

        if (!(cookieManager.getCookieStore() instanceof PersistentCookieStore)) {
            Log.e("RatingRepository", "CookieStore chưa đúng, thiết lập lại...");
            cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);
        } else {
            Log.d("RatingRepository", "PersistentCookieStore đã được thiết lập!");
        }
    }

    public LiveData<Resource<ListRatingResponse>> getAllStoreRating(String storeId, Map<String, String> queryParams) {
        MutableLiveData<Resource<ListRatingResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        ratingService.getAllStoreRating(storeId, queryParams).enqueue(new Callback<ListRatingResponse>() {
            @Override
            public void onResponse(Call<ListRatingResponse> call, Response<ListRatingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RatingRepository", "getAllStoreRating: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                        Log.d("RatingRepository", "getAllStoreRating Error: " + errorMessage);
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                        Log.d("RatingRepository", "getAllStoreRating Error: " +e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListRatingResponse> call, Throwable t) {
                Log.d("RatingRepository", "getAllStoreRating Error: " + t.getMessage());
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<RatingDetailResponse>> getDetailRating(String ratingId) {
        MutableLiveData<Resource<RatingDetailResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        ratingService.getDetailRating(ratingId).enqueue(new Callback<RatingDetailResponse>() {
            @Override
            public void onResponse(Call<RatingDetailResponse> call, Response<RatingDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RatingRepository", "getDetailRating: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                        Log.d("RatingRepository", "getDetailRating Error: " + errorMessage);
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<RatingDetailResponse> call, Throwable t) {
                Log.d("RatingRepository", "getDetailRating Error: " + t.getMessage());
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<String>> addStoreRating(String storeId, Map<String, Object> data) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        ratingService.addStoreRating(storeId, data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RatingRepository", "getAllStoreRating: " + response.body());
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
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<String>> addStoreRatingImage(String storeId, List<String> dishes, float rating, String comment, List<DishImage> imageUrls) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        Map<String, Object> data = new HashMap<>();
        data.put("dishes", dishes);
        data.put("ratingValue", rating);
        data.put("comment", comment);
        data.put("images", imageUrls);

        ratingService.addStoreRating(storeId, data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RatingRepository", "getAllStoreRating: " + response.body());
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
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<MessageResponse>> editStoreRating(String ratingId, @Body Map<String, Object> data) {
        MutableLiveData<Resource<MessageResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        ratingService.editStoreRating(ratingId, data).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RatingRepository", "getAllStoreRating: " + response.body());
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
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<MessageResponse>> deleteStoreRating(String ratingId) {
        MutableLiveData<Resource<MessageResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        ratingService.deleteStoreRating(ratingId).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RatingRepository", "getAllStoreRating: " + response.body());
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
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
