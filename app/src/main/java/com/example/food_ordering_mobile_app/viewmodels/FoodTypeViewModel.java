package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.repository.FoodTypeRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class FoodTypeViewModel extends AndroidViewModel {
    private final FoodTypeRepository foodTypeRepository;

    private final MutableLiveData<Resource<List<FoodType>>> allFoodTypesResponse = new MutableLiveData<>();
    public LiveData<Resource<List<FoodType>>> getAllFoodTypesResponse() {
        return allFoodTypesResponse;
    }

    public FoodTypeViewModel(Application application) {
        super(application);
        foodTypeRepository = new FoodTypeRepository(application);
    }

    public void getAllFoodTypes() {
        LiveData<Resource<List<FoodType>>> result = foodTypeRepository.getAllFoodTypes();
        result.observeForever(new Observer<Resource<List<FoodType>>>() {
            @Override
            public void onChanged(Resource<List<FoodType>> resource) {
                Log.d("FoodTypeViewModel", "getAllFoodTypes: " + resource);
                allFoodTypesResponse.setValue(resource);
            }
        });
    }
}
