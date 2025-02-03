package com.example.food_ordering_mobile_app.ui.customer.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.CartAdapter;
import com.example.food_ordering_mobile_app.models.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartList = new ArrayList<>();
        cartList.add(new Cart("Minute by tuk tuk", "Cafe", 4.9, 124, "Western food",String.valueOf(R.drawable.res_1), 2));
        cartList.add(new Cart("Phở Lý Quoc Su", "Mỳ", 3.4, 200, "Món ăn ngon",String.valueOf(R.drawable.item_2), 5));

        cartAdapter = new CartAdapter(this, cartList);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}