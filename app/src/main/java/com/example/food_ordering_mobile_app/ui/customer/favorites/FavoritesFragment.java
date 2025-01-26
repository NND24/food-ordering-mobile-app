package com.example.food_ordering_mobile_app.ui.customer.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.FavoriteAdapter;
import com.example.food_ordering_mobile_app.adapters.RestaurantAdapter;
import com.example.food_ordering_mobile_app.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private RecyclerView favoriteRecyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Restaurant> favoriteList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView);
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favoriteList = new ArrayList<>();
        // Add your restaurant data to restaurantList
        favoriteList.add(new Restaurant("Minute by tuk tuk", "Cafe", 4.9, 124, "Western food", String.valueOf(R.drawable.item_1)));
        favoriteList.add(new Restaurant("Phở Lý Quoc Su", "Mỳ", 3.4, 200, "Món ăn ngon", String.valueOf(R.drawable.item_2)));

        favoriteAdapter = new FavoriteAdapter(getContext(), favoriteList);
        favoriteRecyclerView.setAdapter(favoriteAdapter);

        return view;
    }
}