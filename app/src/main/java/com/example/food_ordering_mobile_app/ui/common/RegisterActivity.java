package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.viewmodels.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText edtName, edtEmail, edtPassword, edtConfirmPassword, edtPhonenumber;
    private RadioGroup radioGroupGender;
    private LinearLayout layoutFemale, layoutMale, layoutOther;
    private RadioButton radioFemale, radioMale, radioOther;
    private ImageButton btnShowPassword, btnShowConfirmPassword;
    private Button btnRegister;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Views
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhonenumber = findViewById(R.id.edtPhonenumber);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        layoutFemale = findViewById(R.id.layoutFemale);
        layoutMale = findViewById(R.id.layoutMale);
        layoutOther = findViewById(R.id.layoutOther);
        radioFemale = findViewById(R.id.radioFemale);
        radioMale = findViewById(R.id.radioMale);
        radioOther = findViewById(R.id.radioOther);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnShowPassword = findViewById(R.id.btnShowPassword);
        btnShowConfirmPassword = findViewById(R.id.btnShowConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        layoutFemale.setOnClickListener(v -> radioGroupGender.check(R.id.radioFemale));
        layoutMale.setOnClickListener(v -> radioGroupGender.check(R.id.radioMale));
        layoutOther.setOnClickListener(v -> radioGroupGender.check(R.id.radioOther));

        // Initialize ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe register response
        authViewModel.getRegisterResponse().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                    break;
                case ERROR:
                    Toast.makeText(RegisterActivity.this, resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        // Handle register
        btnRegister.setOnClickListener(view -> handleRegister());

        // Toggle password visibility
        btnShowPassword.setOnClickListener(v -> togglePasswordVisibility(edtPassword, btnShowPassword));
        btnShowConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(edtConfirmPassword, btnShowConfirmPassword));
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

    private void handleRegister() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();
        String phonenumber = edtPhonenumber.getText().toString().trim();

        // Validate input
        if (name.isEmpty()) {
            edtName.setError("Vui lòng nhập họ tên");
            edtName.requestFocus();
            return;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }
        if (phonenumber.isEmpty() || phonenumber.length() < 10) {
            edtPhonenumber.setError("Số điện thoại không hợp lệ");
            edtPhonenumber.requestFocus();
            return;
        }
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

        // Check gender selection
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = "";
        if (selectedId == R.id.radioMale) {
            gender = "male";
        } else if (selectedId == R.id.radioFemale) {
            gender = "female";
        } else if (selectedId == R.id.radioOther) {
            gender = "other";
        }

        // Send data to ViewModel
        authViewModel.register(name, email, phonenumber, gender, password);
    }

    public void redirectToLoginPage(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    public void goBack(View view) {
        onBackPressed();
    }
}


