package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.ui.customer.MainCustomerActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Lấy EditText sau khi đã setContentView
        EditText passwordEditText = findViewById(R.id.loginTextViewPassword);

        // Lấy drawableEnd
        Drawable drawableEnd = passwordEditText.getCompoundDrawables()[2]; // drawableEnd là phần thứ ba trong mảng

        // Đặt sự kiện touch listener cho EditText
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if drawableEnd is not null
                if (drawableEnd == null) {
                    return false;
                }

                int drawableWidth = drawableEnd.getIntrinsicWidth();
                int touchX = (int) event.getX();

                // Kiểm tra nếu người dùng chạm vào drawableEnd
                if (event.getAction() == MotionEvent.ACTION_UP && touchX >= passwordEditText.getWidth() - drawableWidth) {
                    // Kiểm tra xem mật khẩu đang ở dạng ẩn hay không
                    int inputType = passwordEditText.getInputType();

                    if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        // Nếu mật khẩu đang ẩn, đổi thành visible
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_eye_hide_24), null); // Đổi icon
                    } else {
                        // Nếu mật khẩu đang visible, đổi lại thành ẩn
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_eye_show_24), null); // Đổi icon
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void redirectToRegisterPage(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void redirectToForgotPasswordPage(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(View view) {
        Intent intent = new Intent(LoginActivity.this, MainCustomerActivity.class);
        startActivity(intent);
        finish();
    }
}
