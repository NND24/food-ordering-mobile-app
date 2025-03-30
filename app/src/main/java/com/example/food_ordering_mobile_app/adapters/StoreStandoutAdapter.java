package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.ui.customer.store.StoreActivity;

import java.util.List;

public class StoreStandoutAdapter extends RecyclerView.Adapter<StoreStandoutAdapter.ViewHolder> {
    private Context context;
    private List<Store> stores;
    private OnStoreClickListener onStoreClickListener;
    public interface OnStoreClickListener {
        void onStoreClick(Store store);
    }

    public StoreStandoutAdapter(Context context, List<Store> stores) {
        this.context = context;
        this.stores = stores;
    }

    public StoreStandoutAdapter(Context context, List<Store> stores, OnStoreClickListener onStoreClickListener) {
        this.context = context;
        this.stores = stores;
        this.onStoreClickListener = onStoreClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_store_standout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Store store = stores.get(position);

        holder.tvStoreName.setText(store.getName());
        if (store.getAmountRating() > 0) {
            holder.tvAvgRating.setText(String.format("%.2f", store.getAvgRating()));
            holder.tvAmountRating.setText(String.valueOf(store.getAmountRating()));
            holder.tvAvgRating.setVisibility(View.VISIBLE);
            holder.tvAmountRating.setVisibility(View.VISIBLE);
            holder.ivStar.setVisibility(View.VISIBLE);
            holder.tvRatingOpen.setVisibility(View.VISIBLE);
            holder.tvRatingText.setVisibility(View.VISIBLE);
            holder.tvRatingClose.setVisibility(View.VISIBLE);
            holder.tvDot.setVisibility(View.VISIBLE);
        } else {
            holder.tvAvgRating.setVisibility(View.GONE);
            holder.tvAmountRating.setVisibility(View.GONE);
            holder.ivStar.setVisibility(View.GONE);
            holder.tvRatingOpen.setVisibility(View.GONE);
            holder.tvRatingText.setVisibility(View.GONE);
            holder.tvRatingClose.setVisibility(View.GONE);
            holder.tvDot.setVisibility(View.GONE);
        }

        if (store.getStoreCategory() != null && !store.getStoreCategory().isEmpty()) {
            StringBuilder categories = new StringBuilder();
            for (FoodType type : store.getStoreCategory()) {
                categories.append(type.getName()).append(", ");  // Nối tên các loại nhà hàng
            }
            // Xóa dấu phẩy cuối cùng
            String categoryText = categories.toString().replaceAll(", $", "");
            holder.tvStoreFoodType.setText(categoryText);
        } else {
            holder.tvStoreFoodType.setText("Unknown");
        }

        String storeAvatarUrl = store.getAvatar() != null ? store.getAvatar().getUrl() : null;
        Glide.with(context)
                .asBitmap()
                .load(storeAvatarUrl)
                .override(410, 210)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.imStoreAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedDrawable.setCornerRadius(0);
                        holder.imStoreAvatar.setImageDrawable(roundedDrawable);
                    }
                });

        holder.itemView.setOnClickListener(v -> {
            if (onStoreClickListener != null) {
                onStoreClickListener.onStoreClick(store);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvStoreFoodType, tvAvgRating, tvAmountRating, tvRatingOpen, tvRatingText, tvRatingClose, tvDot;
        ImageView imStoreAvatar, ivStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvStoreFoodType = itemView.findViewById(R.id.tvStoreFoodType);
            tvAvgRating = itemView.findViewById(R.id.tvAvgRating);
            tvAmountRating = itemView.findViewById(R.id.tvAmountRating);
            tvStoreFoodType = itemView.findViewById(R.id.tvStoreFoodType);
            imStoreAvatar = itemView.findViewById(R.id.imStoreAvatar);
            ivStar = itemView.findViewById(R.id.ivStar);
            tvRatingOpen = itemView.findViewById(R.id.tvRatingOpen);
            tvRatingText = itemView.findViewById(R.id.tvRatingText);
            tvRatingClose = itemView.findViewById(R.id.tvRatingClose);
            tvDot = itemView.findViewById(R.id.tvDot);
        }
    }
}

