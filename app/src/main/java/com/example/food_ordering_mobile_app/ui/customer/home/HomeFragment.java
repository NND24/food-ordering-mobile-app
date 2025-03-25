package com.example.food_ordering_mobile_app.ui.customer.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.RestaurantBigAdapter;
import com.example.food_ordering_mobile_app.adapters.CategoryAdapter;
import com.example.food_ordering_mobile_app.adapters.RestaurantAdapter;
import com.example.food_ordering_mobile_app.models.Category;
import com.example.food_ordering_mobile_app.models.Restaurant;
import com.example.food_ordering_mobile_app.models.User;
import com.example.food_ordering_mobile_app.ui.customer.cart.CartActivity;
import com.example.food_ordering_mobile_app.ui.customer.notifications.NotificationActivity;
import com.example.food_ordering_mobile_app.ui.customer.restaurant.RestaurantActivity;
import com.example.food_ordering_mobile_app.ui.customer.search.SearchActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserViewModel userViewModel;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private RecyclerView categoryRecyclerView;
    private RecyclerView restaurantRecyclerView;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;
    private RecyclerView bigRestaurantRecyclerView;
    private RestaurantBigAdapter restaurantBigAdapter;
    private List<Restaurant> bigRestaurantList;
    private ImageButton goToNotificationBtn, goToCartBtn;
    private TextView tvName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ UI
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        restaurantRecyclerView = view.findViewById(R.id.restaurantRecyclerView);
        bigRestaurantRecyclerView = view.findViewById(R.id.bigRestaurantRecyclerView);
        goToNotificationBtn = view.findViewById(R.id.goToNotificationBtn);
        goToCartBtn = view.findViewById(R.id.goToCartBtn);
        tvName = view.findViewById(R.id.tvName);

        // Sự kiện khi kéo xuống làm mới dữ liệu
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        // Thiết lập danh mục món ăn
        setupCategoryList();

        // Thiết lập danh sách nhà hàng
        setupRestaurantList();

        // Sự kiện điều hướng
        goToNotificationBtn.setOnClickListener(v -> goToActivity(NotificationActivity.class));
        goToCartBtn.setOnClickListener(v -> goToActivity(CartActivity.class));

        // Gọi API để lấy thông tin người dùng
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Lấy dữ liệu người dùng từ SharedPreferences
        User savedUser = SharedPreferencesHelper.getInstance(requireContext()).getCurrentUser();
        if (savedUser != null) {
            String fullName = savedUser.getName();
            if (fullName != null && !fullName.trim().isEmpty()) {
                String[] nameParts = fullName.trim().split("\\s+");
                String lastName = nameParts[nameParts.length - 1];
                tvName.setText(lastName);
            } else {
                tvName.setText("");
            }
        } else {
            fetchCurrentUser();
        }

        return view;
    }

    private void setupCategoryList() {
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        categoryList = new ArrayList<>();
        categoryList.add(new Category(String.valueOf(R.drawable.cat_3), "Cơm"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_4), "Phở"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_offer), "Bánh Mì"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bún"));

        categoryAdapter = new CategoryAdapter(requireContext(), categoryList, category -> {
            Intent intent = new Intent(requireContext(), SearchActivity.class);
            intent.putExtra("category_name", category.getName());
            startActivity(intent);
        });

        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void setupRestaurantList() {
        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bigRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        restaurantList = new ArrayList<>();
        bigRestaurantList = new ArrayList<>();

        restaurantList.add(new Restaurant("Minute by tuk tuk", "Cafe", 4.9, 124, "Western food", String.valueOf(R.drawable.item_1)));
        restaurantList.add(new Restaurant("Phở Lý Quốc Sư", "Mì", 3.4, 200, "Món ăn ngon", String.valueOf(R.drawable.item_2)));

        bigRestaurantList.add(new Restaurant("Minute by tuk tuk", "Cafe", 4.9, 124, "Western food", String.valueOf(R.drawable.res_1)));
        bigRestaurantList.add(new Restaurant("Phở Lý Quốc Sư", "Mì", 3.4, 200, "Món ăn ngon", String.valueOf(R.drawable.item_2)));

        restaurantAdapter = new RestaurantAdapter(getContext(), restaurantList, restaurant -> goToActivity(RestaurantActivity.class));
        restaurantBigAdapter = new RestaurantBigAdapter(getContext(), bigRestaurantList, restaurant -> goToActivity(RestaurantActivity.class));

        restaurantRecyclerView.setAdapter(restaurantAdapter);
        bigRestaurantRecyclerView.setAdapter(restaurantBigAdapter);
    }

    private void fetchCurrentUser() {
        userViewModel.getCurrentUser();
        userViewModel.getCurrentUserResponse().observe(getViewLifecycleOwner(), new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        String fullName = resource.getData().getName();
                        if (fullName != null && !fullName.trim().isEmpty()) {
                            String[] nameParts = fullName.trim().split("\\s+");
                            String lastName = nameParts[nameParts.length - 1];
                            tvName.setText(lastName);
                        } else {
                            tvName.setText("");
                        }
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
    }

    private void refreshData() {
        if (userViewModel != null) {
            userViewModel.getCurrentUserResponse().removeObservers(getViewLifecycleOwner());
            userViewModel.getCurrentUser();
            userViewModel.getCurrentUserResponse().observe(getViewLifecycleOwner(), new Observer<Resource<User>>() {
                @Override
                public void onChanged(Resource<User> resource) {
                    switch (resource.getStatus()) {
                        case LOADING:
                            swipeRefreshLayout.setRefreshing(true);
                            break;
                        case SUCCESS:
                            swipeRefreshLayout.setRefreshing(false);
                            String fullName = resource.getData().getName();
                            if (fullName != null && !fullName.trim().isEmpty()) {
                                String[] nameParts = fullName.trim().split("\\s+");
                                String lastName = nameParts[nameParts.length - 1];
                                tvName.setText(lastName);
                            } else {
                                tvName.setText("");
                            }
                            break;
                        case ERROR:
                            swipeRefreshLayout.setRefreshing(false);
                            Log.d("HomeFragment", "Error loading data: " + resource.getMessage());
                            break;
                    }
                }
            });
        }
    }

    private void goToActivity(Class<?> activityClass) {
        Intent intent = new Intent(requireContext(), activityClass);
        startActivity(intent);
    }
}
