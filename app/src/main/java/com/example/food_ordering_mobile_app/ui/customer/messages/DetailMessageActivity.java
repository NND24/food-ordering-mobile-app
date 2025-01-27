package com.example.food_ordering_mobile_app.ui.customer.messages;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.CategoryAdapter;
import com.example.food_ordering_mobile_app.adapters.ChatAdapter;
import com.example.food_ordering_mobile_app.models.Message;

import java.util.ArrayList;
import java.util.List;

public class DetailMessageActivity extends AppCompatActivity {
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private RecyclerView chatRecyclerView;
    private EditText editTextMessage;
    private ImageView sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_message);

        ImageView avatar = findViewById(R.id.avatar);

        Glide.with(this)
                .load(R.drawable.dess_1)
                .circleCrop()
                .into(avatar);

        editTextMessage = findViewById(R.id.editTextMessage);
        sendMessage = findViewById(R.id.sendMessage);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        messageList = new ArrayList<>();
        messageList.add(new Message("Minute by tuk tuk", "Cafe may muong duong", "13:35", String.valueOf(R.drawable.item_1), false));
        messageList.add(new Message("Minute by tuk tuk", "OK", "13:35", String.valueOf(R.drawable.item_1), true));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));
        messageList.add(new Message("Phở Lý Quoc Su", "Mỳ bo hanh khong", "20:00", String.valueOf(R.drawable.item_2), false));

        chatAdapter = new ChatAdapter(this, messageList);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}