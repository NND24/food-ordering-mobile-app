package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.Image;
import com.example.food_ordering_mobile_app.repository.UploadRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class UploadViewModel extends AndroidViewModel {
    private final UploadRepository uploadRepository;

    private final MutableLiveData<Resource<String>> uploadAvatarResponse = new MutableLiveData<>();
    public LiveData<Resource<String>> getUploadAvatarResponse() {
        return uploadAvatarResponse;
    }
    private final MutableLiveData<Resource<List<Image>>> uploadImagesResponse = new MutableLiveData<>();
    public LiveData<Resource<List<Image>>> getUploadImagesResponse() {
        return uploadImagesResponse;
    }

    public UploadViewModel(Application application) {
        super(application);
        uploadRepository = new UploadRepository(application);
    }

    public void uploadAvatar(Uri imageUri, Context context) {
        LiveData<Resource<String>> result = uploadRepository.uploadAvatar(imageUri, context);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                uploadAvatarResponse.setValue(resource);
            }
        });
    }

    public void uploadImages(List<Uri> imageUris, Context context) {
        LiveData<Resource<List<Image>>> result = uploadRepository.uploadMultipleImages(imageUris, context);
        result.observeForever(new Observer<Resource<List<Image>>>() {
            @Override
            public void onChanged(Resource<List<Image>> resource) {
                uploadImagesResponse.setValue(resource);
            }
        });
    }
}
