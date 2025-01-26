package com.example.food_ordering_mobile_app.ui.customer.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.CategoryAdapter;
import com.example.food_ordering_mobile_app.adapters.RestaurantAdapter;
import com.example.food_ordering_mobile_app.models.Category;
import com.example.food_ordering_mobile_app.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private RecyclerView categoryRecyclerView;
    private RecyclerView restaurantRecyclerView;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        categoryList = new ArrayList<>();
        categoryList.add(new Category(String.valueOf(R.drawable.cat_3), "Cơm"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_4), "Pho"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_offer), "Banh Mi"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bun"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bun"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bun"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bun"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bun"));

        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        restaurantRecyclerView = findViewById(R.id.restaurantRecyclerView);
        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Use LinearLayoutManager

        restaurantList = new ArrayList<>();
        // Add your restaurant data to restaurantList
        restaurantList.add(new Restaurant("Minute by tuk tuk", "Cafe", 4.9, 124, "Western food", String.valueOf(R.drawable.item_1)));
        restaurantList.add(new Restaurant("Phở Lý Quoc Su", "Mỳ", 3.4, 200, "Món ăn ngon", String.valueOf(R.drawable.item_2)));

        restaurantAdapter = new RestaurantAdapter(this, restaurantList);
        restaurantRecyclerView.setAdapter(restaurantAdapter);
    }

    public void openFilterAll(View view) {
        Intent intent = new Intent(SearchActivity.this, FilterAllActivity.class);
        startActivity(intent);
        finish();
    }

    public void openFilterBy(View view) {
        Intent intent = new Intent(SearchActivity.this, FilterByActivity.class);
        startActivity(intent);
        finish();
    }

    public void openFilterRestaurantOptions(View view) {
        Intent intent = new Intent(SearchActivity.this, FilterRestaurantOptionsActivity.class);
        startActivity(intent);
        finish();
    }

    public void openFilterDeliveryFee(View view) {
        Intent intent = new Intent(SearchActivity.this, FilterDeliveryFeeActivity.class);
        startActivity(intent);
        finish();
    }

    public void openFilterPrice(View view) {
        Intent intent = new Intent(SearchActivity.this, FilterPriceActivity.class);
        startActivity(intent);
        finish();
    }
}