package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.ui.customer.MainCustomerActivity;
import com.example.food_ordering_mobile_app.ui.store.MainStoreActivity;


public class ChangingRoleActivity extends AppCompatActivity {

    private RadioGroup roleGroup;
    private RadioButton radioCustomer, radioStoreOwner, radioShipper;
    private Button confirmButton;
    private TextView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_role);

        roleGroup = findViewById(R.id.roleGroup);
        radioCustomer = findViewById(R.id.radioCustomer);
        radioStoreOwner = findViewById(R.id.radioStoreOwner);
        radioShipper = findViewById(R.id.radioShipper);
        confirmButton = findViewById(R.id.confirmButton);
        backBtn = findViewById(R.id.backBtn);

        // Back button action
        backBtn.setOnClickListener(v -> onBackPressed());

        // Get user role (replace with actual logic)
        String userRole = getCurrentUserRole();
        updateUIBasedOnUserRole(userRole);

        confirmButton.setOnClickListener(v -> {
            Intent intent = null;
            int selectedId = roleGroup.getCheckedRadioButtonId();

            if (selectedId == R.id.radioCustomer) {
                intent = new Intent(this, MainCustomerActivity.class);
            } else if (selectedId == R.id.radioStoreOwner) {
                intent = new Intent(this, MainStoreActivity.class);
            } else if (selectedId == R.id.radioShipper) {
//                intent = new Intent(this, MainShipperActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                finish(); // Close this activity after switching
            }
        });
    }

    private void updateUIBasedOnUserRole(String userRole) {
        if ("customer".equals(userRole)) {
            radioCustomer.setChecked(true);
        } else if ("store_owner".equals(userRole)) {
            radioStoreOwner.setChecked(true);
        } else if ("shipper".equals(userRole)) {
            radioShipper.setChecked(true);
        }
    }

    private String getCurrentUserRole() {
        return "customer"; // Replace with real logic (e.g., shared preferences or API call)
    }
}
