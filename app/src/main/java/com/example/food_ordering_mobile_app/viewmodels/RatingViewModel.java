package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.rating.Rating;
import com.example.food_ordering_mobile_app.models.rating.RatingDetail;
import com.example.food_ordering_mobile_app.repository.RatingRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;
import java.util.Map;

public class RatingViewModel extends AndroidViewModel {
    private final RatingRepository ratingRepository;

    private final MutableLiveData<Resource<ApiResponse<List<Rating>>>> allStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Rating>>>> getAllStoreRatingResponse() {
        return allStoreRatingResponse;
    }
    private final MutableLiveData<Resource<RatingDetail>> detailRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<RatingDetail>> getDetailRatingResponse() {
        return detailRatingResponse;
    }
    private final MutableLiveData<Resource<String>> addStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<String>> getAddStoreRatingResponse() {
        return addStoreRatingResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> editStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getEditStoreRatingResponse() {
        return editStoreRatingResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> deleteStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getDeleteStoreRatingResponse() {
        return deleteStoreRatingResponse;
    }

    public RatingViewModel(Application application) {
        super(application);
        ratingRepository = new RatingRepository(application);
    }

    public void getAllStoreRating(String storeId, Map<String, String> queryParams) {
        LiveData<Resource<ApiResponse<List<Rating>>>> result = ratingRepository.getAllStoreRating(storeId, queryParams);
        result.observeForever(new Observer<Resource<ApiResponse<List<Rating>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Rating>>> resource) {
                Log.d("RatingViewModel", "getCurrentUser: " + resource);
                allStoreRatingResponse.setValue(resource);
            }
        });
    }

    public void getDetailRating(String ratingId) {
        LiveData<Resource<RatingDetail>> result = ratingRepository.getDetailRating(ratingId);
        result.observeForever(new Observer<Resource<RatingDetail>>() {
            @Override
            public void onChanged(Resource<RatingDetail> resource) {
                detailRatingResponse.setValue(resource);
            }
        });
    }

    public void addStoreRating(String storeId, Map<String, Object> data) {
        LiveData<Resource<String>> result = ratingRepository.addStoreRating(storeId, data);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                Log.d("RatingViewModel", "getCurrentUser: " + resource);
                addStoreRatingResponse.setValue(resource);
            }
        });
    }

    public void editStoreRating(String ratingId, Map<String, Object> data) {
        LiveData<Resource<ApiResponse<String>>> result = ratingRepository.editStoreRating(ratingId, data);
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                editStoreRatingResponse.setValue(resource);
            }
        });
    }

    public void deleteStoreRating(String ratingId) {
        LiveData<Resource<ApiResponse<String>>> result = ratingRepository.deleteStoreRating(ratingId);
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                deleteStoreRatingResponse.setValue(resource);
            }
        });
    }
}
