package com.example.food_ordering_mobile_app.ui.customer.coupons;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.CouponAdapter;
import com.example.food_ordering_mobile_app.models.Coupon;

import java.util.ArrayList;
import java.util.List;

public class CouponsActivity extends AppCompatActivity {
    private RecyclerView couponRecyclerView;
    private CouponAdapter couponAdapter;
    private List<Coupon> couponList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_coupons);

        couponRecyclerView = findViewById(R.id.couponRecyclerView);
        couponRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        couponList = new ArrayList<>();
        couponList.add(new Coupon("Giam gia sieu ngon", "Giam 1k moi mon", String.valueOf(R.drawable.item_1)));
        couponList.add(new Coupon("Giam gia gio vang", "Khong giam gi het", String.valueOf(R.drawable.item_2)));

        couponAdapter = new CouponAdapter(this, couponList);
        couponRecyclerView.setAdapter(couponAdapter);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}