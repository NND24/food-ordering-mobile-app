package com.example.food_ordering_mobile_app.ui.customer.store;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.DishBigAdapter;
import com.example.food_ordering_mobile_app.adapters.DishAdapter;
import com.example.food_ordering_mobile_app.adapters.RatingShortAdapter;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.ListRatingResponse;
import com.example.food_ordering_mobile_app.models.dish.Rating;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.store.StoreResponse;
import com.example.food_ordering_mobile_app.ui.customer.cart.CartDetailActivity;
import com.example.food_ordering_mobile_app.ui.customer.dish.DishActivity;
import com.example.food_ordering_mobile_app.ui.customer.rating.RatingActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.DishViewModel;
import com.example.food_ordering_mobile_app.viewmodels.RatingViewModel;
import com.example.food_ordering_mobile_app.viewmodels.StoreViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private StoreViewModel storeViewModel;
    private DishViewModel dishViewModel;
    private RatingViewModel ratingViewModel;
    private RecyclerView dishRecyclerView;
    private DishAdapter dishAdapter;
    private List<Dish> dishList;
    private RecyclerView dishBigRecyclerView;
    private DishBigAdapter dishBigAdapter;
    private List<Dish> dishBigList;
    private RecyclerView reviewRecyclerView;
    private RatingShortAdapter reviewAdapter;
    private List<Rating> reviewList;
    private ImageView ivStoreAvatar, ivStoreCover, ivStar;
    private TextView tvStoreName, tvStoreFoodType, tvAvgRating, tvRatingOpen, tvAmountRating, tvRatingText, tvRatingClose, tvDescription;
    private String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_store);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ivStoreCover = findViewById(R.id.ivStoreCover);
        ivStoreAvatar = findViewById(R.id.ivStoreAvatar);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreFoodType = findViewById(R.id.tvStoreFoodType);
        ivStar = findViewById(R.id.ivStar);
        tvAvgRating = findViewById(R.id.tvAvgRating);
        tvRatingOpen = findViewById(R.id.tvRatingOpen);
        tvAmountRating = findViewById(R.id.tvAmountRating);
        tvRatingText = findViewById(R.id.tvRatingText);
        tvRatingClose = findViewById(R.id.tvRatingClose);
        tvDescription = findViewById(R.id.tvDescription);
        dishBigRecyclerView = findViewById(R.id.dishBigRecyclerView);
        dishRecyclerView = findViewById(R.id.dishRecyclerView);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);

        storeId = getIntent().getStringExtra("storeId");

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);
        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        ratingViewModel = new ViewModelProvider(this).get(RatingViewModel.class);

        setupStore();
        setupBigDish();
