package com.example.food_ordering_mobile_app.ui.customer.account;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.ui.common.LoginActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.AuthViewModel;

public class ChangePasswordFragment extends Fragment {
    private AuthViewModel authViewModel;
    private EditText edtOldPassword, edtPassword, edtConfirmPassword;
    private ImageButton btnShowOldPassword, btnShowPassword, btnShowConfirmPassword;
    private Button btnResetPassword;
    private boolean isOldPasswordVisible = true;
    private boolean isPasswordVisible = true;
    private boolean isConfirmPasswordVisible = true;
    private String email;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        edtOldPassword = view.findViewById(R.id.edtOldPassword);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        btnShowOldPassword = view.findViewById(R.id.btnShowOldPassword);
        btnShowPassword = view.findViewById(R.id.btnShowPassword);
        btnShowConfirmPassword = view.findViewById(R.id.btnShowConfirmPassword);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);

        // Initialize ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe reset password response
        authViewModel.getChangePasswordResponse().observe(getViewLifecycleOwner(), new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        // Hiển thị progress bar
                        break;
                    case SUCCESS:
                        break;
                    case ERROR:
                        // Ẩn progress bar và hiển thị thông báo lỗi
                        String errorMessage = resource.getMessage();
                        break;
                }
            }
        });

        // Handle reset password
        btnResetPassword.setOnClickListener(v -> handleChangePassword());

        // Handle show/hide password
        btnShowOldPassword.setOnClickListener(v -> togglePasswordVisibility(edtOldPassword, btnShowOldPassword));
        btnShowPassword.setOnClickListener(v -> togglePasswordVisibility(edtPassword, btnShowPassword));
        btnShowConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(edtConfirmPassword, btnShowConfirmPassword));

        return view;
    }

    private void handleChangePassword() {
        String oldPassword = edtPassword.getText().toString();
        String newPassword = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if(oldPassword.isEmpty()) {
            edtOldPassword.setError("Vui lòng nhập mật khẩu cũ");
            edtOldPassword.requestFocus();
            return;
        }

        // Kiểm tra dữ liệu
        if (newPassword.isEmpty() || newPassword.length() < 6) {
            edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            edtPassword.requestFocus();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            edtConfirmPassword.setError("Mật khẩu nhập lại không khớp");
            edtConfirmPassword.requestFocus();
            return;
        }

        // Gửi dữ liệu lên ViewModel
        authViewModel.changePassword(oldPassword, newPassword);
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
}
