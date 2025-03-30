package com.example.food_ordering_mobile_app.ui.customer.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.CategoryFilterAdapter;
import com.example.food_ordering_mobile_app.adapters.FoodTypeAdapter;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewmodels.FoodTypeViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryFilterActivity extends AppCompatActivity {
    private FoodTypeViewModel foodTypeViewModel;
    private CategoryFilterAdapter categoryFilterAdapter;
    private List<FoodType> categories;
    private String name = "";
    private String sort = "";
    private RecyclerView categoryFilterRecyclerView;
    private Set<String> selectedFoodTypes = new HashSet<>();
    private Button btnApply, btnRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_filter);

        categoryFilterRecyclerView = findViewById(R.id.categoryFilterRecyclerView);
        btnApply = findViewById(R.id.btnApply);
        btnRefresh = findViewById(R.id.btnRefresh);

        Intent intent = getIntent();
        if (intent != null) {
            sort = intent.getStringExtra("sort") != null ? intent.getStringExtra("sort") : "";
            name = intent.getStringExtra("search") != null ? intent.getStringExtra("search") : "";
            List<String> selectedCategories = getIntent().getStringArrayListExtra("selected_categories");
            if (selectedCategories != null) {
                selectedFoodTypes.addAll(selectedCategories);
            }
        }

        if (selectedFoodTypes.isEmpty()) {
            btnRefresh.setVisibility(View.GONE);
        } else {
            btnRefresh.setVisibility(View.VISIBLE);
        }

        btnApply.setOnClickListener(v -> {
            Intent newIntent = new Intent(this, SearchActivity.class);
            newIntent.putExtra("search", name);
            newIntent.putExtra("sort", sort);
            newIntent.putStringArrayListExtra("selected_categories", new ArrayList<>(selectedFoodTypes));
            finish();
            startActivity(newIntent);
        });

        btnRefresh.setOnClickListener(v -> {
            selectedFoodTypes.clear();
            categoryFilterAdapter.notifyDataSetChanged();
            btnRefresh.setVisibility(View.GONE);
        });

        foodTypeViewModel = new ViewModelProvider(this).get(FoodTypeViewModel.class);

        categoryFilterRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        categories = new ArrayList<>();
        categoryFilterAdapter = new CategoryFilterAdapter(this, categories, selectedFoodTypes, selectedFoodTypeIds -> {
            if (selectedFoodTypeIds != null && !selectedFoodTypeIds.isEmpty()) {
                selectedFoodTypes.addAll(selectedFoodTypeIds);
            } else {
                Log.d("DEBUG", "selectedFoodTypeIds is empty or null");
            }

            if (selectedFoodTypes.isEmpty()) {
                btnRefresh.setVisibility(View.GONE);
            } else {
                btnRefresh.setVisibility(View.VISIBLE);
            }
        });

        categoryFilterRecyclerView.setAdapter(categoryFilterAdapter);

        foodTypeViewModel.getAllFoodTypesResponse().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    categories.clear();
                    categories.addAll(resource.getData());
                    categoryFilterAdapter.notifyDataSetChanged();
                    break;
                case ERROR:
                    break;
            }
        });

        foodTypeViewModel.getAllFoodTypes();
    }

    public void closeFilter(View view) {
        onBackPressed();
    }
}