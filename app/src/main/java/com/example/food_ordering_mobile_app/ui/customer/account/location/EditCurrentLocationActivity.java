package com.example.food_ordering_mobile_app.ui.customer.account.location;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.location.Location;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.LocationViewModel;

public class EditCurrentLocationActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationViewModel locationViewModel;
    private Location location;
    private EditText etLocationName, etLocationAddress, tvLocationDetailAddress, etNote, etContactName, etContactPhonenumber;
    private Button btnSaveLocation;
    private String locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_current_edit);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        etLocationName = findViewById(R.id.etLocationName);
        etLocationAddress = findViewById(R.id.etLocationAddress);
        tvLocationDetailAddress = findViewById(R.id.tvLocationDetailAddress);
        etNote = findViewById(R.id.etNote);
        etContactName = findViewById(R.id.etContactName);
        etContactPhonenumber = findViewById(R.id.etContactPhonenumber);
        btnSaveLocation = findViewById(R.id.btnSaveLocation);

        locationId = getIntent().getStringExtra("locationId");

        // Initialize ViewModel
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        setupLocation();
    }

    private void setupLocation() {
        locationViewModel.getLocation(locationId);
        locationViewModel.getLocationResponse().observe(this, new Observer<Resource<Location>>() {
            @Override
            public void onChanged(Resource<Location> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("LocationFragment", "getUserLocationsResponse: " + resource.getData().toString());
                        location = resource.getData();
                        etLocationName.setText(location.getName());
                        etLocationAddress.setText(location.getAddress());
                        tvLocationDetailAddress.setText(location.getDetailAddress());
                        etNote.setText(location.getNote());
                        etContactName.setText(location.getContactName());
                        etContactPhonenumber.setText(location.getContactPhonenumber());
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("LocationFragment", "getUserLocationsResponse Error: " + resource.getData().toString());
                        break;
                }
            }
        });
    }

    private void refreshData() {
        locationViewModel.getLocation(locationId);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
