package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.repository.DishRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class DishViewModel extends AndroidViewModel {
    private final DishRepository dishRepository;

    private final MutableLiveData<Resource<List<Dish>>> allBigDishResponse = new MutableLiveData<>();
    public LiveData<Resource<List<Dish>>> getAllBigDishResponse() {
        return allBigDishResponse;
    }
    private final MutableLiveData<Resource<List<Dish>>> allDishResponse = new MutableLiveData<>();
    public LiveData<Resource<List<Dish>>> getAllDishResponse() {
        return allDishResponse;
    }
    private final MutableLiveData<Resource<Dish>> dishResponse = new MutableLiveData<>();
    public LiveData<Resource<Dish>> getDishResponse() {
        return dishResponse;
    }
    private final MutableLiveData<Resource<Dish>> toppingFromDishResponse = new MutableLiveData<>();
    public LiveData<Resource<Dish>> getToppingFromDishResponse() {
        return toppingFromDishResponse;
    }

    public DishViewModel(Application application) {
        super(application);
        dishRepository = new DishRepository(application);
    }

    public void getAllBigDish(String storeId) {
        LiveData<Resource<List<Dish>>> result = dishRepository.getAllDish(storeId);
        result.observeForever(new Observer<Resource<List<Dish>>>() {
            @Override
            public void onChanged(Resource<List<Dish>> resource) {
                Log.d("DishViewModel", "getAllBigDish: " + resource);
                allBigDishResponse.setValue(resource);
            }
        });
    }

    public void getAllDish(String storeId) {
        LiveData<Resource<List<Dish>>> result = dishRepository.getAllDish(storeId);
        result.observeForever(new Observer<Resource<List<Dish>>>() {
            @Override
            public void onChanged(Resource<List<Dish>> resource) {
                Log.d("DishViewModel", "getAllDish: " + resource);
                allDishResponse.setValue(resource);
            }
        });
    }

    public void getDish(String dishId) {
        LiveData<Resource<Dish>> result = dishRepository.getDish(dishId);
        result.observeForever(new Observer<Resource<Dish>>() {
            @Override
            public void onChanged(Resource<Dish> resource) {
                Log.d("DishViewModel", "getDish: " + resource);
                dishResponse.setValue(resource);
            }
        });
    }

    public void getToppingFromDish(String dishId) {
        LiveData<Resource<Dish>> result = dishRepository.getToppingFromDish(dishId);
        result.observeForever(new Observer<Resource<Dish>>() {
            @Override
            public void onChanged(Resource<Dish> resource) {
                Log.d("DishViewModel", "getToppingFromDish: " + resource);
                toppingFromDishResponse.setValue(resource);
            }
        });
    }
}
