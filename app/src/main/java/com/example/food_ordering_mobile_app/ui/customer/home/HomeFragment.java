package com.example.food_ordering_mobile_app.ui.customer.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.StoreGroupByCategoryAdapter;
import com.example.food_ordering_mobile_app.adapters.StoreBigCardAdapter;
import com.example.food_ordering_mobile_app.adapters.FoodTypeAdapter;
import com.example.food_ordering_mobile_app.adapters.StoreStandoutAdapter;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.StoreGroupByCategory;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.ui.common.CustomHeaderView;
import com.example.food_ordering_mobile_app.ui.customer.store.StoreActivity;
import com.example.food_ordering_mobile_app.ui.customer.search.SearchActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewmodels.FoodTypeViewModel;
import com.example.food_ordering_mobile_app.viewmodels.NotificationViewModel;
import com.example.food_ordering_mobile_app.viewmodels.StoreViewModel;
import com.example.food_ordering_mobile_app.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.food_ordering_mobile_app.utils.Functions;

public class HomeFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserViewModel userViewModel;
    private FoodTypeViewModel foodTypeViewModel;
    private StoreViewModel storeViewModel;
    private FoodTypeAdapter foodTypeAdapter;
    private List<FoodType> foodTypes;
    private RecyclerView foodTypeRecyclerView;
    private RecyclerView categoryRecyclerView;
    private StoreGroupByCategoryAdapter storeGroupByCategoryAdapter;
    private List<StoreGroupByCategory> storeGroupByCategoryList;
    private RecyclerView ratingStoreRecyclerView;
    private StoreBigCardAdapter storeBigCardAdapter;
    private List<Store> ratingStores;
    private RecyclerView standoutStoreRecyclerView;
    private StoreStandoutAdapter standoutStoreAdapter;
    private List<Store> standoutStores;
    private ImageButton btnSearch;
    private TextView searchAllStandoutStore, searchAllRatingStore;
    private EditText etSearch;
    private Set<String> selectedFoodTypes = new HashSet<>();
    private CustomHeaderView customHeaderView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ UI
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        foodTypeRecyclerView = view.findViewById(R.id.foodTypeRecyclerView);
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        ratingStoreRecyclerView = view.findViewById(R.id.ratingStoreRecyclerView);
        standoutStoreRecyclerView = view.findViewById(R.id.standoutStoreRecyclerView);
        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        searchAllStandoutStore = view.findViewById(R.id.searchAllStandoutStore);
        searchAllRatingStore = view.findViewById(R.id.searchAllRatingStore);
        customHeaderView = view.findViewById(R.id.customHeaderView);

        customHeaderView.setLifecycleOwner(getViewLifecycleOwner());

        // Sự kiện khi kéo xuống làm mới dữ liệu
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        searchAllStandoutStore.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchActivity.class);
            intent.putExtra("sort", "standout");
            startActivity(intent);
        });

        searchAllRatingStore.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchActivity.class);
            intent.putExtra("sort", "rating");
            startActivity(intent);
        });

        // Bắt sự kiện nhấn nút "Search" trên bàn phím
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        // Bắt sự kiện nhấn vào icon search
        btnSearch.setOnClickListener(v -> performSearch());

        // Gọi API để lấy thông tin người dùng
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        foodTypeViewModel = new ViewModelProvider(requireActivity()).get(FoodTypeViewModel.class);
        storeViewModel = new ViewModelProvider(requireActivity()).get(StoreViewModel.class);

        // Lấy dữ liệu người dùng từ SharedPreferences
        User savedUser = SharedPreferencesHelper.getInstance(requireContext()).getCurrentUser();
        if (savedUser != null) {
            String fullName = savedUser.getName();
            if (fullName != null && !fullName.trim().isEmpty()) {
                String[] nameParts = fullName.trim().split("\\s+");
                String lastName = nameParts[nameParts.length - 1];
                customHeaderView.setName(lastName);
            } else {
                customHeaderView.setName("");
            }
        } else {
            userViewModel.getCurrentUser();
        }

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
                            customHeaderView.setName(lastName);
                        } else {
                            customHeaderView.setName("");
                        }
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        // Thiết lập danh mục món ăn
        setupFoodTypes();

        // Thiết lập danh sách nhà hàng
        setupStandoutStores();
        setupRatingStores();
        setupStores();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        etSearch.setText("");
        selectedFoodTypes.clear();
        foodTypeAdapter.notifyDataSetChanged();
    }

    private void performSearch() {
        String query = etSearch.getText().toString().trim();
            Intent intent = new Intent(requireContext(), SearchActivity.class);
            intent.putExtra("search", query);
            startActivity(intent);
    }

    private void setupFoodTypes() {
        foodTypeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        foodTypes = new ArrayList<>();
        foodTypeAdapter = new FoodTypeAdapter(requireContext(), foodTypes, selectedFoodTypes, selectedFoodTypeIds -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putStringArrayListExtra("selected_categories", new ArrayList<>(selectedFoodTypeIds));
            startActivity(intent);
        });
        foodTypeRecyclerView.setAdapter(foodTypeAdapter);

        foodTypeViewModel.getAllFoodTypesResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<FoodType>>>() {
            @Override
            public void onChanged(Resource<List<FoodType>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        foodTypes.clear();
                        foodTypes.addAll(resource.getData());
                        foodTypeAdapter.notifyDataSetChanged();

                        Log.d("HomeFragment", "getAllFoodTypesResponse: " + resource);
                        SharedPreferencesHelper.getInstance(requireContext()).saveFoodTypes(resource.getData());
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("HomeFragment", "getAllFoodTypesResponse error: " + resource);
                        break;
                }
            }
        });

        List<FoodType> savedFoodTypes = SharedPreferencesHelper.getInstance(requireContext()).getSavedFoodTypes();
        if (savedFoodTypes != null && !savedFoodTypes.isEmpty()) {
            foodTypes.clear();
            foodTypes.addAll(savedFoodTypes);
            foodTypeAdapter.notifyDataSetChanged();
        }

        foodTypeViewModel.getAllFoodTypes();
    }

    private void setupRatingStores() {
        ratingStoreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ratingStores = new ArrayList<>();
        storeBigCardAdapter = new StoreBigCardAdapter(getContext(), ratingStores, store -> {
            Intent intent = new Intent(this.getContext(), StoreActivity.class);
            intent.putExtra("storeId", store.getId());
            this.getContext().startActivity(intent);
        });
        ratingStoreRecyclerView.setAdapter(storeBigCardAdapter);

        storeViewModel.getRatingStoreResponse().observe(getViewLifecycleOwner(), new Observer<Resource<ApiResponse<List<Store>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Store>>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        ratingStores.clear();
                        ratingStores.addAll(resource.getData().getData());
                        storeBigCardAdapter.notifyDataSetChanged();
                        SharedPreferencesHelper.getInstance(requireContext()).saveStores("ratingStores", resource.getData().getData());
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        List<Store> savedRatingStores = SharedPreferencesHelper.getInstance(requireContext()).getSavedStores("ratingStores");
        if (savedRatingStores != null && !savedRatingStores.isEmpty()) {
            ratingStores.clear();
            ratingStores.addAll(savedRatingStores);
            storeBigCardAdapter.notifyDataSetChanged();
        }

        SharedPreferencesHelper prefs = SharedPreferencesHelper.getInstance(getContext());
        double savedLat = prefs.getProvinceLat();
        double savedLon = prefs.getProvinceLon();

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "");
        queryParams.put("category", "");
        queryParams.put("sort", "rating");
        queryParams.put("limit", "3");
        queryParams.put("page", "1");
        queryParams.put("lat", String.valueOf(savedLat));
        queryParams.put("lon", String.valueOf(savedLon));

        storeViewModel.getRatingStore(queryParams);
    }

    private void setupStandoutStores() {
        standoutStoreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        standoutStores = new ArrayList<>();
        standoutStoreAdapter = new StoreStandoutAdapter(getContext(), standoutStores, store -> {
            Intent intent = new Intent(this.getContext(), StoreActivity.class);
            intent.putExtra("storeId", store.getId());
            this.getContext().startActivity(intent);
        });

        standoutStoreRecyclerView.setAdapter(standoutStoreAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(standoutStoreRecyclerView);

        storeViewModel.getStandoutStoreResponse().observe(getViewLifecycleOwner(), new Observer<Resource<ApiResponse<List<Store>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Store>>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        standoutStores.clear();
                        standoutStores.addAll(resource.getData().getData());
                        standoutStoreAdapter.notifyDataSetChanged();
                        SharedPreferencesHelper.getInstance(requireContext()).saveStores("standoutStores", resource.getData().getData());
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        List<Store> savedStandoutStores = SharedPreferencesHelper.getInstance(requireContext()).getSavedStores("standoutStores");
        if (savedStandoutStores != null && !savedStandoutStores.isEmpty()) {
            standoutStores.clear();
            standoutStores.addAll(savedStandoutStores);
            standoutStoreAdapter.notifyDataSetChanged();
        }

        SharedPreferencesHelper prefs = SharedPreferencesHelper.getInstance(getContext());
        double savedLat = prefs.getProvinceLat();
        double savedLon = prefs.getProvinceLon();

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "");
        queryParams.put("category", "");
        queryParams.put("sort", "standout");
        queryParams.put("limit", "5");
        queryParams.put("page", "1");
        queryParams.put("lat", String.valueOf(savedLat));
        queryParams.put("lon", String.valueOf(savedLon));

        storeViewModel.getStandoutStore(queryParams);
    }

    private void setupStores() {
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storeGroupByCategoryList = new ArrayList<>();
        storeGroupByCategoryAdapter = new StoreGroupByCategoryAdapter(getContext(), storeGroupByCategoryList);
        categoryRecyclerView.setAdapter(storeGroupByCategoryAdapter);

        storeViewModel.getAllStoreResponse().observe(getViewLifecycleOwner(), new Observer<Resource<ApiResponse<List<Store>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Store>>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        storeGroupByCategoryList.clear();
                        List<Store> stores = resource.getData().getData();
                        storeGroupByCategoryList.addAll(Functions.groupStoresByCategory(stores));
                        storeGroupByCategoryAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "");
        queryParams.put("category", "");
        queryParams.put("sort", "");
        queryParams.put("limit", "");
        queryParams.put("page", "");

        storeViewModel.getAllStore(queryParams);
    }

    private void refreshData() {
        userViewModel.getCurrentUser();
        setupFoodTypes();

        SharedPreferencesHelper prefs = SharedPreferencesHelper.getInstance(getContext());
        double savedLat = prefs.getProvinceLat();
        double savedLon = prefs.getProvinceLon();

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "");
        queryParams.put("category", "");
        queryParams.put("sort", "standout");
        queryParams.put("limit", "5");
        queryParams.put("page", "1");
        queryParams.put("lat", String.valueOf(savedLat));
        queryParams.put("lon", String.valueOf(savedLon));

        storeViewModel.getStandoutStore(queryParams);

        setupRatingStores();
        setupStores();
    }
}
