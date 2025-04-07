package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishStore;
import com.example.food_ordering_mobile_app.ui.customer.dish.DishActivity;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;
import com.example.food_ordering_mobile_app.viewmodels.LocationViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishBigAdapter extends RecyclerView.Adapter<DishBigAdapter.ViewHolder> {
    private Context context;
    private List<DishStore> dishList;
    private OnDishClickListener onDishClickListener;
    private FragmentActivity activity;
    public interface OnDishClickListener {
        void onDishClick(DishStore dish);
    }

    public DishBigAdapter(Context context, List<DishStore> dishList) {
        this.context = context;
        this.dishList = dishList;
    }

    public DishBigAdapter(FragmentActivity activity, Context context, List<DishStore> dishList, OnDishClickListener onDishClickListener) {
        this.activity = activity;
        this.context = context;
        this.dishList = dishList != null ? dishList : new ArrayList<>();
        this.onDishClickListener = onDishClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dish_big, parent, false);
        return new ViewHolder(view);
    }

    private void updateCart(DishStore dish, int quantity, CartViewModel cartViewModel) {
        Map<String, Object> data = new HashMap<>();
        data.put("storeId", dish.getStore());
        data.put("dishId", dish.getId());
        data.put("quantity", quantity);
        cartViewModel.updateCart(data);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DishStore dish = dishList.get(position);

        holder.name.setText(dish.getName());
        holder.price.setText(String.valueOf(dish.getPrice()));

        String dishImageUrl = dish.getImage() != null ? dish.getImage().getUrl() : null;
        Glide.with(context)
                .asBitmap()
                .load(dishImageUrl)
                .into(new BitmapImageViewTarget(holder.dishAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedDrawable.setCornerRadius(6);
                        holder.dishAvatar.setImageDrawable(roundedDrawable);
                    }
                });

        CartViewModel cartViewModel = new ViewModelProvider(activity).get(CartViewModel.class);

        boolean[] isAddToCartClicked = {false};

        if(dish.getToppingGroups().isEmpty()) {
            holder.btnAddToCart.setOnClickListener(v -> {
                holder.btnAddToCart.setVisibility(View.GONE);
                holder.quantityContainer.setVisibility(View.VISIBLE);
                holder.tvQuantity.setText("1");
                isAddToCartClicked[0] = true;
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                updateCart(dish, quantity, cartViewModel);
            });

            holder.btnIncrease.setOnClickListener(v -> {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                quantity++;
                holder.tvQuantity.setText(String.valueOf(quantity));
                updateCart(dish, quantity, cartViewModel);
            });

            holder.btnDecrease.setOnClickListener(v -> {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                if (quantity > 1) {
                    quantity--;
                    holder.tvQuantity.setText(String.valueOf(quantity));
                    updateCart(dish, quantity, cartViewModel);
                } else {
                    // If quantity reaches 0, hide quantity container and show the button again
                    holder.quantityContainer.setVisibility(View.GONE);
                    holder.btnAddToCart.setVisibility(View.VISIBLE);
                    updateCart(dish, 0, cartViewModel);
                }
            });
        } else {
            holder.btnAddToCart.setOnClickListener(v -> {
                Intent intent = new Intent(context, DishActivity.class);
                intent.putExtra("dishId", dish.getId());
                context.startActivity(intent);
                isAddToCartClicked[0] = true;
            });
        }

        holder.itemView.setOnClickListener(v -> {
            if (!isAddToCartClicked[0]) {
                if (onDishClickListener != null) {
                    onDishClickListener.onDishClick(dish);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, tvQuantity;
        ImageView dishAvatar;
        LinearLayout quantityContainer;
        ImageButton btnIncrease, btnDecrease, btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            dishAvatar = itemView.findViewById(R.id.dishAvatar);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            quantityContainer = itemView.findViewById(R.id.quantityContainer);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }
}
