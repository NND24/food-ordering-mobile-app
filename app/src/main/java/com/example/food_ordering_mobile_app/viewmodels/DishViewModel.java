package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroup;
import com.example.food_ordering_mobile_app.repository.DishRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class DishViewModel extends AndroidViewModel {
    private final DishRepository dishRepository;

    private final MutableLiveData<Resource<ApiResponse<List<Dish>>>> allBigDishResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Dish>>>> getAllBigDishResponse() {
        return allBigDishResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<List<Dish>>>> allDishResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Dish>>>> getAllDishResponse() {
        return allDishResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<Dish>>> dishResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<Dish>>> getDishResponse() {
        return dishResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<List<ToppingGroup>>>> toppingFromDishResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<ToppingGroup>>>> getToppingFromDishResponse() {
        return toppingFromDishResponse;
    }

    public DishViewModel(Application application) {
        super(application);
        dishRepository = new DishRepository(application);
    }

    public void getAllBigDish(String storeId) {
        LiveData<Resource<ApiResponse<List<Dish>>>> result = dishRepository.getAllDish(storeId);
        result.observeForever(new Observer<Resource<ApiResponse<List<Dish>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Dish>>> resource) {
                allBigDishResponse.setValue(resource);
            }
        });
    }

    public void getAllDish(String storeId) {
        LiveData<Resource<ApiResponse<List<Dish>>>> result = dishRepository.getAllDish(storeId);
        result.observeForever(new Observer<Resource<ApiResponse<List<Dish>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Dish>>> resource) {
                allDishResponse.setValue(resource);
            }
        });
    }

    public void getDish(String dishId) {
        LiveData<Resource<ApiResponse<Dish>>> result = dishRepository.getDish(dishId);
        result.observeForever(new Observer<Resource<ApiResponse<Dish>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Dish>> resource) {
                dishResponse.setValue(resource);
            }
        });
    }

    public void getToppingFromDish(String dishId) {
        LiveData<Resource<ApiResponse<List<ToppingGroup>>>> result = dishRepository.getToppingFromDish(dishId);
        result.observeForever(new Observer<Resource<ApiResponse<List<ToppingGroup>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<ToppingGroup>>> resource) {
                toppingFromDishResponse.setValue(resource);
            }
        });
    }
}
