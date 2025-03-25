package com.example.food_ordering_mobile_app.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.developer.gbuttons.GoogleSignInButton;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.User;
import com.example.food_ordering_mobile_app.ui.customer.MainCustomerActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.AuthViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText edtEmail, edtPassword;
    private ImageButton btnShowPassword;
    private Button btnLogin, btnLoginFacebook;
    private boolean isPasswordVisible = true;
    private GoogleSignInButton btnLoginGoogle;
    private GoogleSignInOptions gOptions;
    private GoogleSignInClient gClient;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo UI
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnShowPassword = findViewById(R.id.btnShowPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);

        // Toggle mật khẩu
        btnShowPassword.setOnClickListener(v -> togglePasswordVisibility(edtPassword, btnShowPassword));

        // Khởi tạo ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Quan sát kết quả đăng nhập
        authViewModel.getLoginResponse().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        User user = resource.getData();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainCustomerActivity.class));
                        finish();
                        break;
                    case ERROR:
                        Toast.makeText(LoginActivity.this, resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // Xử lý đăng nhập bằng email và mật khẩu
        btnLogin.setOnClickListener(view -> handleLogin());

        // Cấu hình đăng nhập Google
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        gClient = GoogleSignIn.getClient(this, gOptions);

        // Đăng xuất trước khi đăng nhập để tránh lưu tài khoản cũ
        gClient.signOut();

        // Khởi tạo ActivityResultLauncher để nhận kết quả đăng nhập Google
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            handleGoogleSignInResult(task);
                        }
                    }
                });

        // Xử lý đăng nhập Google khi nhấn vào nút
        btnLoginGoogle.setOnClickListener(view -> {
            Intent signInIntent = gClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);
        });
    }

    private void handleLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        authViewModel.login(email, password);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                String name = account.getDisplayName();
                String email = account.getEmail();

                authViewModel.loginWithGoogle(name, email);
            }
        } catch (ApiException e) {
            int statusCode = e.getStatusCode();
            Log.e("Google Sign-In", "Đăng nhập thất bại: " + GoogleSignInStatusCodes.getStatusCodeString(statusCode));
            Toast.makeText(LoginActivity.this, "Lỗi đăng nhập: " + GoogleSignInStatusCodes.getStatusCodeString(statusCode), Toast.LENGTH_SHORT).show();
        }
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

    public void redirectToRegisterPage(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    public void redirectToForgotPasswordPage(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        finish();
    }
}
