package com.example.food_ordering_mobile_app.ui.store.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OrderConfirmedAdapter;
import com.example.food_ordering_mobile_app.models.OrderDetail;
import com.example.food_ordering_mobile_app.models.StoreOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentConfirmedOrder extends Fragment {
    private RecyclerView recyclerView;
    private OrderConfirmedAdapter adapter;
    private List<StoreOrder> orderList;

    public FragmentConfirmedOrder() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmed_order, container, false);

        recyclerView = view.findViewById(R.id.confirmOrderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load example data
        loadExampleData();

        // Initialize and set adapter
        adapter = new OrderConfirmedAdapter(getContext(), orderList, order -> {
            // Handle order click event
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadExampleData() {
        orderList = new ArrayList<>();
        List<OrderDetail> orderItems1 = Arrays.asList(
                new OrderDetail(1, "Bún Bò Huế", 2, 50000),
                new OrderDetail(2, "Trà Sữa Trân Châu", 1, 30000)
        );

        List<OrderDetail> orderItems2 = Arrays.asList(
                new OrderDetail(3, "Phở Bò", 1, 60000),
                new OrderDetail(4, "Cà Phê Sữa Đá", 1, 25000)
        );
        orderList.add(new StoreOrder(1, 1, orderItems1, "10:30 AM", "12/2", "Confirmed","John Doe" ));
        orderList.add(new StoreOrder(2, 2,orderItems2,  "11:00 AM", "12/2", "Confirmed", "Alice Smith"));
        orderList.add(new StoreOrder(3, 3,orderItems2, "11:30 AM", "12/2", "Confirmed", "Bob Johnson"));
    }
}
