package com.example.food_ordering_mobile_app.ui.customer.cart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.food_ordering_mobile_app.adapters.CartSummaryAdapter;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.cart.CartItem;
import com.example.food_ordering_mobile_app.models.cart.CartResponse;
import com.example.food_ordering_mobile_app.models.dish.Topping;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.ui.customer.orders.OrderDetailActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartDetailActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private CartViewModel cartViewModel;
    private RecyclerView orderSummaryRecyclerView;
    private CartSummaryAdapter cartSummaryAdapter;
    private List<CartItem> cartItemList;
    private TextView tvStoreName, tvStoreDescription, tvProvisionalTotal, tvFee, tvTotalPrice;
    private ImageView ivStoreAvatar;
    private Button btnCompleteCart;
    private String storeId, deliveryAddress, customerName, customerPhonenumber, detailAddress, note, lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_detail);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        orderSummaryRecyclerView = findViewById(R.id.orderSummaryRecyclerView);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreDescription = findViewById(R.id.tvStoreDescription);
        ivStoreAvatar = findViewById(R.id.ivStoreAvatar);
        tvProvisionalTotal = findViewById(R.id.tvProvisionalTotal);
        tvFee = findViewById(R.id.tvFee);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCompleteCart = findViewById(R.id.btnCompleteCart);

        storeId = getIntent().getStringExtra("storeId");

        deliveryAddress = getIntent().getStringExtra("deliveryAddress") != null ? getIntent().getStringExtra("deliveryAddress") : "";
        customerName = getIntent().getStringExtra("customerName") != null ? getIntent().getStringExtra("customerName") : "";
        customerPhonenumber = getIntent().getStringExtra("customerPhonenumber") != null ? getIntent().getStringExtra("customerPhonenumber") : "";
        detailAddress = getIntent().getStringExtra("detailAddress") != null ? getIntent().getStringExtra("detailAddress") : "";
        note = getIntent().getStringExtra("note") != null ? getIntent().getStringExtra("note") : "";
        lat = getIntent().getStringExtra("lat") != null ? getIntent().getStringExtra("lat") : "";
        lon = getIntent().getStringExtra("lon") != null ? getIntent().getStringExtra("lon") : "";

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        setupCartDetail();

        btnCompleteCart.setOnClickListener(v -> handleCompleteCart());
    }

    private void setupCartDetail() {
        orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();
        cartSummaryAdapter = new CartSummaryAdapter(this, cartItemList);
        orderSummaryRecyclerView.setAdapter(cartSummaryAdapter);

        cartViewModel.getUserCartInStoreResponse().observe(this, new Observer<Resource<CartResponse>>() {
            @Override
            public void onChanged(Resource<CartResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        tvStoreName.setText(resource.getData().getData().getStore().getName());
                        tvStoreDescription.setText(resource.getData().getData().getStore().getDescription());

                        // Xóa danh sách cũ và cập nhật danh sách mới
                        cartItemList.clear();
                        cartItemList.addAll(resource.getData().getData().getItems());
                        cartSummaryAdapter.notifyDataSetChanged();

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
                        for (CartItem item : cartItemList) {
                            double dishPrice = item.getDish().getPrice();
                            double totalToppingPrice = 0;

                            for (Topping topping : item.getToppings()) {
                                totalToppingPrice += topping.getPrice();
                            }

                            // Tính giá tiền của mỗi món và cộng vào tổng giỏ hàng
                            totalCartPrice += (dishPrice + totalToppingPrice) * item.getQuantity();
                        }

                        // Hiển thị tổng giá tiền
                        tvProvisionalTotal.setText(String.format("%.0f", totalCartPrice));
                        tvFee.setText(String.format("%.0f", totalCartPrice));
                        tvTotalPrice.setText(String.format("%.0f", totalCartPrice));

                        break;

                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        cartViewModel.getUserCartInStore(storeId);
    }

    private void handleCompleteCart() {
        // Kiểm tra các trường bắt buộc có giá trị hợp lệ
        if (storeId == null || storeId.isEmpty()) {
            Toast.makeText(this, "Store ID không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (deliveryAddress == null || deliveryAddress.isEmpty() || lat.equals("200")  || lon.equals("200")) {
            Toast.makeText(this, "Địa chỉ giao hàng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customerName == null || customerName.isEmpty()) {
            Toast.makeText(this, "Tên khách hàng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customerPhonenumber == null || customerPhonenumber.isEmpty()) {
            Toast.makeText(this, "Số điện thoại khách hàng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("storeId", storeId);
        data.put("paymentMethod", "cash");
        data.put("deliveryAddress", deliveryAddress);
        data.put("customerName", customerName);
        data.put("customerPhonenumber", customerPhonenumber);
        data.put("detailAddress", detailAddress);
        data.put("note", note);

        List<Double> location = new ArrayList<>();
        location.add(Double.parseDouble(getIntent().getStringExtra("lat")));
        location.add(Double.parseDouble(getIntent().getStringExtra("lon")));
        data.put("location", location);

        // Gọi phương thức completeCart trong ViewModel
        cartViewModel.completeCart(data);
    }

    private void refreshData() {
        cartViewModel.getUserCartInStore(storeId);
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void goToOrderDetail(View view) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        startActivity(intent);
    }
}