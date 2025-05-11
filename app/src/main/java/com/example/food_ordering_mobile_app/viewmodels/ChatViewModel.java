package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.chat.Chat;
import com.example.food_ordering_mobile_app.models.chat.Message;
import com.example.food_ordering_mobile_app.models.chat.MessageResponse;
import com.example.food_ordering_mobile_app.repository.ChatRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;
import java.util.Map;

public class ChatViewModel extends AndroidViewModel {
    private final ChatRepository chatRepository;

    private final MutableLiveData<Resource<String>> createChatResponse = new MutableLiveData<>();
    public LiveData<Resource<String>> getCreateChatResponse() {
        return createChatResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<Message>>> sendMessageResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<Message>>> getSendMessageResponse() {
        return sendMessageResponse;
    }
    private final MutableLiveData<Resource<List<Chat>>> allChatsResponse = new MutableLiveData<>();
    public LiveData<Resource<List<Chat>>> getAllChatsResponse() {
        return allChatsResponse;
    }
    private final MutableLiveData<Resource<MessageResponse>> allMessagesResponse = new MutableLiveData<>();
    public LiveData<Resource<MessageResponse>> getAllMessagesResponse() {
        return allMessagesResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> deleteChatResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getDeleteChatResponse() {
        return deleteChatResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<String>>> deleteMessageResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<String>>> getDeleteMessageResponse() {
        return deleteMessageResponse;
    }

    public ChatViewModel(Application application) {
        super(application);
        chatRepository = new ChatRepository(application);
    }

    public void createChat(String id, String storeId) {
        LiveData<Resource<String>> result = chatRepository.createChat(id, storeId);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                createChatResponse.setValue(resource);
            }
        });
    }

    public void sendMessage(String chatId, Map<String, Object> data) {
        LiveData<Resource<ApiResponse<Message>>> result = chatRepository.sendMessage(chatId, data);
        result.observeForever(new Observer<Resource<ApiResponse<Message>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Message>> resource) {
                sendMessageResponse.setValue(resource);
            }
        });
    }

    public void getAllChats() {
        LiveData<Resource<List<Chat>>> result = chatRepository.getAllChats();
        result.observeForever(new Observer<Resource<List<Chat>>>() {
            @Override
            public void onChanged(Resource<List<Chat>> resource) {
                allChatsResponse.setValue(resource);
            }
        });
    }

    public void getAllMessages(String chatId) {
        LiveData<Resource<MessageResponse>> result = chatRepository.getAllMessages(chatId);
        result.observeForever(new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                allMessagesResponse.setValue(resource);
            }
        });
    }

    public void deleteChat(String id) {
        LiveData<Resource<ApiResponse<String>>> result = chatRepository.deleteChat(id);
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                deleteChatResponse.setValue(resource);
            }
        });
    }

    public void deleteMessage(String id) {
        LiveData<Resource<ApiResponse<String>>> result = chatRepository.deleteMessage(id);
        result.observeForever(new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                deleteMessageResponse.setValue(resource);
            }
        });
    }
}
