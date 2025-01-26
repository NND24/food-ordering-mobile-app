package com.example.food_ordering_mobile_app.ui.customer.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.food_ordering_mobile_app.R;

public class PaymentMethodFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_method, container, false);

        TextView backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            NavHostFragment.findNavController(PaymentMethodFragment.this)
                    .navigate(R.id.action_payment_method_to_settings, bundle);
        });

        TextView addCardBtn = view.findViewById(R.id.addCardBtn);

        addCardBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            NavHostFragment.findNavController(PaymentMethodFragment.this)
                    .navigate(R.id.action_payment_method_to_add_card, bundle);
        });

        return view;
    }
}
