package com.example.food_ordering_mobile_app.ui.store.notifications;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.NotificationAdapter;
import com.example.food_ordering_mobile_app.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);


        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationList = new ArrayList<>();
        notificationList.add(new Notification("Your order has been delivered", "1h ago", false));
        notificationList.add(new Notification("Your order has been delivered", "1h ago", true));
        notificationList.add(new Notification("Your order has been delivered", "1h ago", true));
        notificationList.add(new Notification("Your order has been delivered", "1h ago", true));
        notificationList.add(new Notification("Your order has been delivered", "1h ago", false));

        notificationAdapter = new NotificationAdapter(this, notificationList);
        notificationRecyclerView.setAdapter(notificationAdapter);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}