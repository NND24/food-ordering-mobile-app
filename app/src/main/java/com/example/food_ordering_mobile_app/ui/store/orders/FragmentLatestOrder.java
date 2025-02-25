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
import com.example.food_ordering_mobile_app.adapters.LatestOrderAdapter;
import com.example.food_ordering_mobile_app.models.OrderDetail;
import com.example.food_ordering_mobile_app.models.StoreOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentLatestOrder extends Fragment {
    private LatestOrderAdapter latestOrderAdapter;
    private List<StoreOrder> storeOrderList;
    private RecyclerView latestOrderRecyclerView;


    public FragmentLatestOrder() {
        // Required empty public constructor
    }

    public static FragmentLatestOrder newInstance(String param1, String param2) {
        FragmentLatestOrder fragment = new FragmentLatestOrder();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_latest_order, container, false);
        latestOrderRecyclerView = view.findViewById(R.id.latestOrderRecyclerView);
        latestOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Tạo danh sách đơn hàng giả lập
        storeOrderList = new ArrayList<>();

        // Tạo các chi tiết đơn hàng
        List<OrderDetail> orderItems1 = Arrays.asList(
                new OrderDetail(1, "Bún Bò Huế", 2, 50000),
                new OrderDetail(2, "Trà Sữa Trân Châu", 1, 30000)
        );

        List<OrderDetail> orderItems2 = Arrays.asList(
                new OrderDetail(3, "Phở Bò", 1, 60000),
                new OrderDetail(4, "Cà Phê Sữa Đá", 1, 25000)
        );

        // Thêm đơn hàng vào danh sách
        storeOrderList.add(new StoreOrder(101, 1, orderItems1, "Nguyễn Văn A", "11:30","Lấy đơn trong 24 phút",1.2,"12/12/2024","12/12/2024"));
        storeOrderList.add(new StoreOrder(102, 2, orderItems2, "Trần Thị B","11:30","Lấy đơn trong 24 phút",1.2,"12/12/2024","12/12/2024"));

        // Gán adapter
        latestOrderAdapter = new LatestOrderAdapter(getContext(), storeOrderList);
        latestOrderRecyclerView.setAdapter(latestOrderAdapter);

        return view;
    }

}