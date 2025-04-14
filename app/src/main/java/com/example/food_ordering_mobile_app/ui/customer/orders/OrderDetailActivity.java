package com.example.food_ordering_mobile_app.ui.customer.orders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.food_ordering_mobile_app.adapters.OrderSummaryAdapter;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.chat.Chat;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.order.OrderTopping;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.ui.customer.messages.DetailMessageActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.ChatViewModel;
import com.example.food_ordering_mobile_app.viewmodels.OrderViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private OrderViewModel orderViewModel;
    private ChatViewModel chatViewModel;
    private RecyclerView orderSummaryRecyclerView;
    private OrderSummaryAdapter orderSummaryAdapter;
    private List<OrderItem> orderItemList;
    private TextView tvStoreName, tvStoreDescription, tvProvisionalTotal, tvFee, tvCustomerName, tvCustomerPhonenumber, tvCustomerAddress, tvOrderStatus, textView5, tvShipperName;
    private ImageView ivStoreAvatar, ivShipperAvatar, ivStoreProgress, ivShipperProgress, ivDoneProgress;
    private String orderId;
    private Button btnTrackOrder;
    private LinearLayout shipper_info_container, order_progress_container;
    private ProgressBar pendingProgressBar, storeProgressBar, shipperProgressBar;
    private ImageButton btnCallWithShipper, btnChatWithShipper, btnChatWithStore;

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
        btnTrackOrder = findViewById(R.id.btnTrackOrder);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        shipper_info_container = findViewById(R.id.shipper_info_container);
        textView5 = findViewById(R.id.textView5);
        tvShipperName = findViewById(R.id.tvShipperName);
        ivShipperAvatar = findViewById(R.id.ivShipperAvatar);
        pendingProgressBar = findViewById(R.id.pendingProgressBar);
        storeProgressBar = findViewById(R.id.storeProgressBar);
        shipperProgressBar = findViewById(R.id.shipperProgressBar);
        ivStoreProgress = findViewById(R.id.ivStoreProgress);
        ivShipperProgress = findViewById(R.id.ivShipperProgress);
        ivDoneProgress = findViewById(R.id.ivDoneProgress);
        order_progress_container = findViewById(R.id.order_progress_container);
        btnCallWithShipper = findViewById(R.id.btnCallWithShipper);
        btnChatWithShipper = findViewById(R.id.btnChatWithShipper);
        btnChatWithStore = findViewById(R.id.btnChatWithStore);

        orderId = getIntent().getStringExtra("orderId");

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        setupOrderDetail();

        btnTrackOrder.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, TrackOrderActivity.class);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
        });

        chatViewModel.getCreateChatResponse().observe(this, new Observer<Resource<Chat>>() {
            @Override
            public void onChanged(Resource<Chat> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        Chat chat = resource.getData();
                        Intent intent = new Intent(OrderDetailActivity.this, DetailMessageActivity.class);
                        intent.putExtra("chatId", chat.getId());
                        startActivity(intent);
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
    }

    private void setupOrderDetail() {
        orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemList = new ArrayList<>();
        orderSummaryAdapter = new OrderSummaryAdapter(this, orderItemList);
        orderSummaryRecyclerView.setAdapter(orderSummaryAdapter);

        orderViewModel.getOrderDetailResponse().observe(this, new Observer<Resource<ApiResponse<Order>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Order>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        Order order = resource.getData().getData();
                        tvStoreName.setText(order.getStore().getName());
                        tvStoreDescription.setText(order.getStore().getDescription());
                        tvCustomerName.setText(order.getCustomerName());
                        tvCustomerPhonenumber.setText(order.getCustomerPhonenumber());
                        tvCustomerAddress.setText(order.getShipLocation().getAddress());

                        btnChatWithStore.setOnClickListener(v -> {
                            chatViewModel.createChat(order.getStore().getOwner());
                        });


                        if(order.getStatus().equals("cancelled")) {
                            tvOrderStatus.setText("Đơn hàng đã bị hủy");
                        } else if(order.getStatus().equals("pending")) {
                            tvOrderStatus.setText("Đơn hàng đang chờ quán xác nhận");
                        } else if(order.getStatus().equals("confirmed")) {
                            tvOrderStatus.setText("Quán đã xác nhận đơn hàng");
                        } else if(order.getStatus().equals("preparing")) {
                            tvOrderStatus.setText("Quán đang chuẩn bị món ăn");
                        } else if(order.getStatus().equals("finished")) {
                            tvOrderStatus.setText("Món ăn đã hoàn thành");
                        } else if(order.getStatus().equals("taken")) {
                            tvOrderStatus.setText("Shipper đã lấy món ăn");
                        } else if(order.getStatus().equals("delivering")) {
                            tvOrderStatus.setText("Shipper đang vận chuyển đến chỗ bạn");
                        } else if(order.getStatus().equals("delivered")) {
                            tvOrderStatus.setText("Đơn hàng đã được giao tới nơi");
                        } else if(order.getStatus().equals("done")) {
                            tvOrderStatus.setText("Đơn hàng được giao hoàn tất");
                        } else {
                            tvOrderStatus.setVisibility(View.GONE);
                        }

                        if(order.getStatus().equals("cancelled")) {
                            order_progress_container.setVisibility(View.GONE);
                        } else{
                            order_progress_container.setVisibility(View.VISIBLE);
                        }

                        if (List.of("confirmed", "preparing", "finished", "taken", "delivering", "delivered", "done")
                                .contains(order.getStatus())) {
                            ivStoreProgress.setImageResource(R.drawable.ic_cooking_active_24);
                        } else {
                            ivStoreProgress.setImageResource(R.drawable.ic_cooking_24);
                        }

                        if (List.of("taken", "delivering", "delivered", "done")
                                .contains(order.getStatus())) {
                            ivShipperProgress.setImageResource(R.drawable.ic_delivery_active_24);
                        } else {
                            ivShipperProgress.setImageResource(R.drawable.ic_delivery_24);
                        }

                        if (List.of("done", "delivered")
                                .contains(order.getStatus())) {
                            ivDoneProgress.setImageResource(R.drawable.ic_home_active_24);
                        } else {
                            ivDoneProgress.setImageResource(R.drawable.ic_home_24);
                        }

                        if (List.of("preparing", "finished", "taken", "delivering", "delivered", "done")
                                .contains(order.getStatus())) {
                            storeProgressBar.setProgress(100);
                        } else {
                            storeProgressBar.setProgress(0);
                        }

                        if (List.of("delivering", "delivered", "done")
                                .contains(order.getStatus())) {
                            shipperProgressBar.setProgress(100);
                        } else {
                            shipperProgressBar.setProgress(0);
                        }

                        if(order.getShipper() != null) {
                            textView5.setVisibility(View.VISIBLE);
                            shipper_info_container.setVisibility(View.VISIBLE);

                            tvShipperName.setText(order.getShipper().getName());

                            btnCallWithShipper.setOnClickListener(v -> {
                                String phoneNumber = order.getShipper().getPhonenumber();

                                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + phoneNumber));
                                    v.getContext().startActivity(intent);
                                } else {
                                    Toast.makeText(v.getContext(), "Shipper không có số điện thoại", Toast.LENGTH_SHORT).show();
                                }
                            });

                            btnChatWithShipper.setOnClickListener(v -> {
                                chatViewModel.createChat(order.getShipper().getId());
                            });

                            String shipperAvatarUrl = order.getShipper().getAvatar() != null ? order.getShipper().getAvatar().getUrl() : null;
                            Glide.with(ivShipperAvatar)
                                    .asBitmap()
                                    .load(shipperAvatarUrl)
                                    .override(60, 60)
                                    .centerCrop()
                                    .into(new BitmapImageViewTarget(ivShipperAvatar) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable roundedDrawable =
                                                    RoundedBitmapDrawableFactory.create(ivShipperAvatar.getResources(), resource);
                                            roundedDrawable.setCornerRadius(6);
                                            ivShipperAvatar.setImageDrawable(roundedDrawable);
                                        }
                                    });
                        } else {
                            textView5.setVisibility(View.GONE);
                            shipper_info_container.setVisibility(View.GONE);
                        }

                        // Xóa danh sách cũ và cập nhật danh sách mới
                        orderItemList.clear();
                        orderItemList.addAll(order.getItems());
                        orderSummaryAdapter.notifyDataSetChanged();

                        String storeAvatarUrl = order.getStore().getAvatar() != null ? order.getStore().getAvatar().getUrl() : null;
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
                        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                        tvProvisionalTotal.setText(String.valueOf(formatter.format(totalCartPrice)));
                        tvFee.setText(String.valueOf(formatter.format(totalCartPrice)));

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
        finish();
    }
}