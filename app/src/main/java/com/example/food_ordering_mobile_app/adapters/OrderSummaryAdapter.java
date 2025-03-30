package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.dish.Topping;
import com.example.food_ordering_mobile_app.models.order.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {
    private Context context;

    private List<Order> orderList;

    public OrderSummaryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.name.setText(order.getStore().getName());
      //  holder.quantity.setText(String.valueOf(order.getItems().getQuantity()));
      //  holder.price.setText(String.valueOf(order.getItems().getDish().getPrice()));

//        if (order.getItems().getToppings().isEmpty()) {
//            holder.recyclerViewAddons.setVisibility(View.GONE);
//        } else {
//            holder.recyclerViewAddons.setVisibility(View.VISIBLE);
//            List<String> toppingNames = new ArrayList<>();
//            for (Topping topping : order.getItems().getToppings()) {
//                toppingNames.add(topping.getName()); // Giả sử Topping có phương thức getName()
//            }
//            AddonAdapter addonAdapter = new AddonAdapter(toppingNames);
//            holder.recyclerViewAddons.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
//            holder.recyclerViewAddons.setAdapter(addonAdapter);
//        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, price;
        RecyclerView recyclerViewAddons;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            recyclerViewAddons = itemView.findViewById(R.id.recyclerViewAddons);
        }
    }
}

