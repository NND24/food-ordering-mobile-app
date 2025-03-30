package com.example.food_ordering_mobile_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.chat.Chat;
import com.example.food_ordering_mobile_app.models.chat.Message;
import com.example.food_ordering_mobile_app.models.chat.MessageResponse;
import com.example.food_ordering_mobile_app.repository.ChatRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private final ChatRepository chatRepository;

    private final MutableLiveData<Resource<Chat>> createChatResponse = new MutableLiveData<>();
    public LiveData<Resource<Chat>> getCreateChatResponse() {
        return createChatResponse;
    }
    private final MutableLiveData<Resource<Message>> sendMessageResponse = new MutableLiveData<>();
    public LiveData<Resource<Message>> getSendMessageResponse() {
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
    private final MutableLiveData<Resource<Chat>> deleteChatResponse = new MutableLiveData<>();
    public LiveData<Resource<Chat>> getDeleteChatResponse() {
        return deleteChatResponse;
    }
    private final MutableLiveData<Resource<Message>> deleteMessageResponse = new MutableLiveData<>();
    public LiveData<Resource<Message>> getDeleteMessageResponse() {
        return deleteMessageResponse;
    }

    public ChatViewModel(Application application) {
        super(application);
        chatRepository = new ChatRepository(application);
    }

    public void createChat(String id) {
        LiveData<Resource<Chat>> result = chatRepository.createChat(id);
        result.observeForever(new Observer<Resource<Chat>>() {
            @Override
            public void onChanged(Resource<Chat> resource) {
                Log.d("ChatViewModel", "createChat: " + resource);
                createChatResponse.setValue(resource);
            }
        });
    }

    public void sendMessage(String id, Message message) {
        LiveData<Resource<Message>> result = chatRepository.sendMessage(id,message);
        result.observeForever(new Observer<Resource<Message>>() {
            @Override
            public void onChanged(Resource<Message> resource) {
                Log.d("ChatViewModel", "sendMessage: " + resource);
                sendMessageResponse.setValue(resource);
            }
        });
    }

    public void getAllChats() {
        LiveData<Resource<List<Chat>>> result = chatRepository.getAllChats();
        result.observeForever(new Observer<Resource<List<Chat>>>() {
            @Override
            public void onChanged(Resource<List<Chat>> resource) {
                Log.d("ChatViewModel", "getAllChats: " + resource);
                allChatsResponse.setValue(resource);
            }
        });
    }

    public void getAllMessages(String chatId) {
        LiveData<Resource<MessageResponse>> result = chatRepository.getAllMessages(chatId);
        result.observeForever(new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                Log.d("ChatViewModel", "getAllMessages: " + resource);
                allMessagesResponse.setValue(resource);
            }
        });
    }

    public void deleteChat(String id) {
        LiveData<Resource<Chat>> result = chatRepository.deleteChat(id);
        result.observeForever(new Observer<Resource<Chat>>() {
            @Override
            public void onChanged(Resource<Chat> resource) {
                Log.d("ChatViewModel", "deleteChat: " + resource);
                deleteChatResponse.setValue(resource);
            }
        });
    }

    public void deleteMessage(String id) {
        LiveData<Resource<Message>> result = chatRepository.deleteMessage(id);
        result.observeForever(new Observer<Resource<Message>>() {
            @Override
            public void onChanged(Resource<Message> resource) {
                Log.d("ChatViewModel", "deleteMessage: " + resource);
                deleteMessageResponse.setValue(resource);
            }
        });
    }
}
