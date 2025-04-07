package com.example.food_ordering_mobile_app.ui.customer.account.location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.location.Location;
import com.example.food_ordering_mobile_app.ui.customer.cart.CartDetailActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.LocationViewModel;

public class EditCurrentLocationActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationViewModel locationViewModel;
    private Location location;
    private EditText etLocationName, etLocationDetailAddress, etNote, etContactName, etContactPhonenumber;
    private TextView tvLocationAddress;
    private LinearLayout btnChooseLocation;
    private Button btnSaveLocation;
    private String locationId, storeId, type = "familiar";
    private static final int REQUEST_CODE_LOCATION = 200;
    private double lat = 200, lon = 200;
    private ImageView btnAddToLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_current_edit);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        etLocationName = findViewById(R.id.etLocationName);
        tvLocationAddress = findViewById(R.id.tvLocationAddress);
        etLocationDetailAddress = findViewById(R.id.etLocationDetailAddress);
        etNote = findViewById(R.id.etNote);
        etContactName = findViewById(R.id.etContactName);
        etContactPhonenumber = findViewById(R.id.etContactPhonenumber);
        btnSaveLocation = findViewById(R.id.btnSaveLocation);
        btnChooseLocation = findViewById(R.id.btnChooseLocation);
        btnAddToLocation = findViewById(R.id.btnAddToLocation);

        locationId = getIntent().getStringExtra("locationId");
        storeId = getIntent().getStringExtra("storeId");

        // Initialize ViewModel
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        setupLocation();

        btnChooseLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseLocationActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            startActivityForResult(intent, REQUEST_CODE_LOCATION);
        });

        btnAddToLocation.setOnClickListener(v -> handleAddLocation());

        locationViewModel.getAddLocationResponse().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    Toast.makeText(EditCurrentLocationActivity.this, "Thêm địa chỉ thành công!", Toast.LENGTH_SHORT).show();
                    btnAddToLocation.setImageResource(R.drawable.ic_favorite_active);
                    break;
                case ERROR:
                    break;
            }
        });

        btnSaveLocation.setOnClickListener(v -> {
            String locationName = etLocationName.getText().toString().trim();
            String locationAddress = tvLocationAddress.getText().toString().trim();
            String locationDetailAddress = etLocationDetailAddress.getText().toString().trim();
            String note = etNote.getText().toString().trim();
            String contactName = etContactName.getText().toString().trim();
            String contactPhonenumber = etContactPhonenumber.getText().toString().trim();
            if (locationName.isEmpty()) {
                etLocationName.setError("Vui lòng nhập tên địa điểm");
                etLocationName.requestFocus();
                return;
            }
            if (locationAddress.isEmpty() || lat == 200 || lon == 200) {
                tvLocationAddress.setError("Vui lòng chọn địa điểm");
                return;
            }

            Intent intent = new Intent(this, CartDetailActivity.class);
            intent.putExtra("storeId", storeId);
            intent.putExtra("deliveryAddress", locationAddress);
            intent.putExtra("customerName", contactName);
            intent.putExtra("customerPhonenumber", contactPhonenumber);
            intent.putExtra("detailAddress", locationDetailAddress);
            intent.putExtra("note", note);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOCATION && resultCode == RESULT_OK) {
            lat = data.getDoubleExtra("latitude", 0.0);
            lon = data.getDoubleExtra("longitude", 0.0);
            String address = data.getStringExtra("address");

            tvLocationAddress.setText(address);
        }
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
                        lat = location.getLat();
                        lon = location.getLon();
                        tvLocationAddress.setText(location.getAddress());
                        etLocationDetailAddress.setText(location.getDetailAddress());
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

    private void handleAddLocation() {
        String locationName = etLocationName.getText().toString().trim();
        String locationAddress = tvLocationAddress.getText().toString().trim();
        String locationDetailAddress = etLocationDetailAddress.getText().toString().trim();
        String note = etNote.getText().toString().trim();
        String contactName = etContactName.getText().toString().trim();
        String contactPhonenumber = etContactPhonenumber.getText().toString().trim();

        if (locationName.isEmpty()) {
            etLocationName.setError("Vui lòng nhập tên địa điểm");
            etLocationName.requestFocus();
            return;
        }

        if (locationAddress.isEmpty() || lat == 200 || lon == 200) {
            tvLocationAddress.setError("Vui lòng chọn địa điểm");
            return;
        }

        Location newLocation = new Location(locationName, locationAddress, lat, lon, locationDetailAddress, contactName, note, contactPhonenumber, type);

        locationViewModel.addLocation(newLocation);
    }

    private void refreshData() {
        locationViewModel.getLocation(locationId);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
