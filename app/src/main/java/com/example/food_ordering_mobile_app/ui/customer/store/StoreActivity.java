package com.example.food_ordering_mobile_app.ui.customer.store;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.DishBigAdapter;
import com.example.food_ordering_mobile_app.adapters.DishAdapter;
import com.example.food_ordering_mobile_app.adapters.DishGroupByCategoryAdapter;
import com.example.food_ordering_mobile_app.adapters.FavoriteAdapter;
import com.example.food_ordering_mobile_app.adapters.RatingShortAdapter;
import com.example.food_ordering_mobile_app.models.MessageResponse;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.cart.CartItem;
import com.example.food_ordering_mobile_app.models.cart.ListCartResponse;
import com.example.food_ordering_mobile_app.models.dish.DishGroupByCategory;
import com.example.food_ordering_mobile_app.models.dish.DishStore;
import com.example.food_ordering_mobile_app.models.dish.ListDishResponse;
import com.example.food_ordering_mobile_app.models.dish.Topping;
import com.example.food_ordering_mobile_app.models.favorite.Favorite;
import com.example.food_ordering_mobile_app.models.favorite.FavoriteResponse;
import com.example.food_ordering_mobile_app.models.rating.ListRatingResponse;
import com.example.food_ordering_mobile_app.models.rating.Rating;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.store.StoreResponse;
import com.example.food_ordering_mobile_app.ui.customer.cart.CartDetailActivity;
import com.example.food_ordering_mobile_app.ui.customer.dish.DishActivity;
import com.example.food_ordering_mobile_app.ui.customer.rating.RatingActivity;
import com.example.food_ordering_mobile_app.utils.Functions;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;
import com.example.food_ordering_mobile_app.viewmodels.DishViewModel;
import com.example.food_ordering_mobile_app.viewmodels.FavoriteViewModel;
import com.example.food_ordering_mobile_app.viewmodels.RatingViewModel;
import com.example.food_ordering_mobile_app.viewmodels.StoreViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private StoreViewModel storeViewModel;
    private DishViewModel dishViewModel;
    private RatingViewModel ratingViewModel;
    private FavoriteViewModel favoriteViewModel;
    private CartViewModel cartViewModel;
    private RecyclerView dishRecyclerView;
    private DishGroupByCategoryAdapter dishGroupByCategoryAdapter;
    private List<DishGroupByCategory> dishGroupByCategoryList;
    private RecyclerView dishBigRecyclerView;
    private DishBigAdapter dishBigAdapter;
    private List<DishStore> dishBigList;
    private RecyclerView reviewRecyclerView;
    private RatingShortAdapter reviewAdapter;
    private List<Rating> reviewList;
    private List<Store> favoriteList;
    private ImageView ivStoreAvatar, ivStoreCover, ivStar;
    private ImageButton btnFavorite;
    private TextView tvStoreName, tvStoreFoodType, tvAvgRating, tvRatingOpen, tvAmountRating, tvRatingText, tvRatingClose, tvDescription, tvDot, tvTotalPrice, tvTotalQuantity;
    private String storeId;
    private Cart currentCart = null;
    private LinearLayout footer;

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
        tvDot = findViewById(R.id.tvDot);
        btnFavorite = findViewById(R.id.btnFavorite);
        dishBigRecyclerView = findViewById(R.id.dishBigRecyclerView);
        dishRecyclerView = findViewById(R.id.dishRecyclerView);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        footer = findViewById(R.id.footer);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);

        storeId = getIntent().getStringExtra("storeId");

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);
        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        ratingViewModel = new ViewModelProvider(this).get(RatingViewModel.class);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        setupStore();
        setupBigDish();
        setupDish();
        setupShortRating();
        setupUserFavorite();
        setupUserCart();

        favoriteViewModel.getRemoveFavoriteResponse().observe(this, new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        setupUserFavorite();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        favoriteViewModel.getAddFavoriteResponse().observe(this, new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        setupUserFavorite();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
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
                            tvDot.setVisibility(View.VISIBLE);
                        } else {
                            tvAvgRating.setVisibility(View.GONE);
                            tvAmountRating.setVisibility(View.GONE);
                            ivStar.setVisibility(View.GONE);
                            tvRatingOpen.setVisibility(View.GONE);
                            tvRatingText.setVisibility(View.GONE);
                            tvRatingClose.setVisibility(View.GONE);
                            tvDot.setVisibility(View.GONE);
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
                        break;
                }
            }
        });
    }

    private void setupBigDish() {
        dishBigRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dishBigList = new ArrayList<>();
        dishBigAdapter = new DishBigAdapter(this,this, dishBigList, dishBig -> {
            Intent intent = new Intent(this, DishActivity.class);
            intent.putExtra("dishId", dishBig.getId());
            startActivity(intent);
        });
        dishBigRecyclerView.setAdapter(dishBigAdapter);

        dishViewModel.getAllBigDishResponse().observe(this, new Observer<Resource<ListDishResponse>>() {
            @Override
            public void onChanged(Resource<ListDishResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        dishBigList.clear();
                        dishBigList.addAll(resource.getData().getData());
                        Log.d("StoreActivity", "getAllBigDishResponse: " + resource.getData().getData().toString());
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
        dishGroupByCategoryList = new ArrayList<>();
        dishGroupByCategoryAdapter = new DishGroupByCategoryAdapter(this, dishGroupByCategoryList);
        dishRecyclerView.setAdapter(dishGroupByCategoryAdapter);

        dishViewModel.getAllDishResponse().observe(this, new Observer<Resource<ListDishResponse>>() {
            @Override
            public void onChanged(Resource<ListDishResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        dishGroupByCategoryList.clear();
                        List<DishStore> dishes = resource.getData().getData();
                        dishGroupByCategoryList.addAll(Functions.groupDishesByCategory(dishes));
                        Log.d("StoreActivity", "setupDish: " + resource.getData().toString());
                        dishGroupByCategoryAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("StoreActivity", "setupDish Error: " + resource.getData().toString());
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
                        reviewAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("sort", "desc");
        queryParams.put("limit", "3");
        queryParams.put("page", "1");

        ratingViewModel.getAllStoreRating(storeId, queryParams);
    }

    private void setupUserFavorite() {
        favoriteViewModel.getUserFavorite();
        favoriteViewModel.getUserFavoriteResponse().observe(this, new Observer<Resource<FavoriteResponse>>() {
            @Override
            public void onChanged(Resource<FavoriteResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        List<Store> stores = resource.getData().getData().getStore();
                        favoriteList.clear();
                        favoriteList.addAll(stores);

                        boolean isFavorite = false;

                        for (Store store : stores) {
                            if (store.getId().equals(storeId)) {
                                isFavorite = true;
                                break;
                            }
                        }

                        if (isFavorite) {
                            btnFavorite.setImageResource(R.drawable.ic_favorite_active_24); // icon active
                            btnFavorite.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    favoriteViewModel.removeFavorite(storeId);
                                }
                            });
                        } else {
                            btnFavorite.setImageResource(R.drawable.ic_favorite_white_24); // icon mặc định
                            btnFavorite.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    favoriteViewModel.addFavorite(storeId);
                                }
                            });
                        }
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
    }

    private void setupUserCart() {
        cartViewModel.getUserCart();
        cartViewModel.getUserCartResponse().observe(this, new Observer<Resource<ListCartResponse>>() {
            @Override
            public void onChanged(Resource<ListCartResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        List<Cart> cartList = resource.getData().getData();

                        for (Cart cart : cartList) {
                            if (cart.getStore() != null && storeId.equals(cart.getStore().getId())) {
                                currentCart = cart;
                                break;
                            }
                        }

                        boolean hasCart = currentCart != null;
                        footer.setVisibility(hasCart ? View.VISIBLE : View.GONE);
                        swipeRefreshLayout.setPadding(0, 0, 0, hasCart ? 0 : 70);

                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        currentCart = null;
                        break;
                }
            }
        });
    }

    private void calculateCartPrice(Cart currentCart) {
        double totalPrice = 0;
        int totalQuantity = 0;

        if (currentCart != null && currentCart.getItems() != null) {
            for (CartItem item : currentCart.getItems()) {
                double dishPrice = (item.getDish() != null ? item.getDish().getPrice() : 0) * item.getQuantity();

                double toppingsPrice = 0;
                if (item.getToppings() != null) {
                    for (Topping topping : item.getToppings()) {
                        toppingsPrice += (topping.getPrice() != null ? topping.getPrice() : 0);
                    }
                    toppingsPrice *= item.getQuantity();
                }

                totalPrice += dishPrice + toppingsPrice;
                totalQuantity += item.getQuantity();
            }
        }

        DecimalFormat formatter = new DecimalFormat("#,### đ");
        tvTotalPrice.setText(formatter.format(totalPrice));
        tvTotalQuantity.setText(String.valueOf(totalQuantity));
    }

    private void refreshData() {
        setupStore();
        setupBigDish();
        setupDish();
        setupShortRating();
        setupUserFavorite();
        setupUserCart();
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void goToReviewPage(View view) {
        Intent intent = new Intent(StoreActivity.this, RatingActivity.class);
        intent.putExtra("storeId", storeId);
        startActivity(intent);
    }

    public void goToCartDetailPage(View view) {
        Intent intent = new Intent(StoreActivity.this, CartDetailActivity.class);
        intent.putExtra("storeId", storeId);
        startActivity(intent);
    }
}