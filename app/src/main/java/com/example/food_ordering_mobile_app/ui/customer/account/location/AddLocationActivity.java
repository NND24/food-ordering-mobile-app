package com.example.food_ordering_mobile_app.ui.customer.account.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.viewmodels.LocationViewModel;

public class AddLocationActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private LocationViewModel locationViewModel;
    private EditText etLocationName, etLocationAddress, tvLocationDetailAddress, etNote, etContactName, etContactPhonenumber;
    private Button btnSaveLocation;
    private LinearLayout btnChooseLocation;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_add);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        etLocationName = findViewById(R.id.etLocationName);
        etLocationAddress = findViewById(R.id.etLocationAddress);
        tvLocationDetailAddress = findViewById(R.id.tvLocationDetailAddress);
        etNote = findViewById(R.id.etNote);
        etContactName = findViewById(R.id.etContactName);
        etContactPhonenumber = findViewById(R.id.etContactPhonenumber);
        btnSaveLocation = findViewById(R.id.btnSaveLocation);
        btnChooseLocation = findViewById(R.id.btnChooseLocation);

        type = getIntent().getStringExtra("type");

        // Initialize ViewModel
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        btnChooseLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseLocationActivity.class);
            intent.putExtra("type", "home");
            this.startActivity(intent);
        });
    }

    private void refreshData() {

    }

    public void goBack(View view) {
        onBackPressed();
    }
}
