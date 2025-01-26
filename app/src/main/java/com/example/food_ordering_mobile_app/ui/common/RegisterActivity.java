package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_mobile_app.R;

public class RegisterActivity extends AppCompatActivity {
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText passwordEditText = findViewById(R.id.txtPassword);
        ImageButton toggleShowPasswordButton = findViewById(R.id.btnShowPassword);

        toggleShowPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Ẩn mật khẩu
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    toggleShowPasswordButton.setImageResource(R.drawable.ic_eye_show_24); // Icon mắt đóng
                } else {
                    // Hiển thị mật khẩu
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                    toggleShowPasswordButton.setImageResource(R.drawable.ic_eye_hide_24); // Icon mắt mở
                }
                isPasswordVisible = !isPasswordVisible;

                // Đặt con trỏ lại cuối văn bản
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        EditText confirmPasswordEditText = findViewById(R.id.txtConfirmPassword);
        ImageButton toggleShowConfirmPasswordButton = findViewById(R.id.btnShowConfirmPassword);

        toggleShowConfirmPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConfirmPasswordVisible) {
                    // Ẩn mật khẩu
                    confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    toggleShowConfirmPasswordButton.setImageResource(R.drawable.ic_eye_show_24); // Icon mắt đóng
                } else {
                    // Hiển thị mật khẩu
                    confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                    toggleShowConfirmPasswordButton.setImageResource(R.drawable.ic_eye_hide_24); // Icon mắt mở
                }
                isConfirmPasswordVisible = !isConfirmPasswordVisible;

                // Đặt con trỏ lại cuối văn bản
                confirmPasswordEditText.setSelection(confirmPasswordEditText.getText().length());
            }
        });
    }

    public void redirectToLoginPage(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
