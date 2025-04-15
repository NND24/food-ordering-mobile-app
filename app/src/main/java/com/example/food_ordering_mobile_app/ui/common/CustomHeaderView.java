package com.example.food_ordering_mobile_app.ui.common;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.notification.Notification;
import com.example.food_ordering_mobile_app.network.SocketManager;
import com.example.food_ordering_mobile_app.ui.customer.notifications.NotificationActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;
import com.example.food_ordering_mobile_app.viewmodels.NotificationViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomHeaderView extends LinearLayout {
    private TextView textView, unreadNotificationCount;
    private TextView tvName;
    private ImageButton btnNotification, btnCart;
    private TextView cartItemCount;
    private RelativeLayout unreadNotificationBadge, cartItemCountBadge;
    private CartViewModel cartViewModel;
    private NotificationViewModel notificationViewModel;
    private LifecycleOwner lifecycleOwner;
    private boolean isNotificationObserverSet = false;
    private boolean isCartObserverSet = false;

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        setUpUserCart();
        setUpUserNotification();
    }

    public CustomHeaderView(Context context) {
        super(context);
        init(context);
    }

    public CustomHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_custom_header_view, this, true);

        textView = findViewById(R.id.textView);
        tvName = findViewById(R.id.tvName);
        btnNotification = findViewById(R.id.btnNotification);
        btnCart = findViewById(R.id.btnCart);
        cartItemCount = findViewById(R.id.cartItemCount);
        unreadNotificationBadge = findViewById(R.id.unreadNotificationBadge);
        unreadNotificationCount = findViewById(R.id.unreadNotificationCount);
        cartItemCountBadge = findViewById(R.id.cartItemCountBadge);

        cartViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(CartViewModel.class);
        notificationViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NotificationViewModel.class);

        // Khi nhấn vào Notification Button, chuyển đến NotificationActivity
        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationActivity.class);
            context.startActivity(intent);
        });

        // Khi nhấn vào Cart Button, chuyển đến CartActivity
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.food_ordering_mobile_app.ui.customer.cart.CartActivity.class);
            context.startActivity(intent);
        });
    }

    private void setUpUserCart() {
        if (isCartObserverSet) return;

        cartViewModel.getUserCart();
        cartViewModel.getUserCartResponse().observe(lifecycleOwner, new Observer<Resource<ApiResponse<List<Cart>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Cart>>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        updateCartCount(resource.getData().getData());
                        break;
                    case ERROR:
                        updateCartCount(new ArrayList<Cart>());
                        break;
                }
            }
        });
    }

    private void updateCartCount(List<Cart> carts) {
        int cartCount = 0;

        for (Cart cart : carts) {
            cartCount++;
        }

        if (cartCount > 0) {
            cartItemCountBadge.setVisibility(VISIBLE);
            cartItemCount.setText(String.valueOf(cartCount));
        } else {
            cartItemCountBadge.setVisibility(GONE);
        }
    }

    private void setUpUserNotification() {
        if (isNotificationObserverSet) return;  // Đảm bảo chỉ thiết lập observer 1 lần

        // Kết nối socket
        SocketManager.connectSocket(getContext(), notificationViewModel);

        // Đặt observer cho thông báo
        notificationViewModel.getNotificationsResponse().observe(lifecycleOwner , new Observer<Resource<List<Notification>>>() {
            @Override
            public void onChanged(Resource<List<Notification>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        updateUnreadNotificationCount(resource.getData());
                        break;
                    case ERROR:
                        updateUnreadNotificationCount(new ArrayList<Notification>());
                        break;
                }
            }
        });

        // Đánh dấu observer đã được thiết lập
        isNotificationObserverSet = true;
    }

    private void updateUnreadNotificationCount(List<Notification> notifications) {
        int unreadCount = 0;
        Set<String> seenNotificationIds = new HashSet<>(); // Set to track seen notifications

        for (Notification notification : notifications) {
            if ("unread".equals(notification.getStatus()) && !seenNotificationIds.contains(notification.getId())) {
                unreadCount++;
                seenNotificationIds.add(notification.getId()); // Mark this notification as seen
            }
        }

        // Kiểm tra xem số lượng đã thay đổi không
        if (unreadCount != Integer.parseInt(unreadNotificationCount.getText().toString())) {
            if (unreadCount > 0) {
                unreadNotificationBadge.setVisibility(VISIBLE);
                unreadNotificationCount.setText(String.valueOf(unreadCount));
            } else {
                unreadNotificationBadge.setVisibility(GONE);
            }
        }
    }



    // Hàm để set tên vào tvName từ bên ngoài
    public void setName(String name) {
        tvName.setText(name);
    }

    // Bạn cũng có thể tạo getter/setter cho các view khác nếu cần
    public void setText(String text) {
        textView.setText(text);
    }
}
