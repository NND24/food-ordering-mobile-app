package com.example.food_ordering_mobile_app.ui.customer.messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.FavoriteAdapter;
import com.example.food_ordering_mobile_app.adapters.MessageAdapter;
import com.example.food_ordering_mobile_app.models.Message;
import com.example.food_ordering_mobile_app.models.Restaurant;
import com.example.food_ordering_mobile_app.ui.customer.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        messageRecyclerView = view.findViewById(R.id.messageRecyclerView);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        messageList = new ArrayList<>();
        messageList.add(new Message("Minute by tuk tuk", "Cafe may muong duong", "13:35", String.valueOf(R.drawable.item_1), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));

        messageAdapter = new MessageAdapter(getContext(), messageList, message -> {
            Intent intent = new Intent(requireContext(), DetailMessageActivity.class);
            startActivity(intent);
        });
        messageRecyclerView.setAdapter(messageAdapter);

        return view;
    }
}