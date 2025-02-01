package com.example.food_ordering_mobile_app.ui.customer.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.DishBigAdapter;
import com.example.food_ordering_mobile_app.adapters.RestaurantBigAdapter;
import com.example.food_ordering_mobile_app.adapters.DishAdapter;
import com.example.food_ordering_mobile_app.adapters.ReviewShortAdapter;
import com.example.food_ordering_mobile_app.models.Dish;
import com.example.food_ordering_mobile_app.models.Restaurant;
import com.example.food_ordering_mobile_app.models.Review;
import com.example.food_ordering_mobile_app.ui.common.LoginActivity;
import com.example.food_ordering_mobile_app.ui.common.RegisterActivity;
import com.example.food_ordering_mobile_app.ui.customer.cart.CartDetailActivity;
import com.example.food_ordering_mobile_app.ui.customer.dish.DishActivity;
import com.example.food_ordering_mobile_app.ui.customer.review.ReviewActivity;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {
    private RecyclerView dishRecyclerView;
    private DishAdapter dishAdapter;
    private List<Dish> dishList;
    private RecyclerView dishBigRecyclerView;
    private DishBigAdapter dishBigAdapter;
    private List<Dish> dishBigList;
    private RecyclerView reviewRecyclerView;
    private ReviewShortAdapter reviewAdapter;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant);

        ImageView restaurantAvatar = findViewById(R.id.restaurantAvatar);
        int cornerRadius = (int) getResources().getDisplayMetrics().density * 8; // 8dp to px

        Glide.with(this)
                .load(R.drawable.item_1)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(cornerRadius))) // Áp dụng bo góc
                .into(restaurantAvatar);

        dishBigRecyclerView = findViewById(R.id.dishBigRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        dishBigRecyclerView.setLayoutManager(gridLayoutManager);

        dishBigList = new ArrayList<>();
        dishBigList.add(new Dish("Minute by tuk tuk", "Sợi mỳ dai ngon", 10000, String.valueOf(R.drawable.item_1)));
        dishBigList.add(new Dish("Phở Lý Quoc Su", "Bo ngon", 15000, String.valueOf(R.drawable.item_2)));
        dishBigList.add(new Dish("Phở Lý Quoc Su", "Bo ngon", 15000, String.valueOf(R.drawable.item_2)));
        dishBigList.add(new Dish("Phở Lý Quoc Su", "Bo ngon", 15000, String.valueOf(R.drawable.item_2)));
        dishBigList.add(new Dish("Phở Lý Quoc Su", "Bo ngon", 15000, String.valueOf(R.drawable.item_2)));
        dishBigList.add(new Dish("Phở Lý Quoc Su", "Bo ngon", 15000, String.valueOf(R.drawable.item_2)));

        dishBigAdapter = new DishBigAdapter(this, dishBigList, dishBig -> {
            Intent intent = new Intent(this, DishActivity.class);
            startActivity(intent);
        });
        dishBigRecyclerView.setAdapter(dishBigAdapter);

        // Show normal dish
        dishRecyclerView = findViewById(R.id.dishRecyclerView);
        dishRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dishList = new ArrayList<>();
        dishList.add(new Dish("Minute by tuk tuk", "Sợi mỳ dai ngon", 10000, String.valueOf(R.drawable.item_1)));
        dishList.add(new Dish("Phở Lý Quoc Su", "Bo ngon", 15000, String.valueOf(R.drawable.item_2)));

        dishAdapter = new DishAdapter(this, dishList, dish -> {
            Intent intent = new Intent(this, DishActivity.class);
            startActivity(intent);
        });
        dishRecyclerView.setAdapter(dishAdapter);

        // Show short review
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        reviewList = new ArrayList<>();
        reviewList.add(new Review("Dat Nguyen", 4.9, "Sợi mỳ dai ngon"));
        reviewList.add(new Review( "Bao Pham", 3.5, "Bo ngon"));

        reviewAdapter = new ReviewShortAdapter(this, reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void goToReviewPage(View view) {
        Intent intent = new Intent(RestaurantActivity.this, ReviewActivity.class);
        startActivity(intent);
    }

    public void goToCartDetailPage(View view) {
        Intent intent = new Intent(RestaurantActivity.this, CartDetailActivity.class);
        startActivity(intent);
    }
}