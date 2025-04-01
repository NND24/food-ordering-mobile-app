package com.example.food_ordering_mobile_app.ui.customer.orders;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.CartSummaryAdapter;
import com.example.food_ordering_mobile_app.adapters.OrderSummaryAdapter;
import com.example.food_ordering_mobile_app.models.cart.CartResponse;
import com.example.food_ordering_mobile_app.models.dish.Topping;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.order.OrderResponse;
import com.example.food_ordering_mobile_app.models.order.OrderTopping;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private OrderViewModel orderViewModel;
    private RecyclerView orderSummaryRecyclerView;
    private OrderSummaryAdapter orderSummaryAdapter;
    private List<OrderItem> orderItemList;
    private TextView tvStoreName, tvStoreDescription, tvProvisionalTotal, tvFee, tvCustomerName, tvCustomerPhonenumber, tvCustomerAddress;
    private ImageView ivStoreAvatar;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        orderSummaryRecyclerView = findViewById(R.id.orderSummaryRecyclerView);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreDescription = findViewById(R.id.tvStoreDescription);
        ivStoreAvatar = findViewById(R.id.ivStoreAvatar);
        tvProvisionalTotal = findViewById(R.id.tvProvisionalTotal);
        tvFee = findViewById(R.id.tvFee);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhonenumber = findViewById(R.id.tvCustomerPhonenumber);
        tvCustomerAddress = findViewById(R.id.tvCustomerAddress);

        orderId = getIntent().getStringExtra("orderId");

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        setupCartDetail();
    }

    private void setupCartDetail() {
        orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemList = new ArrayList<>();
        orderSummaryAdapter = new OrderSummaryAdapter(this, orderItemList);
        orderSummaryRecyclerView.setAdapter(orderSummaryAdapter);

        orderViewModel.getOrderDetailResponse().observe(this, new Observer<Resource<OrderResponse>>() {
            @Override
            public void onChanged(Resource<OrderResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        tvStoreName.setText(resource.getData().getData().getStore().getName());
                        tvStoreDescription.setText(resource.getData().getData().getStore().getDescription());
                        tvCustomerName.setText(resource.getData().getData().getCustomerName());
                        tvCustomerPhonenumber.setText(resource.getData().getData().getCustomerPhonenumber());
                        tvCustomerAddress.setText(resource.getData().getData().getShipLocation().getAddress());

                        // Xóa danh sách cũ và cập nhật danh sách mới
                        orderItemList.clear();
                        orderItemList.addAll(resource.getData().getData().getItems());
                        orderSummaryAdapter.notifyDataSetChanged();

                        String storeAvatarUrl = resource.getData().getData().getStore().getAvatar() != null ? resource.getData().getData().getStore().getAvatar().getUrl() : null;
                        Glide.with(ivStoreAvatar)
                                .asBitmap()
                                .load(storeAvatarUrl)
                                .override(60, 60)
                                .centerCrop()
                                .into(new BitmapImageViewTarget(ivStoreAvatar) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable roundedDrawable =
                                                RoundedBitmapDrawableFactory.create(ivStoreAvatar.getResources(), resource);
                                        roundedDrawable.setCornerRadius(6);
                                        ivStoreAvatar.setImageDrawable(roundedDrawable);
                                    }
                                });

                        // Tính tổng giá tiền của giỏ hàng
                        double totalCartPrice = 0;
                        for (OrderItem item : orderItemList) {
                            double dishPrice = item.getDish().getPrice();
                            double totalToppingPrice = 0;

                            for (OrderTopping topping : item.getToppings()) {
                                totalToppingPrice += topping.getPrice();
                            }

                            // Tính giá tiền của mỗi món và cộng vào tổng giỏ hàng
                            totalCartPrice += (dishPrice + totalToppingPrice) * item.getQuantity();
                        }

                        // Hiển thị tổng giá tiền
                        tvProvisionalTotal.setText(String.format("%.0f", totalCartPrice));
                        tvFee.setText(String.format("%.0f", totalCartPrice));

                        break;

                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        orderViewModel.getOrderDetail(orderId);
    }

    private void refreshData() {
        orderViewModel.getOrderDetail(orderId);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}