package com.example.food_ordering_mobile_app.ui.customer.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OrderCurrentAdapter;
import com.example.food_ordering_mobile_app.adapters.OrderHistoryAdapter;
import com.example.food_ordering_mobile_app.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    private RecyclerView orderCurrentRecyclerView;
    private OrderCurrentAdapter orderCurrentAdapter;
    private List<Order> orderCurrentList;

    private RecyclerView orderHistoryRecyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<Order> orderHistoryList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // Show current order
        orderCurrentRecyclerView = view.findViewById(R.id.orderCurrentRecyclerView);
        orderCurrentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderCurrentList = new ArrayList<>();
        orderCurrentList.add(new Order("Minute by tuk tuk", 4, "123 Quoc Lo 14", String.valueOf(R.drawable.item_1)));
        orderCurrentList.add(new Order("Phở Lý Quoc Su", 2, "123 Quoc Lo 14", String.valueOf(R.drawable.item_2)));

        orderCurrentAdapter = new OrderCurrentAdapter(getContext(), orderCurrentList);
        orderCurrentRecyclerView.setAdapter(orderCurrentAdapter);

        //  Show order history
        orderHistoryRecyclerView = view.findViewById(R.id.orderHistoryRecyclerView);
        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderHistoryList = new ArrayList<>();
        orderHistoryList.add(new Order("Minute by tuk tuk", 4, "123 Quoc Lo 14", String.valueOf(R.drawable.item_1)));
        orderHistoryList.add(new Order("Phở Lý Quoc Su", 2, "123 Quoc Lo 14", String.valueOf(R.drawable.item_2)));

        orderHistoryAdapter = new OrderHistoryAdapter(getContext(), orderHistoryList);
        orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);

        return view;
    }
}