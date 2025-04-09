package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.ui.customer.store.StoreActivity;
import com.example.food_ordering_mobile_app.viewmodels.FavoriteViewModel;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context context;
    private List<Store> favoriteList;
    private FragmentActivity fragment;

    public FavoriteAdapter(FragmentActivity fragment, Context context, List<Store> favoriteList) {
        this.fragment = fragment;
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Store favorite = favoriteList.get(position);

        holder.tvStoreName.setText(favorite.getName());
        if (favorite.getAmountRating() > 0) {
            holder.tvAvgRating.setText(String.format("%.2f", favorite.getAvgRating()));
            holder.tvAmountRating.setText(String.valueOf(favorite.getAmountRating()));
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

        if (favorite.getStoreCategory() != null && !favorite.getStoreCategory().isEmpty()) {
            SpannableStringBuilder categories = new SpannableStringBuilder();
            for (int i = 0; i < favorite.getStoreCategory().size(); i++) {
                FoodType type = favorite.getStoreCategory().get(i);
                categories.append(type.getName());

                if (i < favorite.getStoreCategory().size() - 1) {
                    categories.append(" • ");
                }
            }
            holder.tvStoreFoodType.setText(categories);
        } else {
            holder.tvStoreFoodType.setText("Unknown");
        }

        String storeAvatarUrl = favorite.getAvatar() != null ? favorite.getAvatar().getUrl() : null;
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

        FavoriteViewModel favoriteViewModel = new ViewModelProvider(fragment).get(FavoriteViewModel.class);

        holder.btnRemoveFromFavorite.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa cửa hàng này khỏi mục yêu thích?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        favoriteViewModel.removeFavorite(favorite.getId());
                    })
                    .setNegativeButton("Không", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoreActivity.class);
            intent.putExtra("storeId", favorite.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvStoreFoodType, tvAvgRating, tvAmountRating, tvRatingOpen, tvRatingText, tvRatingClose;
        ImageView imStoreAvatar, ivStar;
        ImageButton btnRemoveFromFavorite;

        public FavoriteViewHolder(@NonNull View itemView) {
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
            btnRemoveFromFavorite = itemView.findViewById(R.id.btnRemoveFromFavorite);
        }
    }
}

