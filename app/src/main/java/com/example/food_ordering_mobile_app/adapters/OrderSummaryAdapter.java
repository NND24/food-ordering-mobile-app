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
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.order.OrderTopping;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {
    private Context context;

    private List<OrderItem> orderList;

    public OrderSummaryAdapter(Context context, List<OrderItem> orderList) {
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
        OrderItem item = orderList.get(position);

        holder.tvDishName.setText(item.getDish().getName());
        holder.tvDishQuantity.setText(String.valueOf(item.getQuantity()));

        // Lấy giá món chính
        double dishPrice = item.getDish().getPrice();

        // Tính tổng giá topping
        double totalToppingPrice = 0;
        for (OrderTopping topping : item.getToppings()) {
            totalToppingPrice += topping.getPrice(); // Giả sử Topping có phương thức getPrice()
        }

        // Tính tổng giá
        double totalPrice = (dishPrice + totalToppingPrice) * item.getQuantity();

        // Hiển thị tổng giá
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvDishPrice.setText(String.valueOf(formatter.format(totalPrice)));

        // Xử lý hiển thị topping
        if (item.getToppings().isEmpty()) {
            holder.recyclerViewAddons.setVisibility(View.GONE);
        } else {
            holder.recyclerViewAddons.setVisibility(View.VISIBLE);
            List<String> toppingNames = new ArrayList<>();
            for (OrderTopping topping : item.getToppings()) {
                toppingNames.add(topping.getName());
            }
            AddonAdapter addonAdapter = new AddonAdapter(toppingNames);
            holder.recyclerViewAddons.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.recyclerViewAddons.setAdapter(addonAdapter);
        }
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDishName, tvDishQuantity, tvDishPrice;
        RecyclerView recyclerViewAddons;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDishName = itemView.findViewById(R.id.tvDishName);
            tvDishQuantity = itemView.findViewById(R.id.tvDishQuantity);
            tvDishPrice = itemView.findViewById(R.id.tvDishPrice);
            recyclerViewAddons = itemView.findViewById(R.id.recyclerViewAddons);
        }
    }
}

