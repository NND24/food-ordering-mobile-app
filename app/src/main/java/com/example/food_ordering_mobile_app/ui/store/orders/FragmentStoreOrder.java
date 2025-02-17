package com.example.food_ordering_mobile_app.ui.store.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OrderAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentStoreOrder extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_order, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        orderAdapter = new OrderAdapter(this);
        viewPager.setAdapter(orderAdapter);

        // Add tabs dynamically to ensure they are evenly spread
        tabLayout.addTab(tabLayout.newTab().setText(R.string.preorder));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.latest));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.confirmed));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.history));


        // Set up ViewPager with TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Preorder"); break;
                case 1: tab.setText("Latest"); break;
                case 2: tab.setText("Confirmed"); break;
                case 3: tab.setText("History"); break;
            }
        }).attach();

        return view;
    }
}
