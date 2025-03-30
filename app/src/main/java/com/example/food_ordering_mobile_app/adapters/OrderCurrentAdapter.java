package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.OrderItem;

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
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvQuantity, tvAddress, tvDescription;
        ImageView imStoreAvatar;
        Button btnTrackOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            imStoreAvatar = itemView.findViewById(R.id.imStoreAvatar);
            btnTrackOrder = itemView.findViewById(R.id.btnTrackOrder);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}