//        setupDish();
//        setupShortRating();
    }

    private void setupStore() {
        storeViewModel.getStoreInformation(storeId);
        storeViewModel.getStoreInformationResponse().observe(this, new Observer<Resource<StoreResponse>>() {
            @Override
            public void onChanged(Resource<StoreResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("StoreActivity", "setupStore: " + resource.getData().toString());
                        tvStoreName.setText(resource.getData().getData().getName());
                        tvDescription.setText(resource.getData().getData().getDescription());
                        if (resource.getData().getData().getAmountRating() != null && resource.getData().getData().getAmountRating() > 0) {
                            tvAvgRating.setText(String.format("%.2f", resource.getData().getData().getAvgRating()));
                            tvAmountRating.setText(String.valueOf(resource.getData().getData().getAmountRating()));
                            tvAvgRating.setVisibility(View.VISIBLE);
                            tvAmountRating.setVisibility(View.VISIBLE);
                            ivStar.setVisibility(View.VISIBLE);
                            tvRatingOpen.setVisibility(View.VISIBLE);
                            tvRatingText.setVisibility(View.VISIBLE);
                            tvRatingClose.setVisibility(View.VISIBLE);
                        } else {
                            tvAvgRating.setVisibility(View.GONE);
                            tvAmountRating.setVisibility(View.GONE);
                            ivStar.setVisibility(View.GONE);
                            tvRatingOpen.setVisibility(View.GONE);
                            tvRatingText.setVisibility(View.GONE);
                            tvRatingClose.setVisibility(View.GONE);
                        }

                        if (resource.getData().getData().getStoreCategory() != null && !resource.getData().getData().getStoreCategory().isEmpty()) {
                            SpannableStringBuilder categories = new SpannableStringBuilder();
                            for (int i = 0; i < resource.getData().getData().getStoreCategory().size(); i++) {
                                FoodType type = resource.getData().getData().getStoreCategory().get(i);
                                categories.append(type.getName());

                                if (i < resource.getData().getData().getStoreCategory().size() - 1) {
                                    categories.append(" • ");
                                }
                            }
                            tvStoreFoodType.setText(categories);
                        } else {
                            tvStoreFoodType.setText("");
                        }

                        String storeAvatarUrl = resource.getData().getData().getAvatar() != null ? resource.getData().getData().getAvatar().getUrl() : null;
                        Glide.with(ivStoreAvatar)
                                .asBitmap()
                                .load(storeAvatarUrl)
                                .override(95, 95)
                                .centerCrop()
                                .into(new BitmapImageViewTarget(ivStoreAvatar) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable roundedDrawable =
                                                RoundedBitmapDrawableFactory.create(ivStoreAvatar.getResources(), resource);
                                        roundedDrawable.setCornerRadius(6);
                                        ivStoreAvatar.setImageDrawable(roundedDrawable);
                                    }
                                });

                        String storeCoverUrl = resource.getData().getData().getCover() != null ? resource.getData().getData().getCover().getUrl() : null;
                        Glide.with(ivStoreCover)
                                .asBitmap()
                                .load(storeCoverUrl)
                                .into(new BitmapImageViewTarget(ivStoreCover) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable roundedDrawable =
                                                RoundedBitmapDrawableFactory.create(ivStoreCover.getResources(), resource);
                                        roundedDrawable.setCornerRadius(0);
                                        ivStoreCover.setImageDrawable(roundedDrawable);
                                    }
                                });

                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("StoreActivity", "setupStore error: " + resource.getData().toString());
                        break;
                }
            }
        });
    }

    private void setupBigDish() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        dishBigRecyclerView.setLayoutManager(gridLayoutManager);
        dishBigAdapter = new DishBigAdapter(this, dishBigList, dishBig -> {
            Intent intent = new Intent(this, DishActivity.class);
            startActivity(intent);
        });
        dishBigList = new ArrayList<>();
        dishBigRecyclerView.setAdapter(dishBigAdapter);

        dishViewModel.getAllBigDishResponse().observe(this, new Observer<Resource<List<Dish>>>() {
            @Override
            public void onChanged(Resource<List<Dish>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        dishBigList.clear();
                        dishBigList.addAll(resource.getData());
                        Log.d("StoreActivity", "getAllBigDishResponse: " + resource.getData().toString());
                        dishBigAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("StoreActivity", "getAllBigDishResponse error: " + resource.getData().toString());
                        break;
                }
            }
        });

        dishViewModel.getAllBigDish(storeId);
    }

    private void setupDish() {
        dishRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dishList = new ArrayList<>();
        dishAdapter = new DishAdapter(this, dishList, dish -> {
            Intent intent = new Intent(this, DishActivity.class);
            startActivity(intent);
        });
        dishRecyclerView.setAdapter(dishAdapter);

        dishViewModel.getAllDishResponse().observe(this, new Observer<Resource<List<Dish>>>() {
            @Override
            public void onChanged(Resource<List<Dish>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        dishList.clear();
                        dishList.addAll(resource.getData());
                        Log.d("StoreActivity", "getAllDishResponse: " + resource.getData().toString());
                        dishAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("StoreActivity", "getAllDishResponse Error: " + resource.getData().toString());
                        break;
                }
            }
        });

        dishViewModel.getAllDish(storeId);
    }

    private void setupShortRating() {
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        reviewList = new ArrayList<>();
        reviewAdapter = new RatingShortAdapter(this, reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);

        ratingViewModel.getAllStoreRatingResponse().observe(this, new Observer<Resource<ListRatingResponse>>() {
            @Override
            public void onChanged(Resource<ListRatingResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        reviewList.clear();
                        reviewList.addAll(resource.getData().getData());
                        Log.d("StoreActivity", "getAllStoreRatingResponse: " + resource.getData().toString());
                        reviewAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("StoreActivity", "getAllStoreRatingResponse Error: " + resource.getData().toString());
                        break;
                }
            }
        });

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("sort", "");
        queryParams.put("limit", "3");
        queryParams.put("page", "1");

        ratingViewModel.getAllStoreRating(storeId, queryParams);
    }

    private void refreshData() {
        storeViewModel.getStoreInformation(storeId);
        dishViewModel.getAllBigDish(storeId);
        dishViewModel.getAllDish(storeId);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("sort", "");
        queryParams.put("limit", "3");
        queryParams.put("page", "1");

        ratingViewModel.getAllStoreRating(storeId, queryParams);
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void goToReviewPage(View view) {
        Intent intent = new Intent(StoreActivity.this, RatingActivity.class);
        startActivity(intent);
    }

    public void goToCartDetailPage(View view) {
        Intent intent = new Intent(StoreActivity.this, CartDetailActivity.class);
        startActivity(intent);
    }
}