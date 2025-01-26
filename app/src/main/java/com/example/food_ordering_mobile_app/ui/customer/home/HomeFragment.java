package com.example.food_ordering_mobile_app.ui.customer.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.BigRestaurantAdapter;
import com.example.food_ordering_mobile_app.adapters.CategoryAdapter;
import com.example.food_ordering_mobile_app.adapters.RestaurantAdapter;
import com.example.food_ordering_mobile_app.models.Category;
import com.example.food_ordering_mobile_app.models.Restaurant;
import com.example.food_ordering_mobile_app.ui.customer.MainCustomerActivity;
import com.example.food_ordering_mobile_app.ui.customer.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private RecyclerView categoryRecyclerView;
    private RecyclerView restaurantRecyclerView;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;
    private RecyclerView bigRestaurantRecyclerView;
    private BigRestaurantAdapter bigRestaurantAdapter;
    private List<Restaurant> bigRestaurantList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        categoryList = new ArrayList<>();
        categoryList.add(new Category(String.valueOf(R.drawable.cat_3), "Cơm"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_4), "Pho"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_offer), "Banh Mi"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bun"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bun"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bun"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bun"));
        categoryList.add(new Category(String.valueOf(R.drawable.cat_sri), "Bun"));

        CategoryAdapter categoryAdapter = new CategoryAdapter(requireContext(), categoryList, category -> {
            // Chuyển đến SearchActivity
            Intent intent = new Intent(requireContext(), SearchActivity.class);


            // Gửi dữ liệu category qua Intent (nếu cần)
            intent.putExtra("category_name", category.getName());

            startActivity(intent);
        });

        categoryRecyclerView.setAdapter(categoryAdapter);

        restaurantRecyclerView = view.findViewById(R.id.restaurantRecyclerView);
        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Use LinearLayoutManager

        restaurantList = new ArrayList<>();
        // Add your restaurant data to restaurantList
        restaurantList.add(new Restaurant("Minute by tuk tuk", "Cafe", 4.9, 124, "Western food", String.valueOf(R.drawable.item_1)));
        restaurantList.add(new Restaurant("Phở Lý Quoc Su", "Mỳ", 3.4, 200, "Món ăn ngon", String.valueOf(R.drawable.item_2)));

        restaurantAdapter = new RestaurantAdapter(getContext(), restaurantList);
        restaurantRecyclerView.setAdapter(restaurantAdapter);

        bigRestaurantRecyclerView = view.findViewById(R.id.bigRestaurantRecyclerView);
        bigRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Use LinearLayoutManager

        bigRestaurantList = new ArrayList<>();
        // Add your restaurant data to restaurantList
        bigRestaurantList.add(new Restaurant("Minute by tuk tuk", "Cafe", 4.9, 124, "Western food",String.valueOf(R.drawable.res_1)));
        bigRestaurantList.add(new Restaurant("Phở Lý Quoc Su", "Mỳ", 3.4, 200, "Món ăn ngon",String.valueOf(R.drawable.item_2)));

        bigRestaurantAdapter = new BigRestaurantAdapter(getContext(), bigRestaurantList);
        bigRestaurantRecyclerView.setAdapter(bigRestaurantAdapter);

        return view;
    }
}