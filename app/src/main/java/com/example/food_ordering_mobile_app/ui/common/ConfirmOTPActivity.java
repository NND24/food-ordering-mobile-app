package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food_ordering_mobile_app.R;

public class ConfirmOTPActivity extends AppCompatActivity {
    private TextView tvCountdownTimer;
    private TextView btnResendOtp;
    private CountDownTimer countDownTimer;
    private static final long OTP_TIMEOUT = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);

        tvCountdownTimer = findViewById(R.id.tvCountdownTimer);
        btnResendOtp = findViewById(R.id.btnResendOtp);

        startCountdownTimer();

        btnResendOtp.setOnClickListener(v -> {
            // Khi nhấn "Gửi lại OTP"
            btnResendOtp.setEnabled(false); // Vô hiệu hóa nút gửi lại
            startCountdownTimer();
            sendOtpAgain(); // Hàm để gửi lại OTP (tùy bạn triển khai)
        });
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(OTP_TIMEOUT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Cập nhật TextView mỗi giây
                long secondsRemaining = millisUntilFinished / 1000;
                tvCountdownTimer.setText(String.format("00:%02d", secondsRemaining));
            }

            @Override
            public void onFinish() {
                // Khi hết thời gian đếm ngược
                tvCountdownTimer.setText("00:00");
                btnResendOtp.setEnabled(true); // Bật nút gửi lại OTP
            }
        }.start();
    }

    private void sendOtpAgain() {
        // Logic để gửi lại OTP
        // Ví dụ: gọi API gửi OTP hoặc hiển thị thông báo cho người dùng
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Hủy đồng hồ nếu activity bị hủy
        }
    }

    public void redirectToLoginPage(View view) {
        Intent intent = new Intent(ConfirmOTPActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void redirectToResetPasswordPage(View view) {
        Intent intent = new Intent(ConfirmOTPActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
        finish();
    }
}
