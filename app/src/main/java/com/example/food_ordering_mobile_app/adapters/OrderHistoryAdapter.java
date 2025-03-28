package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.Order;
import com.example.food_ordering_mobile_app.ui.customer.rating.RatingActivity;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private Context context;

    private List<Order> orderList;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.name.setText(order.getName());
        holder.quantity.setText(String.valueOf(order.getQuantity()));
        holder.address.setText(order.getAddress());

        int resourceId = Integer.parseInt(order.getRestaurantAvatar());
        Glide.with(context)
                .load(resourceId)
                .transform(new RoundedCorners(8))
                .into(holder.restaurantAvatar);

        holder.ratingBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, RatingActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, address;
        ImageView restaurantAvatar;
        Button reorderBtn, ratingBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurantName);
            quantity = itemView.findViewById(R.id.quantity);
            address = itemView.findViewById(R.id.address);
            restaurantAvatar = itemView.findViewById(R.id.restaurantAvatar);
            reorderBtn = itemView.findViewById(R.id.reorderBtn);
            ratingBtn = itemView.findViewById(R.id.ratingBtn);
        }
    }
}

