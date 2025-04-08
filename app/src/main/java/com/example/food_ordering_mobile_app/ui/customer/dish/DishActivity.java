package com.example.food_ordering_mobile_app.ui.customer.dish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import com.example.food_ordering_mobile_app.adapters.GroupToppingAdapter;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.cart.CartItem;
import com.example.food_ordering_mobile_app.models.dish.DishDetail;
import com.example.food_ordering_mobile_app.models.dish.DishResponse;
import com.example.food_ordering_mobile_app.models.dish.DishStore;
import com.example.food_ordering_mobile_app.models.dish.Topping;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroup;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroupResponse;
import com.example.food_ordering_mobile_app.ui.customer.store.StoreActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;
import com.example.food_ordering_mobile_app.viewmodels.DishViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DishActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private DishViewModel dishViewModel;
    private CartViewModel cartViewModel;
    private RecyclerView toppingGroupRecyclerView;
    private GroupToppingAdapter groupToppingAdapter;
    private List<ToppingGroup> toppingGroupList;
    private TextView tvQuantity, tvDishName, tvDishDescription, tvPrice, tvTotalPrice;
    private ImageView ivDishAvatar;
    private LinearLayout quantityContainer, btnRemoveFromCart, btnAddToCart, btnGoBackContainer;
    private ImageButton btnIncrease, btnDecrease;
    private String dishId;
    private DishDetail dish;
    private List<Topping> toppingList;
    private int toppingValue = 0;
    private int totalPrice = 0;
    private CartItem matchedItem;
    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dish);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        toppingGroupRecyclerView = findViewById(R.id.toppingGroupRecyclerView);
        tvDishName = findViewById(R.id.tvDishName);
        tvDishDescription = findViewById(R.id.tvDishDescription);
        tvPrice = findViewById(R.id.tvPrice);
        ivDishAvatar = findViewById(R.id.ivDishAvatar);
        quantityContainer = findViewById(R.id.quantityContainer);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnRemoveFromCart = findViewById(R.id.btnRemoveFromCart);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnGoBackContainer = findViewById(R.id.btnGoBackContainer);

        dishId = getIntent().getStringExtra("dishId");
        matchedItem = (CartItem) getIntent().getSerializableExtra("matchedItem");

        btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));

            if(toppingList != null && !toppingList.isEmpty()) {
                toppingValue = 0;
                for(Topping topping : toppingList) {
                    toppingValue += quantity * topping.getPrice();
                }
            }

            totalPrice = quantity * dish.getPrice() + toppingValue;
            tvTotalPrice.setText(String.valueOf(formatter.format(totalPrice)));

            btnAddToCart.setVisibility(View.VISIBLE);
            btnGoBackContainer.setVisibility(View.GONE);
        });

        btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));

                if(toppingList != null && !toppingList.isEmpty()) {
                    toppingValue = 0;
                    for(Topping topping : toppingList) {
                        toppingValue += quantity * topping.getPrice();
                    }
                }

                totalPrice = quantity * dish.getPrice() + toppingValue;
                tvTotalPrice.setText(String.valueOf(formatter.format(totalPrice)));
            } else {
                quantity = 0;
                btnAddToCart.setVisibility(View.GONE);
                btnGoBackContainer.setVisibility(View.VISIBLE);
            }
        });

        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        setupDish();
        setupTopping();

        btnRemoveFromCart.setOnClickListener(v -> {
            updateCart(dish, 0);
        });

        btnAddToCart.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());

            List<String> toppingIds = new ArrayList<>();
            for (Topping topping : toppingList) {
                toppingIds.add(topping.getId());
            }

            Map<String, Object> data = new HashMap<>();
            data.put("storeId", dish.getStore());
            data.put("dishId", dish.getId());
            data.put("quantity", quantity);
            data.put("toppings", toppingIds);

            cartViewModel.updateCart(data);
        });


        cartViewModel.getUpdateCartResponse().observe(this, new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        Intent intent = new Intent(DishActivity.this, StoreActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
    }

    private void updateCart(DishDetail dish, int quantity) {
        Map<String, Object> data = new HashMap<>();
        data.put("storeId", dish.getStore());
        data.put("dishId", dish.getId());
        data.put("quantity", quantity);
        cartViewModel.updateCart(data);
    }

    private void setupDish() {
        dishViewModel.getDish(dishId);
        dishViewModel.getDishResponse().observe(this, new Observer<Resource<DishResponse>>() {
            @Override
            public void onChanged(Resource<DishResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        dish  = resource.getData().getData();

                        if(matchedItem != null) {
                            tvQuantity.setText(String.valueOf(matchedItem.getQuantity()));
                            toppingList = matchedItem.getToppings();

                            if(toppingList != null && !toppingList.isEmpty()) {
                                toppingValue = 0;
                                for(Topping topping : toppingList) {
                                    toppingValue += matchedItem.getQuantity() * topping.getPrice();
                                }
                            }

                            totalPrice = matchedItem.getQuantity() * dish.getPrice() + toppingValue;
                            tvTotalPrice.setText(String.valueOf(formatter.format(totalPrice)));
                        }

                        tvDishName.setText(resource.getData().getData().getName());
                        String description = resource.getData().getData().getDescription();
                        if (description == null || description.isEmpty()) {
                            tvDishDescription.setVisibility(View.GONE);
                        } else {
                            tvDishDescription.setVisibility(View.VISIBLE);
                            tvDishDescription.setText(description);
                        }

                        tvPrice.setText(String.valueOf(formatter.format(resource.getData().getData().getPrice())));

                        String dishAvatarUrl = resource.getData().getData().getImage() != null ? resource.getData().getData().getImage().getUrl() : null;
                        Glide.with(ivDishAvatar)
                                .asBitmap()
                                .load(dishAvatarUrl)
                                .into(new BitmapImageViewTarget(ivDishAvatar) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable roundedDrawable =
                                                RoundedBitmapDrawableFactory.create(ivDishAvatar.getResources(), resource);
                                        roundedDrawable.setCornerRadius(0);
                                        ivDishAvatar.setImageDrawable(roundedDrawable);
                                    }
                                });

                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("DishActivity", "getDishResponse error: " + resource.getData().toString());
                        break;
                }
            }
        });
    }

    private void setupTopping() {
        toppingGroupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toppingGroupList = new ArrayList<>();
        groupToppingAdapter = new GroupToppingAdapter(this, toppingGroupList, matchedItem, (selectedToppings) -> {
            toppingList = groupToppingAdapter.getSelectedTopping();
            toppingValue = 0;
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            for(Topping topping : toppingList) {
                toppingValue += quantity * topping.getPrice();
            }

            totalPrice = quantity * dish.getPrice() + toppingValue;
            tvTotalPrice.setText(String.valueOf(formatter.format(totalPrice)));
        });
        toppingGroupRecyclerView.setAdapter(groupToppingAdapter);

        dishViewModel.getToppingFromDishResponse().observe(this, new Observer<Resource<ToppingGroupResponse>>() {
            @Override
            public void onChanged(Resource<ToppingGroupResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        toppingGroupList.clear();
                        toppingGroupList.addAll(resource.getData().getData());
                        groupToppingAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        dishViewModel.getToppingFromDish(dishId);
    }

    private void refreshData() {
        setupDish();
        setupTopping();
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void openNoteModel(View view) {
        showNoteDialog();
    }

    private void showNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_dialog_note, null);
        builder.setView(dialogView);

        EditText edtNote = dialogView.findViewById(R.id.edtNote);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        ImageButton btnClose = dialogView.findViewById(R.id.btnClose); // Thêm dòng này

        AlertDialog dialog = builder.create();
        dialog.show(); // Hiển thị dialog

        btnConfirm.setOnClickListener(v -> {
            String note = edtNote.getText().toString();
            // Xử lý lưu ý được nhập
            dialog.dismiss();
        });

        btnClose.setOnClickListener(v -> dialog.dismiss()); // Đóng dialog khi nhấn nút
    }

}