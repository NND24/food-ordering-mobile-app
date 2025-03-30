package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.dish.Dish;

import java.util.List;

public class DishBigAdapter extends RecyclerView.Adapter<DishBigAdapter.ViewHolder> {
    private Context context;
    private List<Dish> dishList;
    private OnDishClickListener onDishClickListener;
    public interface OnDishClickListener {
        void onDishClick(Dish dish);
    }

    public DishBigAdapter(Context context, List<Dish> dishList) {
        this.context = context;
        this.dishList = dishList;
    }

    public DishBigAdapter(Context context, List<Dish> dishList, OnDishClickListener onDishClickListener) {
        this.context = context;
        this.dishList = dishList;
        this.onDishClickListener = onDishClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dish_big, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dish dish = dishList.get(position);

        holder.name.setText(dish.getName());
        holder.price.setText(String.valueOf(dish.getPrice()));

        int resourceId = Integer.parseInt(dish.getImage().getUrl());
        Glide.with(context)
                .load(resourceId)
                .transform(new RoundedCorners(8))
                .into(holder.dishAvatar);

        // Handle button clicks for increase, decrease, and visibility
        holder.btnAddToCart.setOnClickListener(v -> {
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.quantityContainer.setVisibility(View.VISIBLE);
            holder.tvQuantity.setText("1");
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            quantity++;
            holder.tvQuantity.setText(String.valueOf(quantity));
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                holder.tvQuantity.setText(String.valueOf(quantity));
            } else {
                // If quantity reaches 0, hide quantity container and show the button again
                holder.quantityContainer.setVisibility(View.GONE);
                holder.btnAddToCart.setVisibility(View.VISIBLE);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (onDishClickListener != null) {
                onDishClickListener.onDishClick(dish);
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
