package com.example.food_ordering_mobile_app.ui.customer.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.food_ordering_mobile_app.R;

public class AddCardFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_card, container, false);

        TextView backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            NavHostFragment.findNavController(AddCardFragment.this)
                    .navigate(R.id.action_add_card_to_payment_method, bundle);
        });

        return view;
    }
}
