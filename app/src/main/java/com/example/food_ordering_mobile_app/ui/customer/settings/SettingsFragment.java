package com.example.food_ordering_mobile_app.ui.customer.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.User;
import com.example.food_ordering_mobile_app.ui.common.ChangingRoleActivity;
import com.example.food_ordering_mobile_app.ui.common.LoginActivity;
import com.example.food_ordering_mobile_app.ui.customer.MainCustomerActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.AuthViewModel;

public class SettingsFragment extends Fragment {
    private AuthViewModel authViewModel;
    ImageView avatar;
    Button btnLogout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        avatar = view.findViewById(R.id.avatar);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Initialize ViewModel
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

        // Load the image using Glide and make it circular
        Glide.with(requireContext())  // Using the correct context here
                .load(R.drawable.dess_1)
                .circleCrop()  // Make the image circular
                .into(avatar);

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

