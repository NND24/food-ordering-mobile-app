package com.example.food_ordering_mobile_app.ui.customer.orders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OrderCurrentAdapter;
import com.example.food_ordering_mobile_app.adapters.OrderHistoryAdapter;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.ListOrderResponse;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private OrderViewModel orderViewModel;
    private RecyclerView orderCurrentRecyclerView;
    private OrderCurrentAdapter orderCurrentAdapter;
    private List<Order> orderCurrentList;
    private RecyclerView orderHistoryRecyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<Order> orderHistoryList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        orderCurrentRecyclerView = view.findViewById(R.id.orderCurrentRecyclerView);
        orderHistoryRecyclerView = view.findViewById(R.id.orderHistoryRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        setupCurrentOrder();
        setupHistoryOrder();

        return view;
    }

    private void setupCurrentOrder() {
        orderCurrentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderCurrentList = new ArrayList<>();
        orderCurrentAdapter = new OrderCurrentAdapter(getContext(), orderCurrentList);
        orderCurrentRecyclerView.setAdapter(orderCurrentAdapter);

        orderViewModel.getCurrentOrderResponse().observe(getViewLifecycleOwner(), new Observer<Resource<ListOrderResponse>>() {
            @Override
            public void onChanged(Resource<ListOrderResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        orderCurrentList.clear();
                        List<Order> filteredOrders = new ArrayList<>();
                        for (Order order : resource.getData().getData()) {
                            if (!"done".equalsIgnoreCase(order.getStatus())) {
                                filteredOrders.add(order);
                            }
                        }
                        orderCurrentList.addAll(filteredOrders);
                        orderCurrentAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        orderViewModel.getCurrentOrder();
    }

    private void setupHistoryOrder() {
        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderHistoryList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(getContext(), orderHistoryList);
        orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);

        orderViewModel.getHistoryOrderResponse().observe(getViewLifecycleOwner(), new Observer<Resource<ListOrderResponse>>() {
            @Override
            public void onChanged(Resource<ListOrderResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        orderHistoryList.clear();
                        List<Order> filteredOrders = new ArrayList<>();
                        for (Order order : resource.getData().getData()) {
                            if ("done".equalsIgnoreCase(order.getStatus())) {
                                filteredOrders.add(order);
                            }
                        }
                        orderHistoryList.addAll(filteredOrders);
                        orderHistoryAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        orderViewModel.getHistoryOrder();
    }

    private void refreshData() {
        orderViewModel.getCurrentOrder();
        orderViewModel.getHistoryOrder();
    }

    private void goToActivity(Class<?> activityClass) {
        Intent intent = new Intent(requireContext(), activityClass);
        startActivity(intent);
    }
}