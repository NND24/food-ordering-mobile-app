package com.example.food_ordering_mobile_app.ui.customer.account.location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.food_ordering_mobile_app.ui.common.LoginActivity;
import com.example.food_ordering_mobile_app.ui.common.RegisterActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.LocationViewModel;

import java.util.List;

public class AddLocationActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationViewModel locationViewModel;
    private EditText etLocationName, etLocationDetailAddress, etNote, etContactName, etContactPhonenumber;
    private TextView tvLocationAddress;
    private Button btnSaveLocation;
    private LinearLayout btnChooseLocation;
    private String type;
    private static final int REQUEST_CODE_LOCATION = 200;
    private double lat = 200, lon = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_add);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        etLocationName = findViewById(R.id.etLocationName);
        tvLocationAddress = findViewById(R.id.tvLocationAddress);
        etLocationDetailAddress = findViewById(R.id.etLocationDetailAddress);
        etNote = findViewById(R.id.etNote);
        etContactName = findViewById(R.id.etContactName);
        etContactPhonenumber = findViewById(R.id.etContactPhonenumber);
        btnSaveLocation = findViewById(R.id.btnSaveLocation);
        btnChooseLocation = findViewById(R.id.btnChooseLocation);

        type = getIntent().getStringExtra("type");

        if ("home".equals(type) || "company".equals(type)) {
            if("home".equals(type)) {
                etLocationName.setText("Nhà");
            } else {
                etLocationName.setText("Công ty");
            }
            etLocationName.setEnabled(false);
        } else {
            etLocationName.setEnabled(true);
        }

        // Initialize ViewModel
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        btnChooseLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseLocationActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            startActivityForResult(intent, REQUEST_CODE_LOCATION);
        });

        btnSaveLocation.setOnClickListener(v -> handleAddLocation());

        locationViewModel.getAddLocationResponse().observe(this, new Observer<Resource<Location>>() {
            @Override
            public void onChanged(Resource<Location> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        Toast.makeText(AddLocationActivity.this, "Thêm địa chỉ thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddLocationActivity.this, LocationActivity.class));
                        finish();
                        break;
                    case ERROR:
                        Log.e("AddLocationActivity", "Thêm địa chỉ thất bại: " + resource.getMessage());
                        break;
                }
            }
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

    }

    public void goBack(View view) {
        finish();
    }
}
