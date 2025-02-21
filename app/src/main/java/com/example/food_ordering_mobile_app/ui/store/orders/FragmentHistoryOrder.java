package com.example.food_ordering_mobile_app.ui.store.orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OuterHistoryAdapter;
import com.example.food_ordering_mobile_app.adapters.OuterPreOrderAdapter;
import com.example.food_ordering_mobile_app.models.OrderDateGroup;
import com.example.food_ordering_mobile_app.models.OrderDateStatusGroup;
import com.example.food_ordering_mobile_app.models.OrderDetail;
import com.example.food_ordering_mobile_app.models.StoreOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentHistoryOrder extends Fragment {

    private OuterHistoryAdapter outerHistoryAdapter;
    private List<StoreOrder> storeOrderlist;
    private RecyclerView historyRecycleView;
    public FragmentHistoryOrder() {
        // Required empty public constructor
    }



    public static FragmentHistoryOrder newInstance() {
        FragmentHistoryOrder fragment = new FragmentHistoryOrder();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_history_order, container,false);
        historyRecycleView = view.findViewById(R.id.historyOrderRecyclerView);
        historyRecycleView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        storeOrderlist = new ArrayList<>();

        List<OrderDetail> orderItems1 = Arrays.asList(
                new OrderDetail(1, "Bún Bò Huế", 2, 50000),
                new OrderDetail(2, "Trà Sữa Trân Châu", 1, 30000)
        );

        List<OrderDetail> orderItems2 = Arrays.asList(
                new OrderDetail(3, "Phở Bò", 1, 60000),
                new OrderDetail(4, "Cà Phê Sữa Đá", 1, 25000)
        );

        List<StoreOrder> todayOrders = Arrays.asList(
                new StoreOrder(101, 1, orderItems1, "Nguyễn Văn A", "11:30", "11:12",1.2,"12/04/2003","12/04/2003","Done"),
                new StoreOrder(102, 2, orderItems2, "Trần Thị B", "11:30", "11:12",1.2,"12/04/2003","12/04/2003","Done")
        );

        List<StoreOrder> tomorrowOrders = Arrays.asList(
                new StoreOrder(103, 1, orderItems1, "Nguyễn Văn C", "14:00", "11:12",1.2,"12/04/2003","12/04/2003","Done"),
                new StoreOrder(104, 2, orderItems2, "Trần Thị D", "15:00", "11:12",1.2,"12/04/2003","12/04/2003","Done")
        );
        List<OrderDateStatusGroup> dateGroups = new ArrayList<>();
        dateGroups.add(new OrderDateStatusGroup("Hôm nay","Đã giao", todayOrders));
        dateGroups.add(new OrderDateStatusGroup("Ngày mai","Chưa giao", tomorrowOrders));

        outerHistoryAdapter = new OuterHistoryAdapter(dateGroups);
        historyRecycleView.setAdapter(outerHistoryAdapter);

        return view;
    }

}