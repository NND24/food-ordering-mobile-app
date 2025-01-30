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
import com.example.food_ordering_mobile_app.models.Review;

import java.util.List;

public class ReviewShortAdapter extends RecyclerView.Adapter<ReviewShortAdapter.ViewHolder> {
    private Context context;
    private List<Review> reviewList;
    private OnReviewClickListener onReviewClickListener;
    public interface OnReviewClickListener {
        void onRestaurantClick(Review review);
    }

    public ReviewShortAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    public ReviewShortAdapter(Context context, List<Review> reviewList, OnReviewClickListener onReviewClickListener) {
        this.context = context;
        this.reviewList = reviewList;
        this.onReviewClickListener = onReviewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_review_short, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);

        holder.name.setText(review.getCustomerName());
        holder.rating.setText(String.valueOf(review.getRating()));
        holder.review.setText(review.getReview());

        holder.itemView.setOnClickListener(v -> {
            if (onReviewClickListener != null) {
                onReviewClickListener.onRestaurantClick(review);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, rating, review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.rating);
            review = itemView.findViewById(R.id.review);
        }
    }
}

