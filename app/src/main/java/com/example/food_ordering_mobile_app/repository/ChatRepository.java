package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.chat.Chat;
import com.example.food_ordering_mobile_app.models.chat.Message;
import com.example.food_ordering_mobile_app.models.chat.MessageResponse;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.ChatService;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository {
    private ChatService chatService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public ChatRepository(Context context) {
        chatService = RetrofitClient.getClient(context).create(ChatService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public LiveData<Resource<Chat>> createChat(String id, String storeId) {
        MutableLiveData<Resource<Chat>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        chatService.createChat(id, storeId).enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ChatRepository", "createChat: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
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
            public void onFailure(Call<Chat> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<ApiResponse<Message>>> sendMessage(String chatId, Map<String, Object> data) {
        MutableLiveData<Resource<ApiResponse<Message>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        Log.d("ChatRepository", "chatId = " + chatId + ", content = " + data);
        chatService.sendMessage(chatId, data).enqueue(new Callback<ApiResponse<Message>>() {
            @Override
            public void onResponse(Call<ApiResponse<Message>> call, Response<ApiResponse<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ChatRepository", "sendMessage: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                        Log.d("ChatRepository", "sendMessage error: " + errorMessage);
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Message>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
                Log.d("ChatRepository", "sendMessage error: " + t.getMessage());
            }
        });

        return result;
    }

    public LiveData<Resource<List<Chat>>> getAllChats() {
        MutableLiveData<Resource<List<Chat>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        chatService.getAllChats().enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ChatRepository", "createChat: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                        Log.d("ChatRepository", "getAllChats error: " + errorMessage);
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Log.d("ChatRepository", "getAllChats error: " + t.getMessage());
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<MessageResponse>> getAllMessages(String chatId) {
        MutableLiveData<Resource<MessageResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        chatService.getAllMessages(chatId).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ChatRepository", "sendMessage: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                        Log.d("ChatRepository", "getAllMessages error: " + errorMessage);
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d("ChatRepository", "getAllMessages error: " + t.getMessage());
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<ApiResponse<String>>> deleteChat(String id) {
        MutableLiveData<Resource<ApiResponse<String>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        chatService.deleteChat(id).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ChatRepository", "createChat: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
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
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<ApiResponse<String>>> deleteMessage(String id) {
        MutableLiveData<Resource<ApiResponse<String>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        chatService.deleteMessage(id).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ChatRepository", "sendMessage: " + response.body());
                    result.setValue(Resource.success("Lay thong tin thành công!", response.body()));
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
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
