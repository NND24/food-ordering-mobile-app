package com.example.food_ordering_mobile_app.ui.customer.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.ui.common.ChangingRoleActivity;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Accessing the ImageView after the view has been inflated
        ImageView avatar = view.findViewById(R.id.avatar);

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

    public void goToSetting(View view) {
        Intent intent = new Intent(requireContext(), ChangingRoleActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
    }
}

