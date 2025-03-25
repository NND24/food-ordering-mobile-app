package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.User;
import com.example.food_ordering_mobile_app.repository.UserRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository userRepository;

    private final MutableLiveData<Resource<User>> currentUserResponse = new MutableLiveData<>();
    public LiveData<Resource<User>> getCurrentUserResponse() {
        return currentUserResponse;
    }
    private final MutableLiveData<Resource<User>> updateUserResponse = new MutableLiveData<>();
    public LiveData<Resource<User>> getUpdateUserResponse() {
        return updateUserResponse;
    }

    public UserViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public void getCurrentUser() {
        LiveData<Resource<User>> result = userRepository.getCurrentUser();
        result.observeForever(new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> resource) {
                Log.d("UserViewModel", "getCurrentUser: " + resource);
                currentUserResponse.setValue(resource);
            }
        });
    }

    public void updateUser(User user) {
        LiveData<Resource<User>> result = userRepository.updateUser(user);
        result.observeForever(new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> resource) {
                Log.d("UserViewModel", "updateUser: " + resource);
                updateUserResponse.setValue(resource);
            }
        });
    }
}
