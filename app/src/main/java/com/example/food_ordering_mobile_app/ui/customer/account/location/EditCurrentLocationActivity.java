package com.example.food_ordering_mobile_app.ui.customer.account.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.location.Location;
import com.example.food_ordering_mobile_app.ui.customer.cart.CartDetailActivity;
import com.example.food_ordering_mobile_app.viewmodels.LocationViewModel;

public class EditCurrentLocationActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationViewModel locationViewModel;
    private Location location;
    private EditText etLocationName, etLocationDetailAddress, etNote, etContactName, etContactPhonenumber;
    private TextView tvLocationAddress;
    private LinearLayout btnChooseLocation;
    private Button btnSaveLocation;
    private String storeId, type = "familiar", locationName, deliveryAddress, customerName, customerPhonenumber, detailAddress, note;
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

        storeId = getIntent().getStringExtra("storeId");
        locationName = getIntent().getStringExtra("locationName") != null ? getIntent().getStringExtra("locationName") : "";
        deliveryAddress = getIntent().getStringExtra("deliveryAddress") != null ? getIntent().getStringExtra("deliveryAddress") : "";
        customerName = getIntent().getStringExtra("customerName") != null ? getIntent().getStringExtra("customerName") : "";
        customerPhonenumber = getIntent().getStringExtra("customerPhonenumber") != null ? getIntent().getStringExtra("customerPhonenumber") : "";
        detailAddress = getIntent().getStringExtra("detailAddress") != null ? getIntent().getStringExtra("detailAddress") : "";
        note = getIntent().getStringExtra("note") != null ? getIntent().getStringExtra("note") : "";
        lat = getIntent().getDoubleExtra("lat", 200);
        lon = getIntent().getDoubleExtra("lon", 200);

        etLocationName.setText(locationName);
        tvLocationAddress.setText(deliveryAddress);
        etLocationDetailAddress.setText(detailAddress);
        etNote.setText(note);
        etContactName.setText(customerName);
        etContactPhonenumber.setText(customerPhonenumber);

        // Initialize ViewModel
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

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
            locationName = etLocationName.getText().toString().trim();
            deliveryAddress = tvLocationAddress.getText().toString().trim();
            detailAddress = etLocationDetailAddress.getText().toString().trim();
            note = etNote.getText().toString().trim();
            customerName = etContactName.getText().toString().trim();
            customerPhonenumber = etContactPhonenumber.getText().toString().trim();
            if (locationName.isEmpty()) {
                etLocationName.setError("Vui lòng nhập tên địa điểm");
                etLocationName.requestFocus();
                return;
            }
            if (deliveryAddress.isEmpty() || lat == 200 || lon == 200) {
                tvLocationAddress.setError("Vui lòng chọn địa điểm");
                return;
            }

            Intent resultIntent  = new Intent(this, CartDetailActivity.class);
            resultIntent.putExtra("storeId", storeId);
            resultIntent.putExtra("locationName", locationName);
            resultIntent.putExtra("deliveryAddress", deliveryAddress);
            resultIntent.putExtra("customerName", customerName);
            resultIntent.putExtra("customerPhonenumber", customerPhonenumber);
            resultIntent.putExtra("detailAddress", detailAddress);
            resultIntent.putExtra("note", note);
            resultIntent.putExtra("lat", lat);
            resultIntent.putExtra("lon", lon);
            setResult(RESULT_OK, resultIntent);
            finish();
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

    private void handleAddLocation() {
        locationName= etLocationName.getText().toString().trim();
        deliveryAddress = tvLocationAddress.getText().toString().trim();
        detailAddress = etLocationDetailAddress.getText().toString().trim();
        note = etNote.getText().toString().trim();
        customerName = etContactName.getText().toString().trim();
        customerPhonenumber = etContactPhonenumber.getText().toString().trim();

        if (locationName.isEmpty()) {
            etLocationName.setError("Vui lòng nhập tên địa điểm");
            etLocationName.requestFocus();
            return;
        }

        if (deliveryAddress.isEmpty() || lat == 200 || lon == 200) {
            tvLocationAddress.setError("Vui lòng chọn địa điểm");
            return;
        }

        Location newLocation = new Location(locationName, deliveryAddress, lat, lon, detailAddress, customerName, note, customerPhonenumber, type);

        locationViewModel.addLocation(newLocation);
    }

    private void refreshData() {

    }

    public void goBack(View view) {
        onBackPressed();
    }
}
