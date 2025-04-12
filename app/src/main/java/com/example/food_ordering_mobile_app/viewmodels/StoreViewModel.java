package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.repository.StoreRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;
import java.util.Map;

public class StoreViewModel extends AndroidViewModel {
    private final StoreRepository storeRepository;

    private final MutableLiveData<Resource<ApiResponse<List<Store>>>> allStoreResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Store>>>> getAllStoreResponse() {
        return allStoreResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<List<Store>>>> ratingStoreResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Store>>>> getRatingStoreResponse() {
        return ratingStoreResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<List<Store>>>> standoutStoreResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Store>>>> getStandoutStoreResponse() {
        return standoutStoreResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<List<Store>>>> searchStoreResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Store>>>> getSearchStoreResponse() {
        return searchStoreResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<Store>>> storeInformationResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<Store>>> getStoreInformationResponse() {
        return storeInformationResponse;
    }

    public StoreViewModel(Application application) {
        super(application);
        storeRepository = new StoreRepository(application);
    }

    public void getAllStore(Map<String, String> queryParams) {
        LiveData<Resource<ApiResponse<List<Store>>>> result = storeRepository.getAllStore(queryParams);
        result.observeForever(new Observer<Resource<ApiResponse<List<Store>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Store>>> resource) {
                allStoreResponse.setValue(resource);
            }
        });
    }

    public void getStandoutStore(Map<String, String> queryParams) {
        LiveData<Resource<ApiResponse<List<Store>>>> result = storeRepository.getAllStore(queryParams);
        result.observeForever(new Observer<Resource<ApiResponse<List<Store>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Store>>> resource) {
                standoutStoreResponse.setValue(resource);
            }
        });
    }

    public void getRatingStore(Map<String, String> queryParams) {
        LiveData<Resource<ApiResponse<List<Store>>>> result = storeRepository.getAllStore(queryParams);
        result.observeForever(new Observer<Resource<ApiResponse<List<Store>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Store>>> resource) {
                ratingStoreResponse.setValue(resource);
            }
        });
    }

    public void getSearchStore(Map<String, String> queryParams) {
        LiveData<Resource<ApiResponse<List<Store>>>> result = storeRepository.getAllStore(queryParams);
        result.observeForever(new Observer<Resource<ApiResponse<List<Store>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Store>>> resource) {
                searchStoreResponse.setValue(resource);
            }
        });
    }

    public void getStoreInformation(String storeId) {
        LiveData<Resource<ApiResponse<Store>>> result = storeRepository.getStoreInformation(storeId);
        result.observeForever(new Observer<Resource<ApiResponse<Store>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Store>> resource) {
                Log.d("StoreViewModel", "getCurrentUser: " + resource);
                storeInformationResponse.setValue(resource);
            }
        });
    }

}
