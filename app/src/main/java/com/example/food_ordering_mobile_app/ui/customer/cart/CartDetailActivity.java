package com.example.food_ordering_mobile_app.ui.customer.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OrderSummaryAdapter;
import com.example.food_ordering_mobile_app.models.Order;
import com.example.food_ordering_mobile_app.ui.customer.coupons.CouponsActivity;
import com.example.food_ordering_mobile_app.ui.customer.dish.DishActivity;
import com.example.food_ordering_mobile_app.ui.customer.orders.OrderDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartDetailActivity extends AppCompatActivity {
    private RecyclerView orderSummaryRecyclerView;
    private OrderSummaryAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_detail);

        orderSummaryRecyclerView = findViewById(R.id.orderSummaryRecyclerView);
        orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderList.add(new Order("Pizza", 2, 120000, Arrays.asList("Phô mai", "Xúc xích")));
        orderList.add(new Order("Gà rán", 3, 50000, Arrays.asList("Tương ớt", "Khoai tây chiên")));
        orderList.add(new Order("Mì cay", 1, 70000, new ArrayList<>()));

        orderAdapter = new OrderSummaryAdapter(this, orderList);
        orderSummaryRecyclerView.setAdapter(orderAdapter);
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void goToOrderDetail(View view) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        startActivity(intent);
    }

    public void goToCouponPage(View view) {
        Intent intent = new Intent(this, CouponsActivity.class);
        startActivity(intent);
    }
}