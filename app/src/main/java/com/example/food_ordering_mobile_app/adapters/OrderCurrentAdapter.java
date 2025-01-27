package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.Order;
import com.example.food_ordering_mobile_app.models.Restaurant;

import java.util.List;

public class OrderCurrentAdapter extends RecyclerView.Adapter<OrderCurrentAdapter.ViewHolder> {
    private Context context;

    private List<Order> orderList;

    public OrderCurrentAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_current, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.name.setText(order.getName());
        holder.amountOfDish.setText(String.valueOf(order.getAmountOfDish()));
        holder.address.setText(order.getAddress());

        int resourceId = Integer.parseInt(order.getRestaurantAvatar());
        Glide.with(context)
                .load(resourceId)
                .transform(new RoundedCorners(8))
                .into(holder.restaurantAvatar);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, amountOfDish, address;
        ImageView restaurantAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurantName);
            amountOfDish = itemView.findViewById(R.id.amountOfDish);
            address = itemView.findViewById(R.id.address);
            restaurantAvatar = itemView.findViewById(R.id.restaurantAvatar);
        }
    }
}

