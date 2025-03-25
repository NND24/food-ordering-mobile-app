package com.example.food_ordering_mobile_app.ui.customer.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.User;
import com.example.food_ordering_mobile_app.ui.common.ChangingRoleActivity;
import com.example.food_ordering_mobile_app.ui.common.LoginActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewmodels.AuthViewModel;
import com.example.food_ordering_mobile_app.viewmodels.UserViewModel;

public class SettingsFragment extends Fragment {
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private ImageView ivAvatar;
    private TextView tvUserName, tvPhonenumber;
    private Button btnLogout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ivAvatar = view.findViewById(R.id.ivAvatar);
        btnLogout = view.findViewById(R.id.btnLogout);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvPhonenumber = view.findViewById(R.id.tvPhonenumber);

        // Initialize AuthViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        btnLogout.setOnClickListener(v -> handleLogout());

        // Observe login response
        authViewModel.getLogoutResponse().observe(getViewLifecycleOwner(), new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        // Show progress bar
                        break;
                    case SUCCESS:
                        Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case ERROR:
                        String errorMessage = resource.getMessage();
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        LinearLayout profileContainer = view.findViewById(R.id.profile_container);

        profileContainer.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            NavHostFragment.findNavController(SettingsFragment.this)
                    .navigate(R.id.action_settings_to_profile, bundle);
        });

        Button paymentMethodBtn = view.findViewById(R.id.payment_method_btn);

        paymentMethodBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            NavHostFragment.findNavController(SettingsFragment.this)
                    .navigate(R.id.action_settings_to_payment_method, bundle);
        });

        Button goToSettingBtn = view.findViewById(R.id.setting_button);
        goToSettingBtn.setOnClickListener(this::goToSetting);

        // Initialize UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        User savedUser = SharedPreferencesHelper.getInstance(requireContext()).getCurrentUser();
        if (savedUser != null) {
            tvUserName.setText(savedUser.getName());
            tvPhonenumber.setText(savedUser.getPhonenumber());

            // Lấy URL avatar từ savedUser
            String avatarUrl = savedUser.getAvatar() != null ? savedUser.getAvatar().getUrl() : null;

            Glide.with(requireContext())
                    .asBitmap()
                    .load(avatarUrl != null ? avatarUrl : R.drawable.default_avatar)
                    .apply(new RequestOptions().override(60, 60).centerCrop())
                    .into(new BitmapImageViewTarget(ivAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable roundedDrawable =
                                    RoundedBitmapDrawableFactory.create(requireContext().getResources(), resource);
                            roundedDrawable.setCornerRadius(6);
                            ivAvatar.setImageDrawable(roundedDrawable);
                        }
                    });
        }

        return view;
    }

    private void handleLogout() {
        authViewModel.logout(requireContext());
    }

    public void goToSetting(View view) {
        Intent intent = new Intent(requireContext(), ChangingRoleActivity.class);
        startActivity(intent);
    }
}

