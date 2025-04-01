package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.repository.AuthRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;

    private final MutableLiveData<Resource<User>> loginResponse = new MutableLiveData<>();
    public LiveData<Resource<User>> getLoginResponse() {
        return loginResponse;
    }
    private final MutableLiveData<Resource<User>> registerResponse = new MutableLiveData<>();
    public LiveData<Resource<User>> getRegisterResponse() {
        return registerResponse;
    }
    public MutableLiveData<Resource<String>> forgotPasswordResponse = new MutableLiveData<>();
    public LiveData<Resource<String>> getForgotPasswordResponse() {
        return forgotPasswordResponse;
    }
    public MutableLiveData<Resource<String>> checkOTPResponse = new MutableLiveData<Resource<String>>();
    public LiveData<Resource<String>> getCheckOTPResponse() {
        return checkOTPResponse;
    }
    public MutableLiveData<Resource<String>> resetPasswordResponse = new MutableLiveData<Resource<String>>();
    public LiveData<Resource<String>> getResetPasswordResponse() {
        return resetPasswordResponse;
    }
    public MutableLiveData<Resource<String>> changePasswordResponse = new MutableLiveData<Resource<String>>();
    public LiveData<Resource<String>> getChangePasswordResponse() {
        return changePasswordResponse;
    }
    public MutableLiveData<Resource<String>> logoutResponse = new MutableLiveData<Resource<String>>();
    public LiveData<Resource<String>> getLogoutResponse() {
        return logoutResponse;
    }

    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public void login(String email, String password) {
        LiveData<Resource<User>> result = authRepository.login(email, password);
        result.observeForever(new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> resource) {
                loginResponse.setValue(resource);
            }
        });
    }

    public void loginWithGoogle(String name, String email) {
        LiveData<Resource<User>> result = authRepository.loginWithGoogle(name, email);
        result.observeForever(new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> resource) {
                loginResponse.setValue(resource);
            }
        });
    }

    public void register(String name, String email, String phonenumber, String gender, String password) {
        LiveData<Resource<User>> result = authRepository.register(name, email, phonenumber, gender, password);
        result.observeForever(new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> resource) {
                loginResponse.setValue(resource);
            }
        });
    }

    public void forgotPassword(String email) {
        LiveData<Resource<String>> result = authRepository.forgotPassword(email);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                forgotPasswordResponse.setValue(resource);
            }
        });
    }

    public void checkOtp(String email, String otp) {
        LiveData<Resource<String>> result = authRepository.checkOtp(email, otp);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                checkOTPResponse.setValue(resource);
            }
        });
    }

    public void resetPassword(String email, String password) {
        LiveData<Resource<String>> result = authRepository.resetPassword(email, password);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                resetPasswordResponse.setValue(resource);
            }
        });
    }

    public void changePassword(String oldPassword, String password) {
        LiveData<Resource<String>> result = authRepository.changePassword(oldPassword, password);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                changePasswordResponse.setValue(resource);
            }
        });
    }

    public void logout(Context context) {
        LiveData<Resource<String>> result = authRepository.logout(context);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                logoutResponse.setValue(resource);
            }
        });
    }
}
