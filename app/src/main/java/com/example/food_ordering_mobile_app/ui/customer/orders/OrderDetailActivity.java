package com.example.food_ordering_mobile_app.ui.customer.orders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OrderSummaryAdapter;
import com.example.food_ordering_mobile_app.models.order.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private RecyclerView orderSummaryRecyclerView;
    private OrderSummaryAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);

        ImageView restaurantAvatar = findViewById(R.id.restaurantAvatar);
        int cornerRadius = (int) getResources().getDisplayMetrics().density * 8;

        Glide.with(this)
                .load(R.drawable.item_1)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(cornerRadius)))
                .into(restaurantAvatar);

        ImageView shipperAvatar = findViewById(R.id.shipperAvatar);
        int shipperAvatarCornerRadius = (int) getResources().getDisplayMetrics().density * 8; // 8dp to px

        Glide.with(this)
                .load(R.drawable.item_1)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(shipperAvatarCornerRadius))) // Áp dụng bo góc
                .into(shipperAvatar);

        orderSummaryRecyclerView = findViewById(R.id.orderSummaryRecyclerView);
        orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
//        orderList.add(new Order("Pizza", 2, 120000, Arrays.asList("Phô mai", "Xúc xích")));
//        orderList.add(new Order("Gà rán", 3, 50000, Arrays.asList("Tương ớt", "Khoai tây chiên")));
//        orderList.add(new Order("Mì cay", 1, 70000, new ArrayList<>()));

        orderAdapter = new OrderSummaryAdapter(this, orderList);
        orderSummaryRecyclerView.setAdapter(orderAdapter);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}