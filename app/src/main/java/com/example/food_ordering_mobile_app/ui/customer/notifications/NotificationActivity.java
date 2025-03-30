package com.example.food_ordering_mobile_app.ui.customer.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.NotificationAdapter;
import com.example.food_ordering_mobile_app.models.notification.ListNotificationResponse;
import com.example.food_ordering_mobile_app.models.notification.Notification;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.NotificationViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private NotificationViewModel notificationViewModel;
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        setupUserNotification();
    }

    private void setupUserNotification() {
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        notificationList = new ArrayList<>();

        notificationAdapter = new NotificationAdapter(this, notificationList);
        notificationRecyclerView.setAdapter(notificationAdapter);

        notificationViewModel.getAllNotificationsResponse().observe(this, new Observer<Resource<ListNotificationResponse>>() {
            @Override
            public void onChanged(Resource<ListNotificationResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        notificationList.clear();
                        notificationList.addAll(resource.getData().getData());
                        notificationAdapter.notifyDataSetChanged();
                        Log.d("NotificationActivity", "getAllNotifications: " + resource.getData().getData().toString());
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("NotificationActivity", "getAllNotifications Error: " + resource.getMessage());
                        break;
                }
            }
        });

        notificationViewModel.getAllNotifications();
    }

    public void goBack(View view) {
        onBackPressed();
    }
}