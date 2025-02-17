package com.example.food_ordering_mobile_app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.food_ordering_mobile_app.ui.store.orders.FragmentConfirmedOrder;
import com.example.food_ordering_mobile_app.ui.store.orders.FragmentHistoryOrder;
import com.example.food_ordering_mobile_app.ui.store.orders.FragmentLatestOrder;
import com.example.food_ordering_mobile_app.ui.store.orders.FragmentPreorder;

public class OrderAdapter extends FragmentStateAdapter {
    public OrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public OrderAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public OrderAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        switch (position) {
            case 0:
                return new FragmentPreorder();
            case 1:
                return new FragmentLatestOrder();
            case 2:
                return new FragmentConfirmedOrder();
            default:
                return new FragmentHistoryOrder();
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
