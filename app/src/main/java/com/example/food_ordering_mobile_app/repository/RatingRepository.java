package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.Image;
import com.example.food_ordering_mobile_app.models.rating.Rating;
import com.example.food_ordering_mobile_app.models.rating.RatingDetail;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.RatingService;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import org.json.JSONObject;

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
    }

    public LiveData<Resource<ApiResponse<List<Rating>>>> getAllStoreRating(String storeId, Map<String, String> queryParams) {
        MutableLiveData<Resource<ApiResponse<List<Rating>>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        ratingService.getAllStoreRating(storeId, queryParams).enqueue(new Callback<ApiResponse<List<Rating>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Rating>>> call, Response<ApiResponse<List<Rating>>> response) {
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
            public void onFailure(Call<ApiResponse<List<Rating>>> call, Throwable t) {
                Log.d("RatingRepository", "getAllStoreRating Error: " + t.getMessage());
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<RatingDetail>> getDetailRating(String ratingId) {
        MutableLiveData<Resource<RatingDetail>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        ratingService.getDetailRating(ratingId).enqueue(new Callback<RatingDetail>() {
            @Override
            public void onResponse(Call<RatingDetail> call, Response<RatingDetail> response) {
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
            public void onFailure(Call<RatingDetail> call, Throwable t) {
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

    public LiveData<Resource<String>> addStoreRatingImage(String storeId, List<String> dishes, float rating, String comment, List<Image> imageUrls) {
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

    public LiveData<Resource<ApiResponse<String>>> editStoreRating(String ratingId, @Body Map<String, Object> data) {
        MutableLiveData<Resource<ApiResponse<String>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        ratingService.editStoreRating(ratingId, data).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
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
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<ApiResponse<String>>> deleteStoreRating(String ratingId) {
        MutableLiveData<Resource<ApiResponse<String>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        ratingService.deleteStoreRating(ratingId).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
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
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
