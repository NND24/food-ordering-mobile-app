package com.example.food_ordering_mobile_app.ui.store;

import android.os.Bundle;

import com.example.food_ordering_mobile_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_store_main);
        NavigationUI.setupWithNavController(binding.navViewStore, navController);
    }
}
