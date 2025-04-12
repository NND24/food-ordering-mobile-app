package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.favorite.Favorite;
import com.example.food_ordering_mobile_app.repository.FavoriteRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

public class FavoriteViewModel extends AndroidViewModel {
    private final FavoriteRepository favoriteRepository;

    private final MutableLiveData<Resource<ApiResponse<Favorite>>> userFavoriteResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<Favorite>>> getUserFavoriteResponse() {
        return userFavoriteResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> addFavoriteResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getAddFavoriteResponse() {
        return addFavoriteResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> removeFavoriteResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getRemoveFavoriteResponse() {
        return removeFavoriteResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> removeAllFavoriteResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getRemoveAllFavoriteResponse() {
        return removeAllFavoriteResponse;
    }

    public FavoriteViewModel(Application application) {
        super(application);
        favoriteRepository = new FavoriteRepository(application);
    }

    public void getUserFavorite() {
        LiveData<Resource<ApiResponse<Favorite>>> result = favoriteRepository.getUserFavorite();
        result.observeForever(new Observer<Resource<ApiResponse<Favorite>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Favorite>> resource) {
                Log.d("FavoriteViewModel", "getUserFavorite: " + resource);
                userFavoriteResponse.setValue(resource);
            }
        });
    }

    public void addFavorite(String storeId) {
        LiveData<Resource<ApiResponse<String>>> result = favoriteRepository.addFavorite(storeId);
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                Log.d("FavoriteViewModel", "addFavorite: " + resource);
                addFavoriteResponse.setValue(resource);
            }
        });
    }

    public void removeFavorite(String id) {
        LiveData<Resource<ApiResponse<String>>> result = favoriteRepository.removeFavorite(id);
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                Log.d("FavoriteViewModel", "removeFavorite: " + resource);
                removeFavoriteResponse.setValue(resource);
            }
        });
    }

    public void removeAllFavorite() {
        LiveData<Resource<ApiResponse<String>>> result = favoriteRepository.removeAllFavorite();
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                Log.d("FavoriteViewModel", "removeAllFavorite: " + resource);
                removeAllFavoriteResponse.setValue(resource);
            }
        });
    }
}
