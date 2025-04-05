package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishImage;
import com.example.food_ordering_mobile_app.models.rating.ListRatingResponse;
import com.example.food_ordering_mobile_app.models.rating.Rating;
import com.example.food_ordering_mobile_app.repository.RatingRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;
import java.util.Map;

public class RatingViewModel extends AndroidViewModel {
    private final RatingRepository ratingRepository;

    private final MutableLiveData<Resource<ListRatingResponse>> allStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<ListRatingResponse>> getAllStoreRatingResponse() {
        return allStoreRatingResponse;
    }
    private final MutableLiveData<Resource<Rating>> detailRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<Rating>> getDetailRatingResponse() {
        return detailRatingResponse;
    }
    private final MutableLiveData<Resource<String>> addStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<String>> getAddStoreRatingResponse() {
        return addStoreRatingResponse;
    }
    private final MutableLiveData<Resource<Rating>> editStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<Rating>> getEditStoreRatingResponse() {
        return editStoreRatingResponse;
    }
    private final MutableLiveData<Resource<Rating>> deleteStoreRatingResponse = new MutableLiveData<>();
    public LiveData<Resource<Rating>> getDeleteStoreRatingResponse() {
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
        LiveData<Resource<Rating>> result = ratingRepository.getDetailRating(ratingId);
        result.observeForever(new Observer<Resource<Rating>>() {
            @Override
            public void onChanged(Resource<Rating> resource) {
                Log.d("RatingViewModel", "getCurrentUser: " + resource);
                detailRatingResponse.setValue(resource);
            }
        });
    }

    public void addStoreRating(String storeId, List<String> dishes, float rating, String comment) {
        LiveData<Resource<String>> result = ratingRepository.addStoreRating(storeId, dishes, rating, comment);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                Log.d("RatingViewModel", "getCurrentUser: " + resource);
                addStoreRatingResponse.setValue(resource);
            }
        });
    }

    public void addStoreRatingImages(String storeId, List<String> dishes, float rating, String comment, List<DishImage> imageUrls) {
        LiveData<Resource<String>> result = ratingRepository.addStoreRatingImage(storeId, dishes, rating, comment, imageUrls);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                Log.d("RatingViewModel", "getCurrentUser: " + resource);
                addStoreRatingResponse.setValue(resource);
            }
        });
    }

    public void editStoreRating(String storeId, Rating rating) {
        LiveData<Resource<Rating>> result = ratingRepository.editStoreRating(storeId, rating);
        result.observeForever(new Observer<Resource<Rating>>() {
            @Override
            public void onChanged(Resource<Rating> resource) {
                Log.d("RatingViewModel", "getCurrentUser: " + resource);
                editStoreRatingResponse.setValue(resource);
            }
        });
    }


    public void deleteStoreRating(String ratingId) {
        LiveData<Resource<Rating>> result = ratingRepository.deleteStoreRating(ratingId);
        result.observeForever(new Observer<Resource<Rating>>() {
            @Override
            public void onChanged(Resource<Rating> resource) {
                Log.d("RatingViewModel", "getCurrentUser: " + resource);
                deleteStoreRatingResponse.setValue(resource);
            }
        });
    }
}
