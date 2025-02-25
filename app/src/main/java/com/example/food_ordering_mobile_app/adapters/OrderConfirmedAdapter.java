package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.StoreOrder;

import java.util.List;

public class OrderConfirmedAdapter extends RecyclerView.Adapter<OrderConfirmedAdapter.ViewHolder> {

    private Context context;
    private List<StoreOrder> ordersList;
    private OrderConfirmedAdapter.OnOrderClickListener onOrderClickListener;


    // Interface for handling item clicks
    public interface OnOrderClickListener {
        void onOrderClick(StoreOrder order);
    }

    // Constructor without click listener
    public OrderConfirmedAdapter(Context context, List<StoreOrder> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    // Constructor with click listener
    public OrderConfirmedAdapter(Context context, List<StoreOrder> ordersList, OrderConfirmedAdapter.OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.ordersList = ordersList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public OrderConfirmedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ensure you are inflating the correct layout for orders
        View view = LayoutInflater.from(context).inflate(R.layout.item_confirmed_order, parent, false);
        return new OrderConfirmedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderConfirmedAdapter.ViewHolder holder, int position) {
        StoreOrder order = ordersList.get(position);



        holder.orderId.setText(!TextUtils.isEmpty(String.valueOf(order.getId())) ? "#" + order.getId() : "#0000");
        holder.pickupTime.setText(!TextUtils.isEmpty(order.getPickUpTime()) ? order.getPickUpTime() : "Not Available");
        holder.customerName.setText(!TextUtils.isEmpty(order.getCustomerName()) ? order.getCustomerName() : "Unknown");
        holder.status.setText(!TextUtils.isEmpty(order.getStatus()) ? order.getStatus() : "Pending");



        // Handle click event
        holder.itemView.setOnClickListener(v -> {
            if (onOrderClickListener != null) {
                onOrderClickListener.onOrderClick(order);
            }
        });

        // Button actions (if applicable)
//        holder.btnDetail.setOnClickListener(v -> {
//            if (onOrderClickListener != null) {
//                onOrderClickListener.onOrderClick(order);
//            }
//        });

        holder.btnInformDriver.setOnClickListener(v -> {
            // Implement order confirmation logic here (if needed)
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size(); // Return the correct size of the list
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber, orderId, pickupTime, customerName,status, itemsNumber;
        Button btnInformDriver;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumber = itemView.findViewById(R.id.order_number);
            customerName = itemView.findViewById(R.id.customer_name);
            pickupTime = itemView.findViewById(R.id.pickup_time);
            btnInformDriver = itemView.findViewById(R.id.btn_confirm);
            orderId =  itemView.findViewById(R.id.order_id);
            status = itemView.findViewById(R.id.status);
            itemsNumber = itemView.findViewById(R.id.items_number);

        }
    }
}
