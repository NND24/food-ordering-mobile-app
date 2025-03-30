package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.store.ListStoreResponse;
import com.example.food_ordering_mobile_app.models.store.StoreResponse;
import com.example.food_ordering_mobile_app.repository.StoreRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.Map;

public class StoreViewModel extends AndroidViewModel {
    private final StoreRepository storeRepository;

    private final MutableLiveData<Resource<ListStoreResponse>> allStoreResponse = new MutableLiveData<>();
    public LiveData<Resource<ListStoreResponse>> getAllStoreResponse() {
        return allStoreResponse;
    }
    private final MutableLiveData<Resource<ListStoreResponse>> ratingStoreResponse = new MutableLiveData<>();
    public LiveData<Resource<ListStoreResponse>> getRatingStoreResponse() {
        return ratingStoreResponse;
    }
    private final MutableLiveData<Resource<ListStoreResponse>> standoutStoreResponse = new MutableLiveData<>();
    public LiveData<Resource<ListStoreResponse>> getStandoutStoreResponse() {
        return standoutStoreResponse;
    }
    private final MutableLiveData<Resource<ListStoreResponse>> searchStoreResponse = new MutableLiveData<>();
    public LiveData<Resource<ListStoreResponse>> getSearchStoreResponse() {
        return searchStoreResponse;
    }
    private final MutableLiveData<Resource<StoreResponse>> storeInformationResponse = new MutableLiveData<>();
    public LiveData<Resource<StoreResponse>> getStoreInformationResponse() {
        return storeInformationResponse;
    }

    public StoreViewModel(Application application) {
        super(application);
        storeRepository = new StoreRepository(application);
    }

    public void getAllStore(Map<String, String> queryParams) {
        LiveData<Resource<ListStoreResponse>> result = storeRepository.getAllStore(queryParams);
        result.observeForever(new Observer<Resource<ListStoreResponse>>() {
            @Override
            public void onChanged(Resource<ListStoreResponse> resource) {
                allStoreResponse.setValue(resource);
            }
        });
    }

    public void getStandoutStore(Map<String, String> queryParams) {
        LiveData<Resource<ListStoreResponse>> result = storeRepository.getAllStore(queryParams);
        result.observeForever(new Observer<Resource<ListStoreResponse>>() {
            @Override
            public void onChanged(Resource<ListStoreResponse> resource) {
                standoutStoreResponse.setValue(resource);
            }
        });
    }

    public void getRatingStore(Map<String, String> queryParams) {
        LiveData<Resource<ListStoreResponse>> result = storeRepository.getAllStore(queryParams);
        result.observeForever(new Observer<Resource<ListStoreResponse>>() {
            @Override
            public void onChanged(Resource<ListStoreResponse> resource) {
                ratingStoreResponse.setValue(resource);
            }
        });
    }

    public void getSearchStore(Map<String, String> queryParams) {
        LiveData<Resource<ListStoreResponse>> result = storeRepository.getAllStore(queryParams);
        result.observeForever(new Observer<Resource<ListStoreResponse>>() {
            @Override
            public void onChanged(Resource<ListStoreResponse> resource) {
                searchStoreResponse.setValue(resource);
            }
        });
    }

    public void getStoreInformation(String storeId) {
        LiveData<Resource<StoreResponse>> result = storeRepository.getStoreInformation(storeId);
        result.observeForever(new Observer<Resource<StoreResponse>>() {
            @Override
            public void onChanged(Resource<StoreResponse> resource) {
                Log.d("StoreViewModel", "getCurrentUser: " + resource);
                storeInformationResponse.setValue(resource);
            }
        });
    }

}
