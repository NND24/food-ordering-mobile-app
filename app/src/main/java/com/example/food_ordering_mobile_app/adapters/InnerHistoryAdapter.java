package com.example.food_ordering_mobile_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.StoreOrder;

import java.util.List;

public class InnerHistoryAdapter extends RecyclerView.Adapter<InnerHistoryAdapter.ViewHolder> {
    private List<StoreOrder> orderList;

    public InnerHistoryAdapter(List<StoreOrder> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public InnerHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHistoryAdapter.ViewHolder holder, int position) {
        StoreOrder order = orderList.get(position);

        holder.customerName.setText(order.getCustomerName() != null ? order.getCustomerName() : "Khách hàng không xác định");
        holder.orderNumber.setText(order.getNumberOrder() > 0 ? String.valueOf(order.getNumberOrder()) : "N/A");
        holder.pickupTime.setText(order.getOrderTime() != null ? order.getOrderTime() : "Không có thời gian");
        holder.totalItems.setText(order.getTotalItems() > 0 ? String.valueOf(order.getTotalItems()) : "0");
        holder.totalPrice.setText(order.getTotalPrice() > 0 ? String.valueOf(order.getTotalPrice()) + "₫" : "0₫");
        holder.distance.setText(order.getDistance() > 0 ? String.format("%.1f Km", order.getDistance()) : "Không xác định");
        holder.status.setText(order.getStatus() != null ? order.getStatus() : "Không xác định");
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, orderNumber, pickupTime, totalItems, totalPrice, distance, status;

        public ViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name);
            orderNumber = itemView.findViewById(R.id.order_number);
            pickupTime = itemView.findViewById(R.id.pickup_time);
            totalItems = itemView.findViewById(R.id.item_count);
            totalPrice = itemView.findViewById(R.id.total_price);
            distance = itemView.findViewById(R.id.distance_value);
            status = itemView.findViewById(R.id.history_status);
        }
    }
}
