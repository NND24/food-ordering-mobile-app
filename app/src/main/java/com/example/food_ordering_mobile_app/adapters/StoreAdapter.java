package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.ui.customer.store.StoreActivity;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    private Context context;
    private List<Store> stores = new ArrayList<>();
    private OnStoreClickListener onStoreClickListener;

    public interface OnStoreClickListener {
        void onStoreClick(Store store);
    }

    public StoreAdapter(Context context, List<Store> stores, OnStoreClickListener onStoreClickListener) {
        this.context = context;
        this.stores = stores != null ? stores : new ArrayList<>();
        this.onStoreClickListener = onStoreClickListener;
    }

    public void updateData(List<Store> newStores) {
        if (newStores != null) {
            stores.clear();
            stores.addAll(newStores);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
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
        } else {
            holder.tvAvgRating.setVisibility(View.GONE);
            holder.tvAmountRating.setVisibility(View.GONE);
            holder.ivStar.setVisibility(View.GONE);
            holder.tvRatingOpen.setVisibility(View.GONE);
            holder.tvRatingText.setVisibility(View.GONE);
            holder.tvRatingClose.setVisibility(View.GONE);
        }

        if (store.getStoreCategory() != null && !store.getStoreCategory().isEmpty()) {
            SpannableStringBuilder categories = new SpannableStringBuilder();
            for (int i = 0; i < store.getStoreCategory().size(); i++) {
                FoodType type = store.getStoreCategory().get(i);
                categories.append(type.getName());

                if (i < store.getStoreCategory().size() - 1) {
                    categories.append(" • ");
                }
            }
            holder.tvStoreFoodType.setText(categories);
        } else {
            holder.tvStoreFoodType.setText("");
        }

        String storeAvatarUrl = store.getAvatar() != null ? store.getAvatar().getUrl() : null;
        Glide.with(context)
                .asBitmap()
                .load(storeAvatarUrl)
                .override(95, 95)
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

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvStoreFoodType, tvAvgRating, tvAmountRating, tvRatingOpen, tvRatingText, tvRatingClose;
        ImageView imStoreAvatar, ivStar;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvStoreFoodType = itemView.findViewById(R.id.tvStoreFoodType);
            tvAvgRating = itemView.findViewById(R.id.tvAvgRating);
            tvAmountRating = itemView.findViewById(R.id.tvAmountRating);
            imStoreAvatar = itemView.findViewById(R.id.imStoreAvatar);
            ivStar = itemView.findViewById(R.id.ivStar);
            tvRatingOpen = itemView.findViewById(R.id.tvRatingOpen);
            tvRatingText = itemView.findViewById(R.id.tvRatingText);
            tvRatingClose = itemView.findViewById(R.id.tvRatingClose);
        }
    }
}
