package com.example.food_ordering_mobile_app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.ui.customer.orders.OrderDetailActivity;
import com.example.food_ordering_mobile_app.ui.customer.store.StoreActivity;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;
import com.example.food_ordering_mobile_app.viewmodels.OrderViewModel;

import java.util.List;

public class OrderCurrentAdapter extends RecyclerView.Adapter<OrderCurrentAdapter.ViewHolder> {
    private Context context;
    private List<Order> orderList;
    private FragmentActivity fragment;

    public OrderCurrentAdapter(FragmentActivity fragment, Context context, List<Order> orderList) {
        this.fragment = fragment;
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

        holder.tvStoreName.setText(order.getStore().getName());
        holder.tvDescription.setText(order.getStore().getDescription());

        int totalQuantity = 0;
        for (OrderItem item : order.getItems()) {
            totalQuantity += item.getQuantity();
        }

        holder.tvQuantity.setText(String.valueOf(totalQuantity));
        holder.tvAddress.setText(order.getShipLocation().getAddress());

        String storeAvatarUrl = order.getStore().getAvatar() != null ? order.getStore().getAvatar().getUrl() : null;
        Glide.with(context)
                .asBitmap()
                .load(storeAvatarUrl)
                .override(80, 80)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.imStoreAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedDrawable.setCornerRadius(6);
                        holder.imStoreAvatar.setImageDrawable(roundedDrawable);
                    }
                });

        holder.btnTrackOrder.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderId", order.getId());
            context.startActivity(intent);
        });

        OrderViewModel orderViewModel = new ViewModelProvider(fragment).get(OrderViewModel.class);

        holder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không?")
                        .setPositiveButton("Hủy đơn", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                orderViewModel.cancelOrder(order.getId());
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        holder.storeInfoContainer.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoreActivity.class);
            intent.putExtra("storeId", order.getStore().getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvQuantity, tvAddress, tvDescription;
        ImageView imStoreAvatar;
        Button btnTrackOrder, btnCancelOrder;
        LinearLayout storeInfoContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            imStoreAvatar = itemView.findViewById(R.id.imStoreAvatar);
            btnTrackOrder = itemView.findViewById(R.id.btnTrackOrder);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            storeInfoContainer = itemView.findViewById(R.id.storeInfoContainer);
        }
    }
}

