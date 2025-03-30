package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.favorite.Favorite;
import com.example.food_ordering_mobile_app.models.favorite.FavoriteResponse;
import com.example.food_ordering_mobile_app.repository.FavoriteRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

public class FavoriteViewModel extends AndroidViewModel {
    private final FavoriteRepository favoriteRepository;

    private final MutableLiveData<Resource<FavoriteResponse>> userFavoriteResponse = new MutableLiveData<>();
    public LiveData<Resource<FavoriteResponse>> getUserFavoriteResponse() {
        return userFavoriteResponse;
    }
    private final MutableLiveData<Resource<Favorite>> addFavoriteResponse = new MutableLiveData<>();
    public LiveData<Resource<Favorite>> getAddFavoriteResponse() {
        return addFavoriteResponse;
    }
    private final MutableLiveData<Resource<Favorite>> removeFavoriteResponse = new MutableLiveData<>();
    public LiveData<Resource<Favorite>> getRemoveFavoriteResponse() {
        return removeFavoriteResponse;
    }
    private final MutableLiveData<Resource<Favorite>> removeAllFavoriteResponse = new MutableLiveData<>();
    public LiveData<Resource<Favorite>> getRemoveAllFavoriteResponse() {
        return removeAllFavoriteResponse;
    }

    public FavoriteViewModel(Application application) {
        super(application);
        favoriteRepository = new FavoriteRepository(application);
    }

    public void getUserFavorite() {
        LiveData<Resource<FavoriteResponse>> result = favoriteRepository.getUserFavorite();
        result.observeForever(new Observer<Resource<FavoriteResponse>>() {
            @Override
            public void onChanged(Resource<FavoriteResponse> resource) {
                Log.d("FavoriteViewModel", "getUserFavorite: " + resource);
                userFavoriteResponse.setValue(resource);
            }
        });
    }

    public void addFavorite(Favorite favorite) {
        LiveData<Resource<Favorite>> result = favoriteRepository.addFavorite(favorite);
        result.observeForever(new Observer<Resource<Favorite>>() {
            @Override
            public void onChanged(Resource<Favorite> resource) {
                Log.d("FavoriteViewModel", "addFavorite: " + resource);
                addFavoriteResponse.setValue(resource);
            }
        });
    }

    public void removeFavorite(String id) {
        LiveData<Resource<Favorite>> result = favoriteRepository.removeFavorite(id);
        result.observeForever(new Observer<Resource<Favorite>>() {
            @Override
            public void onChanged(Resource<Favorite> resource) {
                Log.d("FavoriteViewModel", "removeFavorite: " + resource);
                removeFavoriteResponse.setValue(resource);
            }
        });
    }

    public void removeAllFavorite() {
        LiveData<Resource<Favorite>> result = favoriteRepository.removeAllFavorite();
        result.observeForever(new Observer<Resource<Favorite>>() {
            @Override
            public void onChanged(Resource<Favorite> resource) {
                Log.d("FavoriteViewModel", "removeAllFavorite: " + resource);
                removeAllFavoriteResponse.setValue(resource);
            }
        });
    }
}
