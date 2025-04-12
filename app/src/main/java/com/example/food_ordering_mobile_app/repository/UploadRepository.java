package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.Image;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.UploadService;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadRepository {
    private UploadService uploadService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public UploadRepository(Context context) {
        uploadService = RetrofitClient.getClient(context).create(UploadService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public LiveData<Resource<String>> uploadAvatar(Uri imageUri, Context context) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        File file = new File(getRealPathFromURI(imageUri, context));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        uploadService.uploadAvatar(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success("Upload thành công!", null));
                } else {
                    result.setValue(Resource.error("Upload thất bại!", null));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Image>>> uploadMultipleImages(List<Uri> imageUris, Context context) {
        MutableLiveData<Resource<List<Image>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        List<MultipartBody.Part> parts = new ArrayList<>();

        for (Uri uri : imageUris) {
            File file = new File(getRealPathFromURI(uri, context));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            parts.add(body);
        }

        uploadService.uploadImages(parts).enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                if (response.isSuccessful()) {
                    Log.d("UploadSuccess", "Response: " + response.body());
                    result.setValue(Resource.success("Upload thành công", response.body()));
                } else {
                    Log.d("UploadError", "Error Response: " + response.errorBody());
                    result.setValue(Resource.error("Upload thất bại!", null));
                }
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Log.d("UploadFailure", "Error Message: " + t.getMessage());
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }


    // Hàm chuyển đổi URI sang đường dẫn thực
    private String getRealPathFromURI(Uri uri, Context context) {
        String result = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}
