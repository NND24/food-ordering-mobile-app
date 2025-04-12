package com.example.food_ordering_mobile_app.ui.customer.store;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.food_ordering_mobile_app.adapters.DishGroupByCategoryAdapter;
import com.example.food_ordering_mobile_app.adapters.RatingShortAdapter;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.cart.CartItem;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishGroupByCategory;
import com.example.food_ordering_mobile_app.models.dish.Topping;
import com.example.food_ordering_mobile_app.models.favorite.Favorite;
import com.example.food_ordering_mobile_app.models.rating.Rating;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.ui.customer.cart.CartDetailActivity;
import com.example.food_ordering_mobile_app.ui.customer.rating.RatingActivity;
import com.example.food_ordering_mobile_app.utils.Functions;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
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
    private List<Dish> dishBigList;
    private RecyclerView reviewRecyclerView;
    private RatingShortAdapter reviewAdapter;
    private List<Rating> reviewList;
    private List<Store> favoriteList;
    private List<Cart> cartList;
    private ImageView ivStoreAvatar, ivStoreCover, ivStar;
    private ImageButton btnFavorite;
    private TextView tvStoreName, tvStoreFoodType, tvAvgRating, tvRatingOpen, tvAmountRating, tvRatingText, tvRatingClose, tvDescription, tvDot, tvTotalPrice, tvTotalQuantity;
    private String storeId;
    private Cart currentCart = null;
    private LinearLayout footer, review_container;

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
        review_container = findViewById(R.id.review_container);

        storeId = getIntent().getStringExtra("storeId");

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        ratingViewModel = new ViewModelProvider(this).get(RatingViewModel.class);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        setupStore();
        setupBigDish();
        setupDish();
        setupUserCart();
        setupShortRating();
        setupUserFavorite();

        favoriteViewModel.getRemoveFavoriteResponse().observe(this, new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
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

        favoriteViewModel.getAddFavoriteResponse().observe(this, new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
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

        cartViewModel.getUpdateCartResponse().observe(this, new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        setupUserCart();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupDish();
            }
        }, 100);
    }

    private void setupStore() {
        Store storeInfo = SharedPreferencesHelper.getInstance(getApplicationContext()).getSavedStoreInfo();
        if (storeInfo != null) {
            displayStoreInfo(storeInfo);
        }

        storeViewModel.getStoreInformation(storeId);
        storeViewModel.getStoreInformationResponse().observe(this, new Observer<Resource<ApiResponse<Store>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Store>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        Store newStoreInfo = resource.getData().getData();
                        SharedPreferencesHelper.getInstance(getApplicationContext()).saveStoreInfo(newStoreInfo);
                        displayStoreInfo(newStoreInfo);
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
    }

    private void displayStoreInfo(Store store) {
        tvStoreName.setText(store.getName());
        tvDescription.setText(store.getDescription());

        if (store.getAmountRating() != null && store.getAmountRating() > 0) {
            tvAvgRating.setText(String.format("%.2f", store.getAvgRating()));
            tvAmountRating.setText(String.valueOf(store.getAmountRating()));
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

        if (store.getStoreCategory() != null && !store.getStoreCategory().isEmpty()) {
            SpannableStringBuilder categories = new SpannableStringBuilder();
            for (int i = 0; i < store.getStoreCategory().size(); i++) {
                categories.append(store.getStoreCategory().get(i).getName());
                if (i < store.getStoreCategory().size() - 1) {
                    categories.append(" • ");
                }
            }
            tvStoreFoodType.setText(categories);
        } else {
            tvStoreFoodType.setText("");
        }

        // Load avatar
        String storeAvatarUrl = store.getAvatar() != null ? store.getAvatar().getUrl() : null;
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

        // Load cover
        String storeCoverUrl = store.getCover() != null ? store.getCover().getUrl() : null;
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
    }

    private void setupBigDish() {
        dishBigRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dishBigList = new ArrayList<>();
        dishBigAdapter = new DishBigAdapter(this,this, dishBigList, dishBig -> {
        });
        dishBigRecyclerView.setAdapter(dishBigAdapter);

        dishViewModel.getAllBigDishResponse().observe(this, new Observer<Resource<ApiResponse<List<Dish>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Dish>>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        dishBigList.clear();
                        dishBigList.addAll(resource.getData().getData());
                        SharedPreferencesHelper.getInstance(getApplicationContext()).saveListDish(resource.getData().getData());
                        dishBigAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        List<Dish> savedListDish = SharedPreferencesHelper.getInstance(getApplicationContext()).getSavedListDish();
        if (savedListDish != null && !savedListDish.isEmpty()) {
            dishBigList.clear();
            dishBigList.addAll(savedListDish);
            dishBigAdapter.notifyDataSetChanged();
        }

        dishViewModel.getAllBigDish(storeId);
    }

    private void setupDish() {
        dishRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dishGroupByCategoryList = new ArrayList<>();
        dishGroupByCategoryAdapter = new DishGroupByCategoryAdapter(this,this, dishGroupByCategoryList);
        dishRecyclerView.setAdapter(dishGroupByCategoryAdapter);



        dishViewModel.getAllDishResponse().observe(this, new Observer<Resource<ApiResponse<List<Dish>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Dish>>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        dishGroupByCategoryList.clear();
                        List<Dish> dishes = resource.getData().getData();
                        dishGroupByCategoryList.addAll(Functions.groupDishesByCategory(dishes));
                        dishGroupByCategoryAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
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

        ratingViewModel.getAllStoreRatingResponse().observe(this, new Observer<Resource<ApiResponse<List<Rating>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Rating>>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        reviewList.clear();
                        reviewList.addAll(resource.getData().getData());

                        if (reviewList != null && !reviewList.isEmpty()) {
                            review_container.setVisibility(View.VISIBLE);
                        } else {
                            review_container.setVisibility(View.GONE);
                        }

                        reviewAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        review_container.setVisibility(View.GONE);
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
        favoriteList = new ArrayList<>();
        favoriteViewModel.getUserFavoriteResponse().observe(this, new Observer<Resource<ApiResponse<Favorite>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Favorite>> resource) {
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
                        btnFavorite.setImageResource(R.drawable.ic_favorite_white_24);
                        btnFavorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                favoriteViewModel.addFavorite(storeId);
                            }
                        });
                        break;
                }
            }
        });
        favoriteViewModel.getUserFavorite();
    }

    private void setupUserCart() {
        cartList = new ArrayList<>();
        cartViewModel.getUserCartResponse().observe(this, new Observer<Resource<ApiResponse<List<Cart>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Cart>>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        cartList.clear();
                        cartList = resource.getData().getData();

                        for (Cart cart : cartList) {
                            if (cart.getStore() != null && storeId.equals(cart.getStore().getId())) {
                                currentCart = cart;
                                break;
                            }
                        }

                        calculateCartPrice(currentCart);

                        dishBigAdapter.setCurrentCart(currentCart);
                        dishGroupByCategoryAdapter.setCurrentCart(currentCart);

                        boolean hasCart = currentCart != null;
                        footer.setVisibility(hasCart ? View.VISIBLE : View.GONE);

                        // Chuyển dp sang px
                        int marginInDp = hasCart ? 70 : 0;
                        int marginInPx = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                marginInDp,
                                getResources().getDisplayMetrics()
                        );

                        // Set bottom margin
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) swipeRefreshLayout.getLayoutParams();
                        params.bottomMargin = marginInPx;
                        swipeRefreshLayout.setLayoutParams(params);
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        currentCart = null;
                        break;
                }
            }
        });
        cartViewModel.getUserCart();
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

        DecimalFormat formatter = new DecimalFormat("#,###");
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