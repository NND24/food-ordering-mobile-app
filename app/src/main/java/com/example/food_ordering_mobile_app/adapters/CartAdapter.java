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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.cart.CartItem;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.ui.customer.store.StoreActivity;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;
import com.example.food_ordering_mobile_app.viewmodels.FavoriteViewModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<Cart> cartList;
    private OnCartClickListener onCartClickListener;
    private FragmentActivity activity;

    public interface OnCartClickListener {
        void onCartClick(Cart cart);
    }

    public CartAdapter(FragmentActivity activity, Context context, List<Cart> cartList) {
        this.activity = activity;
        this.context = context;
        this.cartList = cartList;
    }

    public CartAdapter(Context context, List<Cart> restaurantList, OnCartClickListener onCartClickListener) {
        this.context = context;
        this.cartList = restaurantList;
        this.onCartClickListener = onCartClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        holder.tvStoreName.setText(cart.getStore().getName());
        int totalQuantity = 0;
        for (CartItem item : cart.getItems()) {
            totalQuantity += item.getQuantity();
        }

        holder.tvQuantity.setText(String.valueOf(totalQuantity));
        if (cart.getStore().getAmountRating() > 0) {
            holder.tvAvgRating.setText(String.format("%.2f", cart.getStore().getAvgRating()));
            holder.tvAmountRating.setText(String.valueOf(cart.getStore().getAmountRating()));
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

        if (cart.getStore().getStoreCategory() != null && !cart.getStore().getStoreCategory().isEmpty()) {
            SpannableStringBuilder categories = new SpannableStringBuilder();
            for (int i = 0; i < cart.getStore().getStoreCategory().size(); i++) {
                FoodType type = cart.getStore().getStoreCategory().get(i);
                categories.append(type.getName());

                if (i < cart.getStore().getStoreCategory().size() - 1) {
                    categories.append(" • ");
                }
            }
            holder.tvStoreFoodType.setText(categories);
        } else {
            holder.tvStoreFoodType.setText("Unknown");
        }

        String storeAvatarUrl = cart.getStore().getAvatar() != null ? cart.getStore().getAvatar().getUrl() : null;
        Glide.with(context)
                .asBitmap()
                .load(storeAvatarUrl)
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

        CartViewModel cartViewModel = new ViewModelProvider(activity).get(CartViewModel.class);

        holder.btnRemoveFromCart.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa cửa hàng này khỏi mục giỏ hàng?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        cartViewModel.clearCartItem(cart.getStore().getId());
                    })
                    .setNegativeButton("Không", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoreActivity.class);
            intent.putExtra("storeId", cart.getStore().getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvStoreFoodType, tvAvgRating, tvAmountRating, tvRatingOpen, tvRatingText, tvRatingClose, tvQuantity;
        ImageView imStoreAvatar, ivStar;
        ImageButton btnRemoveFromCart;

        public ViewHolder(@NonNull View itemView) {
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
            btnRemoveFromCart = itemView.findViewById(R.id.btnRemoveFromCart);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}

