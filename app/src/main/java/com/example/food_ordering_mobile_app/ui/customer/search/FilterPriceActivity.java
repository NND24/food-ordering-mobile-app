package com.example.food_ordering_mobile_app.ui.customer.search;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_mobile_app.R;

public class FilterPriceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter_price);
    }

    public void closeFilter(View view) {
        finish();
    }
}