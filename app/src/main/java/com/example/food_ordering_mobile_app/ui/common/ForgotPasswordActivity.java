package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.User;
import com.example.food_ordering_mobile_app.ui.customer.MainCustomerActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.AuthViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText edtEmail;
    private Button btnSendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = findViewById(R.id.edtEmail);
        btnSendOTP = findViewById(R.id.btnSendOTP);

        // Initialize ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe forgot password response
        authViewModel.getForgotPasswordResponse().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        // Show progress bar
                        break;
                    case SUCCESS:
                        String email = edtEmail.getText().toString().trim();
                        Intent intent = new Intent(ForgotPasswordActivity.this, ConfirmOTPActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        String errorMessage = resource.getMessage();
                        Toast.makeText(ForgotPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        btnSendOTP.setOnClickListener(view -> sendOtp());
    }

    private void sendOtp() {
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        authViewModel.forgotPassword(email);
    }

    public void redirectToLoginPage(View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
