package com.example.food_ordering_mobile_app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.food_ordering_mobile_app.ui.customer.account.AccountFragment;
import com.example.food_ordering_mobile_app.ui.customer.favorites.FavoritesFragment;
import com.example.food_ordering_mobile_app.ui.customer.home.HomeFragment;
import com.example.food_ordering_mobile_app.ui.customer.messages.ChatFragment;
import com.example.food_ordering_mobile_app.ui.customer.orders.OrdersFragment;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {
    public ViewpagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int positon) {
    switch (positon){
        case 0:
            return new ChatFragment();
        case 1:
            return new OrdersFragment();
        case 2:
            return new HomeFragment();
        case 3:
            return new FavoritesFragment();
        case 4:
            return new AccountFragment();
         }
        return null;
    }

    @Override
    public int getCount() {return 5;}
}
