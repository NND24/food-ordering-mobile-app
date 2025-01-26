package com.example.food_ordering_mobile_app.ui.customer.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.food_ordering_mobile_app.R;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Accessing the ImageView after the view has been inflated
        ImageView avatar = view.findViewById(R.id.avatar);

        // Load the image using Glide and make it circular
        Glide.with(requireContext())  // Using the correct context here
                .load(R.drawable.dess_1)
                .circleCrop()  // Make the image circular
                .into(avatar);

        TextView backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            NavHostFragment.findNavController(ProfileFragment.this)
                    .navigate(R.id.action_profile_to_settings, bundle);
        });

        return view;
    }
}
