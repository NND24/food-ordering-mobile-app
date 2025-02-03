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
import com.bumptech.glide.request.RequestOptions;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<Cart> cartList;
    private OnCartClickListener onCartClickListener;
    public interface OnCartClickListener {
        void onCartClick(Cart cart);
    }

    public CartAdapter(Context context, List<Cart> cartList) {
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

        holder.name.setText(cart.getName());
        holder.type.setText(cart.getType());
        holder.rating.setText(String.valueOf(cart.getRating()));
        holder.amountOfRating.setText(String.valueOf(cart.getAmountOfRating()));
        holder.description.setText(cart.getDescription());

        int resourceId = Integer.parseInt(cart.getRestaurantAvatar());
        Glide.with(context)
                .load(resourceId)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(8)))
                .into(holder.restaurantAvatar);

        holder.itemView.setOnClickListener(v -> {
            if (onCartClickListener != null) {
                onCartClickListener.onCartClick(cart);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, rating, amountOfRating, description, quantity;
        ImageView restaurantAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurantName);
            type = itemView.findViewById(R.id.restaurantType);
            rating = itemView.findViewById(R.id.rating);
            amountOfRating = itemView.findViewById(R.id.amountOfRating);
            description = itemView.findViewById(R.id.restaurantDescription);
            restaurantAvatar = itemView.findViewById(R.id.restaurantAvatar);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}

