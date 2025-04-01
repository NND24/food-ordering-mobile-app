package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.dish.DishResponse;
import com.example.food_ordering_mobile_app.models.dish.ListDishResponse;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroupResponse;
import com.example.food_ordering_mobile_app.repository.DishRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

public class DishViewModel extends AndroidViewModel {
    private final DishRepository dishRepository;

    private final MutableLiveData<Resource<ListDishResponse>> allBigDishResponse = new MutableLiveData<>();
    public LiveData<Resource<ListDishResponse>> getAllBigDishResponse() {
        return allBigDishResponse;
    }
    private final MutableLiveData<Resource<ListDishResponse>> allDishResponse = new MutableLiveData<>();
    public LiveData<Resource<ListDishResponse>> getAllDishResponse() {
        return allDishResponse;
    }
    private final MutableLiveData<Resource<DishResponse>> dishResponse = new MutableLiveData<>();
    public LiveData<Resource<DishResponse>> getDishResponse() {
        return dishResponse;
    }
    private final MutableLiveData<Resource<ToppingGroupResponse>> toppingFromDishResponse = new MutableLiveData<>();
    public LiveData<Resource<ToppingGroupResponse>> getToppingFromDishResponse() {
        return toppingFromDishResponse;
    }

    public DishViewModel(Application application) {
        super(application);
        dishRepository = new DishRepository(application);
    }

    public void getAllBigDish(String storeId) {
        LiveData<Resource<ListDishResponse>> result = dishRepository.getAllDish(storeId);
        result.observeForever(new Observer<Resource<ListDishResponse>>() {
            @Override
            public void onChanged(Resource<ListDishResponse> resource) {
                Log.d("DishViewModel", "getAllBigDish: " + resource.toString());
                allBigDishResponse.setValue(resource);
            }
        });
    }

    public void getAllDish(String storeId) {
        LiveData<Resource<ListDishResponse>> result = dishRepository.getAllDish(storeId);
        result.observeForever(new Observer<Resource<ListDishResponse>>() {
            @Override
            public void onChanged(Resource<ListDishResponse> resource) {
                Log.d("DishViewModel", "getAllDish: " + resource);
                allDishResponse.setValue(resource);
            }
        });
    }

    public void getDish(String dishId) {
        LiveData<Resource<DishResponse>> result = dishRepository.getDish(dishId);
        result.observeForever(new Observer<Resource<DishResponse>>() {
            @Override
            public void onChanged(Resource<DishResponse> resource) {
                Log.d("DishViewModel", "getDish: " + resource.toString());
                dishResponse.setValue(resource);
            }
        });
    }

    public void getToppingFromDish(String dishId) {
        LiveData<Resource<ToppingGroupResponse>> result = dishRepository.getToppingFromDish(dishId);
        result.observeForever(new Observer<Resource<ToppingGroupResponse>>() {
            @Override
            public void onChanged(Resource<ToppingGroupResponse> resource) {
                Log.d("DishViewModel", "getToppingFromDish: " + resource);
                toppingFromDishResponse.setValue(resource);
            }
        });
    }
}
