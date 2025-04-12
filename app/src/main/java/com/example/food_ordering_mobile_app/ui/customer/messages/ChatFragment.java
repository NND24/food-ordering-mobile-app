package com.example.food_ordering_mobile_app.ui.customer.messages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.ChatAdapter;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.chat.Chat;
import com.example.food_ordering_mobile_app.ui.common.CustomHeaderView;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChatViewModel chatViewModel;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList;
    private CustomHeaderView customHeaderView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        chatRecyclerView = view.findViewById(R.id.messageRecyclerView);
        customHeaderView = view.findViewById(R.id.customHeaderView);

        customHeaderView.setLifecycleOwner(getViewLifecycleOwner());
        customHeaderView.setText("Tin nhắn");

        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        setupUserChat();

        chatViewModel.getDeleteChatResponse().observe(getViewLifecycleOwner(), new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        setupUserChat();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        return view;
    }

    private void setupUserChat() {
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(requireActivity(),getContext(), chatList, chat -> {
            Intent intent = new Intent(requireContext(), DetailMessageActivity.class);
            intent.putExtra("chatId", chat.getId());
            startActivity(intent);
        });
        chatRecyclerView.setAdapter(chatAdapter);

        chatViewModel.getAllChatsResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<Chat>>>() {
            @Override
            public void onChanged(Resource<List<Chat>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        chatList.clear();
                        chatList.addAll(resource.getData());
                        chatAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        chatViewModel.getAllChats();
    }

    private void refreshData() {
        chatViewModel.getAllChats();
    }
}