package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.MessageResponse;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishImage;
import com.example.food_ordering_mobile_app.models.rating.ListRatingResponse;
import com.example.food_ordering_mobile_app.models.rating.Rating;
import com.example.food_ordering_mobile_app.models.rating.RatingDetailResponse;
import com.example.food_ordering_mobile_app.repository.RatingRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;

public class RatingViewModel extends AndroidViewModel {
    private final RatingRepository ratingRepository;

    private final MutableLiveData<Resource<ListRatingResponse>> allStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<ListRatingResponse>> getAllStoreRatingResponse() {
        return allStoreRatingResponse;
    }
    private final MutableLiveData<Resource<RatingDetailResponse>> detailRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<RatingDetailResponse>> getDetailRatingResponse() {
        return detailRatingResponse;
    }
    private final MutableLiveData<Resource<String>> addStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<String>> getAddStoreRatingResponse() {
        return addStoreRatingResponse;
    }
    private final MutableLiveData<Resource<MessageResponse>> editStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<MessageResponse>> getEditStoreRatingResponse() {
        return editStoreRatingResponse;
    }
    private final MutableLiveData<Resource<MessageResponse>> deleteStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<MessageResponse>> getDeleteStoreRatingResponse() {
        return deleteStoreRatingResponse;
    }

    public RatingViewModel(Application application) {
        super(application);
        ratingRepository = new RatingRepository(application);
    }

    public void getAllStoreRating(String storeId, Map<String, String> queryParams) {
        LiveData<Resource<ListRatingResponse>> result = ratingRepository.getAllStoreRating(storeId, queryParams);
        result.observeForever(new Observer<Resource<ListRatingResponse>>() {
            @Override
            public void onChanged(Resource<ListRatingResponse> resource) {
                Log.d("RatingViewModel", "getCurrentUser: " + resource);
                allStoreRatingResponse.setValue(resource);
            }
        });
    }

    public void getDetailRating(String ratingId) {
        LiveData<Resource<RatingDetailResponse>> result = ratingRepository.getDetailRating(ratingId);
        result.observeForever(new Observer<Resource<RatingDetailResponse>>() {
            @Override
            public void onChanged(Resource<RatingDetailResponse> resource) {
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
        LiveData<Resource<MessageResponse>> result = ratingRepository.editStoreRating(ratingId, data);
        result.observeForever(new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                editStoreRatingResponse.setValue(resource);
            }
        });
    }

    public void deleteStoreRating(String ratingId) {
        LiveData<Resource<MessageResponse>> result = ratingRepository.deleteStoreRating(ratingId);
        result.observeForever(new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                deleteStoreRatingResponse.setValue(resource);
            }
        });
    }
}
