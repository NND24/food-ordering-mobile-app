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
import com.example.food_ordering_mobile_app.ui.customer.cart.CartActivity;
import com.example.food_ordering_mobile_app.ui.customer.cart.CartDetailActivity;
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
    private String homeLocationId = "", companyLocationId = "", storeId;
    private CustomHeaderView customHeaderView;
    private List<Location> familiarLocations;
    private List<Location> homeLocations;
    private List<Location> companyLocations;
    private boolean isFromCartDetailActivity;

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

        storeId = getIntent().getStringExtra("storeId") != null ? getIntent().getStringExtra("storeId") : "";
        isFromCartDetailActivity = getIntent().getBooleanExtra("isFromCartDetailActivity", false);

        customHeaderView.setLifecycleOwner(this);

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

        if(isFromCartDetailActivity) {
            btnCurrentHomeAddress.setOnClickListener(v -> {
                Intent resultIntent  = new Intent(this, CartDetailActivity.class);
                resultIntent.putExtra("storeId", storeId);
                resultIntent.putExtra("locationName", homeLocations.get(0).getName());
                resultIntent.putExtra("deliveryAddress", homeLocations.get(0).getAddress());
                resultIntent.putExtra("customerName", homeLocations.get(0).getContactName());
                resultIntent.putExtra("customerPhonenumber", homeLocations.get(0).getContactPhonenumber());
                resultIntent.putExtra("detailAddress", homeLocations.get(0).getDetailAddress());
                resultIntent.putExtra("note", homeLocations.get(0).getNote());
                resultIntent.putExtra("lat", homeLocations.get(0).getLat());
                resultIntent.putExtra("lon", homeLocations.get(0).getLon());
                setResult(RESULT_OK, resultIntent);
                finish();
            });

            btnCurrentCompanyAddress.setOnClickListener(v -> {
                Intent resultIntent = new Intent(this, CartDetailActivity.class);
                resultIntent.putExtra("storeId", storeId);
                resultIntent.putExtra("locationName", companyLocations.get(0).getName());
                resultIntent.putExtra("deliveryAddress", companyLocations.get(0).getAddress());
                resultIntent.putExtra("customerName", companyLocations.get(0).getContactName());
                resultIntent.putExtra("customerPhonenumber", companyLocations.get(0).getContactPhonenumber());
                resultIntent.putExtra("detailAddress", companyLocations.get(0).getDetailAddress());
                resultIntent.putExtra("note", companyLocations.get(0).getNote());
                resultIntent.putExtra("lat", companyLocations.get(0).getLat());
                resultIntent.putExtra("lon", companyLocations.get(0).getLon());
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        }


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

        btnHomeRemove.setOnClickListener(v -> {
            if(homeLocationId != "") {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa địa chỉ này?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            locationViewModel.deleteLocation(homeLocationId);
                        })
                        .setNegativeButton("Không", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create()
                        .show();
            }
        });

        btnCompanyRemove.setOnClickListener(v -> {
            if(companyLocationId != "") {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa địa chỉ này?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            locationViewModel.deleteLocation(companyLocationId);
                        })
                        .setNegativeButton("Không", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create()
                        .show();
            }
        });

        locationViewModel.getUpdateLocationResponse().observe(this, new Observer<Resource<Location>>() {
            @Override
            public void onChanged(Resource<Location> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        locationViewModel.getUserLocations();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        locationViewModel.getDeleteLocationResponse().observe(this, new Observer<Resource<Location>>() {
            @Override
            public void onChanged(Resource<Location> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        locationViewModel.getUserLocations();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
    }

    private void setupLocation() {
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        locationList = new ArrayList<>();
        locationAdapter = new LocationAdapter(LocationActivity.this,this, locationList, (location) -> {
            if(isFromCartDetailActivity) {
                Intent resultIntent = new Intent(this, CartDetailActivity.class);
                resultIntent.putExtra("storeId", storeId);
                resultIntent.putExtra("locationName", location.getName());
                resultIntent.putExtra("deliveryAddress", location.getAddress());
                resultIntent.putExtra("customerName", location.getContactName());
                resultIntent.putExtra("customerPhonenumber", location.getContactPhonenumber());
                resultIntent.putExtra("detailAddress", location.getDetailAddress());
                resultIntent.putExtra("note", location.getNote());
                resultIntent.putExtra("lat", location.getLat());
                resultIntent.putExtra("lon", location.getLon());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
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

                        familiarLocations = new ArrayList<>();
                        homeLocations = new ArrayList<>();
                        companyLocations = new ArrayList<>();

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
                            tvCompanyLocationAddress.setText(companyLocations.get(0).getAddress());
                            homeLocationId = companyLocations.get(0).getId();
                        } else {
                            btnAddCompanyAddress.setVisibility(View.VISIBLE);
                            btnCurrentCompanyAddress.setVisibility(View.GONE);
                        }

                        locationList.addAll(familiarLocations);
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
