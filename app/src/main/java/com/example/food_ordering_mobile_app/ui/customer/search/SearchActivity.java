package com.example.food_ordering_mobile_app.ui.customer.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.FoodTypeAdapter;
import com.example.food_ordering_mobile_app.adapters.StoreAdapter;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.ui.common.CustomHeaderView;
import com.example.food_ordering_mobile_app.ui.customer.store.StoreActivity;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewmodels.FoodTypeViewModel;
import com.example.food_ordering_mobile_app.viewmodels.StoreViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private FoodTypeViewModel foodTypeViewModel;
    private StoreViewModel storeViewModel;
    private FoodTypeAdapter foodTypeAdapter;
    private List<FoodType> foodTypes;
    private RecyclerView foodTypeRecyclerView;
    private StoreAdapter searchStoreAdapter;
    private List<Store> searchStores;
    private RecyclerView searchStoreRecyclerView;
    private TextView tvRefresh, tvAmountFilter;
    private EditText etSearch;
    private ImageButton btnSearch;
    private String name = "";
    private Set<String> selectedFoodTypes = new HashSet<>();
    private String sort = "";
    private Map<String, String> queryParams = new HashMap<>();
    private int filterAmount = 0;
    private CustomHeaderView customHeaderView;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private final int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        foodTypeRecyclerView = findViewById(R.id.foodTypeRecyclerView);
        searchStoreRecyclerView = findViewById(R.id.searchStoreRecyclerView);
        tvRefresh = findViewById(R.id.tvRefresh);
        tvAmountFilter = findViewById(R.id.tvAmountFilter);
        customHeaderView = findViewById(R.id.customHeaderView);

        customHeaderView.setLifecycleOwner(this);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        Intent intent = getIntent();
        if (intent != null) {
            sort = intent.getStringExtra("sort") != null ? intent.getStringExtra("sort") : "";
            name = intent.getStringExtra("search") != null ? intent.getStringExtra("search") : "";
            List<String> selectedCategories = getIntent().getStringArrayListExtra("selected_categories");
            if (selectedCategories != null) {
                selectedFoodTypes.addAll(selectedCategories);
            }
        }

        updateFilterAmount();

        etSearch.setText(name);

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = etSearch.getText().toString().trim();
                name = query;
                fetchStores();
                return true;
            }
            return false;
        });

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            name = query;

            currentPage = 1;
            isLastPage = false;

            fetchStores();
        });

        tvRefresh.setOnClickListener(v -> {
            Intent newIntent = new Intent(this, SearchActivity.class);
            startActivity(newIntent);
            finish();
        });

        foodTypeViewModel = new ViewModelProvider(this).get(FoodTypeViewModel.class);
        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);

        // Handle show user name
        User savedUser = SharedPreferencesHelper.getInstance(this).getCurrentUser();
        if (savedUser != null) {
            String fullName = savedUser.getName();
            if (fullName != null && !fullName.trim().isEmpty()) {
                String[] nameParts = fullName.trim().split("\\s+");
                String lastName = nameParts[nameParts.length - 1];
                customHeaderView.setName(lastName);
            } else {
                customHeaderView.setName("");
            }
        }

        // Set up show food types
        setupFoodTypes();

        // Handle search store
        searchStoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchStores = new ArrayList<>();
        searchStoreAdapter = new StoreAdapter(this, searchStores, store -> goToActivity(StoreActivity.class));
        searchStoreRecyclerView.setAdapter(searchStoreAdapter);

        searchStoreRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= pageSize) {
                            // Tải trang tiếp theo
                            currentPage++;
                            fetchStores();
                        }
                    }
                }
            }
        });

        fetchStores();

        storeViewModel.getSearchStoreResponse().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    if (currentPage == 1) swipeRefreshLayout.setRefreshing(true);
                    break;
                case SUCCESS:
                    swipeRefreshLayout.setRefreshing(false);
                    isLoading = false;

                    List<Store> newData = resource.getData().getData();

                    if (currentPage == 1) {
                        searchStores.clear();
                    }

                    if (newData.size() < pageSize) {
                        isLastPage = true;
                    }

                    searchStores.addAll(newData);
                    searchStoreAdapter.notifyDataSetChanged();
                    break;
                case ERROR:
                    swipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                    break;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            ArrayList<String> selectedCategories = data.getStringArrayListExtra("selected_categories");
            if (selectedCategories != null) {
                Log.d("DEBUG", "Updated categories: " + selectedCategories);
            }
        }
    }

    private void updateFilterAmount() {
        filterAmount = 0;

        if (!sort.isEmpty()) {
            filterAmount++;
        }
        if (!selectedFoodTypes.isEmpty()) {
            filterAmount++;
        }

        if (filterAmount > 0) {
            tvAmountFilter.setText(String.valueOf(filterAmount));
            tvAmountFilter.setVisibility(View.VISIBLE);
        } else {
            tvAmountFilter.setVisibility(View.GONE);
        }
    }


    private void setupFoodTypes() {
        foodTypeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        foodTypes = new ArrayList<>();
        foodTypeAdapter = new FoodTypeAdapter(this, foodTypes, selectedFoodTypes, selectedFoodTypeIds -> {
            selectedFoodTypes.addAll(selectedFoodTypeIds);
            queryParams.put("category", TextUtils.join(",", selectedFoodTypes));

            updateFilterAmount();

            storeViewModel.getSearchStore(queryParams);
        });

        foodTypeRecyclerView.setAdapter(foodTypeAdapter);

        foodTypeViewModel.getAllFoodTypesResponse().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    swipeRefreshLayout.setRefreshing(true);
                    break;
                case SUCCESS:
                    swipeRefreshLayout.setRefreshing(false);
                    foodTypes.clear();
                    foodTypes.addAll(resource.getData());
                    foodTypeAdapter.notifyDataSetChanged();

                    SharedPreferencesHelper.getInstance(getApplicationContext()).saveFoodTypes(resource.getData());
                    break;
                case ERROR:
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        });

        List<FoodType> savedFoodTypes = SharedPreferencesHelper.getInstance(this).getSavedFoodTypes();
        if (savedFoodTypes != null && !savedFoodTypes.isEmpty()) {
            foodTypes.clear();
            foodTypes.addAll(savedFoodTypes);
            foodTypeAdapter.notifyDataSetChanged();
        }

        foodTypeViewModel.getAllFoodTypes();
    }

    private void fetchStores() {
        isLoading = true;
        queryParams.put("name", name);
        queryParams.put("category", TextUtils.join(",", selectedFoodTypes));
        queryParams.put("sort", sort.isEmpty() ? "standout" : sort);
        queryParams.put("limit", String.valueOf(pageSize));
        queryParams.put("page", String.valueOf(currentPage));

        storeViewModel.getSearchStore(queryParams);
    }

    private void refreshData() {
        foodTypeViewModel.getAllFoodTypes();
        fetchStores();
    }

    public void openFilterAll(View view) {
        Intent intent = new Intent(this, FilterAllActivity.class);
        intent.putExtra("search", name);
        intent.putExtra("sort", sort);
        intent.putStringArrayListExtra("selected_categories", new ArrayList<>(selectedFoodTypes));
        startActivity(intent);
    }

    public void openSortBy(View view) {
        Intent intent = new Intent(this, SortByActivity.class);
        intent.putExtra("search", name);
        intent.putExtra("sort", sort);
        intent.putStringArrayListExtra("selected_categories", new ArrayList<>(selectedFoodTypes));
        startActivity(intent);
    }

    public void openCategoryFilter(View view) {
        Intent intent = new Intent(this, CategoryFilterActivity.class);
        intent.putExtra("search", name);
        intent.putExtra("sort", sort);
        intent.putStringArrayListExtra("selected_categories", new ArrayList<>(selectedFoodTypes));
        startActivity(intent);
    }

    private void goToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
