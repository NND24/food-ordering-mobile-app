package com.example.food_ordering_mobile_app.ui.customer.messages;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.MessageAdapter;
import com.example.food_ordering_mobile_app.adapters.NotificationAdapter;
import com.example.food_ordering_mobile_app.models.chat.Chat;
import com.example.food_ordering_mobile_app.models.chat.Message;
import com.example.food_ordering_mobile_app.models.chat.MessageResponse;
import com.example.food_ordering_mobile_app.models.notification.ListNotificationResponse;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.ChatViewModel;
import com.example.food_ordering_mobile_app.viewmodels.NotificationViewModel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DetailMessageActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChatViewModel chatViewModel;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private RecyclerView chatRecyclerView;
    private EditText editTextMessage;
    private ImageView sendMessage, ivUserAvatar;
    private TextView tvUserName, tvTime;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_message);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        tvUserName = findViewById(R.id.tvUserName);
        //tvTime = findViewById(R.id.tvTime);
        editTextMessage = findViewById(R.id.editTextMessage);
        sendMessage = findViewById(R.id.sendMessage);

        Intent intent = getIntent();
        if (intent != null) {
            chatId = intent.getStringExtra("chatId") != null ? intent.getStringExtra("chatId") : "";
        }

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        setupAllMessages();
    }

    private void setupAllMessages() {
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        chatRecyclerView.setAdapter(messageAdapter);

        chatViewModel.getAllMessagesResponse().observe(this, new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        messageList.clear();
                        messageList.addAll(resource.getData().getMessages());

                        tvUserName.setText(resource.getData().getChat().getUsers().get(1).getName());

//                        long timeDiff = System.currentTimeMillis() - resource.getData().getChat().getLatestMessage().getUpdatedAt().getTime();
//                        tvTime.setText(formatTimeAgo(timeDiff));

                        String avatarUrl = resource.getData().getChat().getUsers().get(1).getAvatar() != null
                                ? resource.getData().getChat().getUsers().get(1).getAvatar().getUrl()
                                : null;
                        loadRoundedImage(avatarUrl, ivUserAvatar);

                        messageAdapter.notifyDataSetChanged();
                        Log.d("ChatFragment", "getAllMessagesResponse: " + resource.getData().toString());
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("ChatFragment", "getAllMessagesResponse Error: " + resource.getMessage());
                        break;
                }
            }
        });

        chatViewModel.getAllMessages(chatId);
    }

    private void refreshData() {
        chatViewModel.getAllMessages(chatId);
    }

    private String formatTimeAgo(long timeDiff) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
        long days = TimeUnit.MILLISECONDS.toDays(timeDiff);

        if (seconds < 60) return seconds + " giây trước";
        if (minutes < 60) return minutes + " phút trước";
        if (hours < 24) return hours + " giờ trước";
        return days + " ngày trước";
    }

    private void loadRoundedImage(String url, ImageView imageView) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .override(50, 50)
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
                        roundedDrawable.setCornerRadius(6);
                        imageView.setImageDrawable(roundedDrawable);
                    }
                });
    }


    public void goBack(View view) {
        onBackPressed();
    }
}