package com.example.food_ordering_mobile_app.ui.customer.cart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.CartAdapter;
import com.example.food_ordering_mobile_app.models.MessageResponse;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.cart.ListCartResponse;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private CartViewModel cartViewModel;
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;
    private Button btnRemoveAllCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        btnRemoveAllCart = findViewById(R.id.btnRemoveAllCart);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        setupUserCart();

        btnRemoveAllCart.setOnClickListener(this::handleRemoveAllCart);

        cartViewModel.getClearCartResponse().observe(this, new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        cartViewModel.getUserCart();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        cartViewModel.getClearCartItemResponse().observe(this, new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        cartViewModel.getUserCart();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
    }

    private void handleRemoveAllCart(View view) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa tất cả các mục giỏ hàng?")
                .setPositiveButton("Có", (dialog, which) -> {
                    cartViewModel.clearCart();
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void setupUserCart() {
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(CartActivity.this, this, cartList);
        cartRecyclerView.setAdapter(cartAdapter);

        cartViewModel.getUserCartResponse().observe(this, new Observer<Resource<ListCartResponse>>() {
            @Override
            public void onChanged(Resource<ListCartResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        cartList.clear();
                        cartList.addAll(resource.getData().getData());
                        cartAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        cartList.clear();
                        cartAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        cartViewModel.getUserCart();
    }

    private void refreshData() {
        cartViewModel.getUserCart();
    }
}