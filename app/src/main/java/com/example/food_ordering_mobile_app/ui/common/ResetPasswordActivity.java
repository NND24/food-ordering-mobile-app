package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.User;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.AuthViewModel;

public class ResetPasswordActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText edtPassword, edtConfirmPassword;
    private ImageButton btnShowPassword, btnShowConfirmPassword;
    private Button btnResetPassword;
    private boolean isPasswordVisible = true;
    private boolean isConfirmPasswordVisible = true;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnShowPassword = findViewById(R.id.btnShowPassword);
        btnShowConfirmPassword = findViewById(R.id.btnShowConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        email = getIntent().getStringExtra("email"); // Get email from Intent

        if (email == null) {
            Toast.makeText(this, "Không tìm thấy email", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initialize ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe reset password response
        authViewModel.getResetPasswordResponse().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        // Hiển thị progress bar
                        break;
                    case SUCCESS:
                        // Ẩn progress bar và hiển thị thông báo thành công
                        Toast.makeText(ResetPasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        // Ẩn progress bar và hiển thị thông báo lỗi
                        String errorMessage = resource.getMessage();
                        Toast.makeText(ResetPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // Handle reset password
        btnResetPassword.setOnClickListener(view -> handleResetPassword());

        // Handle show/hide password
        btnShowPassword.setOnClickListener(v -> togglePasswordVisibility(edtPassword, btnShowPassword));
        btnShowConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(edtConfirmPassword, btnShowConfirmPassword));
    }

    private void handleResetPassword() {
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        // Kiểm tra dữ liệu
        if (password.isEmpty() || password.length() < 6) {
            edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            edtPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Mật khẩu nhập lại không khớp");
            edtConfirmPassword.requestFocus();
            return;
        }

        // Gửi dữ liệu lên ViewModel
        authViewModel.resetPassword(email, password);
    }

    private void togglePasswordVisibility(EditText editText, ImageButton toggleButton) {
        boolean isCurrentlyVisible = editText.getTransformationMethod() == null;

        if (isCurrentlyVisible) {
            editText.setTransformationMethod(new PasswordTransformationMethod());
            toggleButton.setImageResource(R.drawable.ic_eye_hide_24);
        } else {
            editText.setTransformationMethod(null);
            toggleButton.setImageResource(R.drawable.ic_eye_show_24);
        }
        editText.setSelection(editText.getText().length());
    }

    public void redirectToLoginPage(View view) {
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
