package com.example.food_ordering_mobile_app.ui.customer.search;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_mobile_app.R;

public class FilterRestaurantOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter_restaurant_options);
    }

    public void closeFilter(View view) {
        onBackPressed();
    }
}