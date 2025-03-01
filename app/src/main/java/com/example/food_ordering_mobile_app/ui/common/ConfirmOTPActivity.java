package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.AuthViewModel;

public class ConfirmOTPActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText[] otpFields = new EditText[6]; // Mảng chứa 6 ô nhập OTP
    private TextView tvCountdownTimer, btnResendOtp, tvEmailInfo;
    private Button btnVerifyOtp;
    private CountDownTimer countDownTimer;
    private static final long OTP_TIMEOUT = 2 * 60000;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_otp);

        otpFields[0] = findViewById(R.id.otp1);
        otpFields[1] = findViewById(R.id.otp2);
        otpFields[2] = findViewById(R.id.otp3);
        otpFields[3] = findViewById(R.id.otp4);
        otpFields[4] = findViewById(R.id.otp5);
        otpFields[5] = findViewById(R.id.otp6);

        tvCountdownTimer = findViewById(R.id.tvCountdownTimer);
        btnResendOtp = findViewById(R.id.btnResendOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        tvEmailInfo = findViewById(R.id.tvEmailInfo);

        email = getIntent().getStringExtra("email"); // Get email from Intent

        if (email != null) {
            tvEmailInfo.setText(email);
        }

        if (email == null) {
            Toast.makeText(this, "Không tìm thấy email", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initialize ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupOtpInputs();
        startCountdownTimer();

        btnVerifyOtp.setOnClickListener(v -> verifyOtp());

        btnResendOtp.setOnClickListener(v -> {
            if(btnResendOtp.isEnabled()) {
                btnResendOtp.setEnabled(false);
                resendOtp();
            } else {
                Toast.makeText(this, "Vui lòng đợi hết thời gian đếm nguược trước", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe forgot password response
        authViewModel.getCheckOTPResponse().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        // Show progress bar
                        break;
                    case SUCCESS:
                        Intent intent = new Intent(ConfirmOTPActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        String errorMessage = resource.getMessage();
                        Toast.makeText(ConfirmOTPActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // Observe forgot password response
        authViewModel.getForgotPasswordResponse().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        // Show progress bar
                        break;
                    case SUCCESS:
                        if (countDownTimer != null) {
                            countDownTimer.cancel(); // Huỷ countdown trước đó
                        }
                        startCountdownTimer(); // Khởi động lại countdown
                        Toast.makeText(ConfirmOTPActivity.this, "OTP đã được gửi lại!", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        String errorMessage = resource.getMessage();
                        Toast.makeText(ConfirmOTPActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    // Xử lý nhập OTP từ 6 ô EditText
    private void setupOtpInputs() {
        otpFields[0].requestFocus(); // Focus vào ô đầu tiên khi mở màn hình

        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty() && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus(); // Chuyển focus sang ô kế tiếp
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Xử lý khi người dùng bấm backspace để xóa ký tự
            otpFields[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && otpFields[index].getText().toString().isEmpty() && index > 0) {
                    otpFields[index - 1].requestFocus(); // Quay lại ô trước đó
                }
                return false;
            });
        }
    }


    // Ghép OTP từ 6 ô EditText và gửi đi kiểm tra
    private void verifyOtp() {
        StringBuilder otpBuilder = new StringBuilder();
        for (EditText otpField : otpFields) {
            String digit = otpField.getText().toString().trim();
            if (digit.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            otpBuilder.append(digit);
        }

        String otp = otpBuilder.toString();
        authViewModel.checkOtp(email, otp);
    }

    // Bắt đầu đếm ngược cho OTP
    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(OTP_TIMEOUT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                tvCountdownTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvCountdownTimer.setText("00:00");
                btnResendOtp.setEnabled(true);
            }
        }.start();
    }

    // Gửi lại OTP
    private void resendOtp() {
        if (email != null) {
            authViewModel.forgotPassword(email);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void redirectToLoginPage(View view) {
        startActivity(new Intent(ConfirmOTPActivity.this, LoginActivity.class));
        finish();
    }
}
