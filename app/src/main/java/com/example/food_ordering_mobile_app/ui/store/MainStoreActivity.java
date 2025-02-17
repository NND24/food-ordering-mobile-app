package com.example.food_ordering_mobile_app.ui.store;

import android.os.Bundle;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.ui.store.discount.DiscountFragment;
import com.example.food_ordering_mobile_app.ui.store.home.HomeFragment;
import com.example.food_ordering_mobile_app.ui.store.orders.FragmentStoreOrder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.food_ordering_mobile_app.databinding.ActivityMainStoreBinding;

public class MainStoreActivity extends AppCompatActivity {

    private ActivityMainStoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view_store);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_store_main);
        NavigationUI.setupWithNavController(navView, navController); // ✅ No need to manually replace fragments


        // Manual Fragment Transactions (Handling Clicks)
        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home_item) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.orders_item) {
                selectedFragment = new FragmentStoreOrder();
            } else if (item.getItemId() == R.id.discount_item) {
                selectedFragment = new DiscountFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_store_main, selectedFragment)
                        .commit();
            }
            return true;
        });

    }
}
