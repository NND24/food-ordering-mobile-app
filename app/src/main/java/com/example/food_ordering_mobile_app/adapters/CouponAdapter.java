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
import com.example.food_ordering_mobile_app.models.Coupon;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {
    private Context context;
    private List<Coupon> restaurantList;
    private OnCouponClickListener onCouponClickListener;
    public interface OnCouponClickListener {
        void onCouponClick(Coupon coupon);
    }

    public CouponAdapter(Context context, List<Coupon> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    public CouponAdapter(Context context, List<Coupon> restaurantList, OnCouponClickListener onCouponClickListener) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.onCouponClickListener = onCouponClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_coupon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coupon coupon = restaurantList.get(position);

        holder.name.setText(coupon.getName());
        holder.description.setText(coupon.getDescription());

        int resourceId = Integer.parseInt(coupon.getCouponImage());
        Glide.with(context)
                .load(resourceId)
                .transform(new RoundedCorners(8))
                .into(holder.couponImage);

        holder.itemView.setOnClickListener(v -> {
            if (onCouponClickListener != null) {
                onCouponClickListener.onCouponClick(coupon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        ImageView couponImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            couponImage = itemView.findViewById(R.id.couponImage);
        }
    }
}

