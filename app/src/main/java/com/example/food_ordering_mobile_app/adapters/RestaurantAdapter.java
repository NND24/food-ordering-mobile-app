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
import com.example.food_ordering_mobile_app.models.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private Context context;
    private List<Restaurant> restaurantList;
    private OnRestaurantClickListener onRestaurantClickListener;
    public interface OnRestaurantClickListener {
        void onRestaurantClick(Restaurant restaurant);
    }

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList, OnRestaurantClickListener onRestaurantClickListener) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.onRestaurantClickListener = onRestaurantClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        holder.name.setText(restaurant.getName());
        holder.type.setText(restaurant.getType());
        holder.rating.setText(String.valueOf(restaurant.getRating()));
        holder.amountOfRating.setText(String.valueOf(restaurant.getAmountOfRating()));
        holder.description.setText(restaurant.getDescription());

        int resourceId = Integer.parseInt(restaurant.getRestaurantAvatar());
        Glide.with(context)
                .load(resourceId)
                .transform(new RoundedCorners(8))
                .into(holder.restaurantAvatar);

        holder.itemView.setOnClickListener(v -> {
            if (onRestaurantClickListener != null) {
                onRestaurantClickListener.onRestaurantClick(restaurant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, rating, amountOfRating, description;
        ImageView restaurantAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurantName);
            type = itemView.findViewById(R.id.restaurantType);
            rating = itemView.findViewById(R.id.rating);
            amountOfRating = itemView.findViewById(R.id.amountOfRating);
            description = itemView.findViewById(R.id.restaurantDescription);
            restaurantAvatar = itemView.findViewById(R.id.restaurantAvatar);
        }
    }
}

