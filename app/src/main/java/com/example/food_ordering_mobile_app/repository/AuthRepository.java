package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.network.services.AuthService;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private AuthService authService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public AuthRepository(Context context) {
        authService = RetrofitClient.getClient(context).create(AuthService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public LiveData<Resource<User>> loginMobile(String email, String password) {
        MutableLiveData<Resource<User>> loginLiveData  = new MutableLiveData<>();
        loginLiveData.setValue(Resource.loading(null)); // Trạng thái Loading

        User request = new User(email, password);
        authService.loginMobile(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    String refreshToken = response.body().getRefreshToken();
                    String userId = response.body().getId();

                    // Lưu vào SharedPreferences
                    sharedPreferencesHelper.saveUserData(accessToken, refreshToken, userId);

                    loginLiveData.setValue(Resource.success("Đăng nhập thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");

                        loginLiveData.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        loginLiveData.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loginLiveData.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return loginLiveData ;
    }

    public LiveData<Resource<User>> loginWithGoogle(String name, String email) {
        MutableLiveData<Resource<User>> googleLoginLiveData = new MutableLiveData<>();
        googleLoginLiveData.setValue(Resource.loading(null));

        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);

        authService.loginWithGoogle(data).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    String refreshToken = response.body().getRefreshToken();
                    String userId = response.body().getId();

                    // Lưu vào SharedPreferences
                    sharedPreferencesHelper.saveUserData(accessToken, refreshToken, userId);

                    googleLoginLiveData.setValue(Resource.success("Đăng nhập thành công!", response.body()));
                } else {
                    googleLoginLiveData.setValue(Resource.error("Xác thực Google thất bại!", null));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                googleLoginLiveData.setValue(Resource.error("Lỗi mạng, vui lòng thử lại!", null));
            }
        });

        return googleLoginLiveData;
    }

    public LiveData<Resource<User>> refreshTokenMobile(String refreshToken) {
        MutableLiveData<Resource<User>> refreshTokenLiveData  = new MutableLiveData<>();
        refreshTokenLiveData.setValue(Resource.loading(null)); // Trạng thái Loading

        authService.refreshTokenMobile(refreshToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = response.body().getAccessToken();

                    // Lưu vào SharedPreferences
                    sharedPreferencesHelper.saveAccessToken(accessToken);

                    refreshTokenLiveData.setValue(Resource.success("Refresh Token Success!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");

                        refreshTokenLiveData.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        refreshTokenLiveData.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                refreshTokenLiveData.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return refreshTokenLiveData ;
    }

    public LiveData<Resource<User>> register(String name, String email, String phonenumber, String gender, String password) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Trạng thái Loading

        User user = new User(name, email, phonenumber, gender, password);
        authService.register(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success("Đăng ký thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

    public LiveData<Resource<String>> forgotPassword(String email) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Trạng thái Loading

        Map<String, String> data = new HashMap<>();
        data.put("email", email);

        authService.forgotPassword(data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body(), null));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

    public MutableLiveData<Resource<String>> checkOtp(String email, String otp) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Đặt trạng thái Loading

        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("otp", otp);

        authService.checkOTP(data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body(), null));
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định!";
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi khi đọc phản hồi từ server!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

    public MutableLiveData<Resource<String>> resetPassword(String email, String password) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Đặt trạng thái Loading

        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("newPassword", password);

        authService.resetPassword(data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body(), null));
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định!";
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi khi đọc phản hồi từ server!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

    public MutableLiveData<Resource<String>> changePassword(String oldPassword, String password) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Đặt trạng thái Loading

        Map<String, String> data = new HashMap<>();
        data.put("oldPassword", oldPassword);
        data.put("newPassword", password);

        authService.changePassword(data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body(), null));
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định!";
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi khi đọc phản hồi từ server!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

    public MutableLiveData<Resource<String>> logout(Context context) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Trạng thái Loading

        authService.logout().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Logout", "Logout successful, clearing data...");

                    sharedPreferencesHelper.clearUserData();

                    result.setValue(Resource.success("Logout thành công!", null));
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định!";
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi khi đọc phản hồi từ server!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

}

