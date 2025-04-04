package com.example.food_ordering_mobile_app.ui.customer.account.location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.LocationAdapter;
import com.example.food_ordering_mobile_app.models.location.Location;
import com.example.food_ordering_mobile_app.ui.common.CustomHeaderView;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.LocationViewModel;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationViewModel locationViewModel;
    private LocationAdapter locationAdapter;
    private List<Location> locationList;
    private RecyclerView locationRecyclerView;
    private LinearLayout btnAddHomeAddress, btnCurrentHomeAddress, btnAddCompanyAddress, btnCurrentCompanyAddress, btnAddNewAddress;
    private TextView tvHomeLocationAddress, tvCompanyLocationAddress;
    private ImageView btnHomeEdit, btnHomeRemove, btnCompanyEdit, btnCompanyRemove;
    private String homeLocationId, companyLocationId;
    private CustomHeaderView customHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        locationRecyclerView = findViewById(R.id.locationRecyclerView);
        btnAddHomeAddress = findViewById(R.id.btnAddHomeAddress);
        btnCurrentHomeAddress = findViewById(R.id.btnCurrentHomeAddress);
        btnAddCompanyAddress = findViewById(R.id.btnAddCompanyAddress);
        btnAddNewAddress = findViewById(R.id.btnAddNewAddress);
        btnCurrentCompanyAddress = findViewById(R.id.btnCurrentCompanyAddress);
        tvHomeLocationAddress = findViewById(R.id.tvHomeLocationAddress);
        tvCompanyLocationAddress = findViewById(R.id.tvCompanyLocationAddress);
        btnHomeEdit = findViewById(R.id.btnHomeEdit);
        btnHomeRemove = findViewById(R.id.btnHomeRemove);
        btnCompanyEdit = findViewById(R.id.btnCompanyEdit);
        btnCompanyRemove =findViewById(R.id.btnCompanyRemove);
        customHeaderView = findViewById(R.id.customHeaderView);

        customHeaderView.setLifecycleOwner(this);
        customHeaderView.setText("Địa chỉ");

        // Initialize ViewModel
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        setupLocation();

        btnAddHomeAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLocationActivity.class);
            intent.putExtra("type", "home");
            this.startActivity(intent);
        });

        btnHomeEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditLocationActivity.class);
            intent.putExtra("locationId", homeLocationId);
            intent.putExtra("type", "home");
            this.startActivity(intent);
        });

        btnAddCompanyAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLocationActivity.class);
            intent.putExtra("type", "company");
            this.startActivity(intent);
        });

        btnCompanyEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditLocationActivity.class);
            intent.putExtra("locationId", companyLocationId);
            intent.putExtra("type", "company");
            this.startActivity(intent);
        });

        btnAddNewAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLocationActivity.class);
            intent.putExtra("type", "familiar");
            this.startActivity(intent);
        });
    }

    private void setupLocation() {
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        locationList = new ArrayList<>();
        locationAdapter = new LocationAdapter(this, locationList);
        locationRecyclerView.setAdapter(locationAdapter);

        locationViewModel.getUserLocationsResponse().observe(this, new Observer<Resource<List<Location>>>() {
            @Override
            public void onChanged(Resource<List<Location>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("LocationFragment", "getUserLocationsResponse: " + resource.getData().toString());
                        locationList.clear();

                        List<Location> familiarLocations = new ArrayList<>();
                        List<Location> homeLocations = new ArrayList<>();
                        List<Location> companyLocations = new ArrayList<>();

                        for (Location location : resource.getData()) {
                            if ("familiar".equalsIgnoreCase(location.getType())) {
                                familiarLocations.add(location);
                            } else if ("home".equalsIgnoreCase(location.getType())) {
                                homeLocations.add(location);
                            } else if ("company".equalsIgnoreCase(location.getType())) {
                                companyLocations.add(location);
                            }
                        }

                        if(!homeLocations.isEmpty()) {
                            btnAddHomeAddress.setVisibility(View.GONE);
                            btnCurrentHomeAddress.setVisibility(View.VISIBLE);
                            tvHomeLocationAddress.setText(homeLocations.get(0).getAddress());
                            homeLocationId = homeLocations.get(0).getId();
                        } else {
                            btnAddHomeAddress.setVisibility(View.VISIBLE);
                            btnCurrentHomeAddress.setVisibility(View.GONE);
                        }

                        if(!companyLocations.isEmpty()) {
                            btnAddCompanyAddress.setVisibility(View.GONE);
                            btnCurrentCompanyAddress.setVisibility(View.VISIBLE);
                            tvHomeLocationAddress.setText(companyLocations.get(0).getAddress());
                            homeLocationId = companyLocations.get(0).getId();
                        } else {
                            btnAddCompanyAddress.setVisibility(View.VISIBLE);
                            btnCurrentCompanyAddress.setVisibility(View.GONE);
                        }

                        locationList.addAll(resource.getData());
                        locationAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("LocationFragment", "getUserLocationsResponse Error: " + resource.getData().toString());
                        break;
                }
            }
        });

        locationViewModel.getUserLocations();
    }

    private void refreshData() {
        locationViewModel.getUserLocations();
    }
}
