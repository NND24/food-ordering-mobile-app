package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context context;
    private List<Review> reviewList;
    private OnReviewClickListener onReviewClickListener;
    public interface OnReviewClickListener {
        void onRestaurantClick(Review review);
    }

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    public ReviewAdapter(Context context, List<Review> reviewList, OnReviewClickListener onReviewClickListener) {
        this.context = context;
        this.reviewList = reviewList;
        this.onReviewClickListener = onReviewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);

        holder.customerName.setText(review.getCustomerName());
        holder.numberOfCustomerReview.setText(String.valueOf(review.getNumberOfCustomerReview()));
        holder.review.setText(review.getReview());
        holder.answer.setText(review.getAnswer());
        holder.reviewTime.setText(String.valueOf(review.getReviewTime()));
        holder.answerTime.setText(String.valueOf(review.getAnswerTime()));
        holder.orderFood.setText(review.getOrderFood());


        int customerAvatarResourceId = Integer.parseInt(review.getCustomerAvatar());
        Glide.with(context)
                .load(customerAvatarResourceId)
                .circleCrop()
                .into(holder.customerAvatar);

        int reviewImageResourceId = Integer.parseInt(review.getReviewImage());
        Glide.with(context)
                .load(reviewImageResourceId)
                .transform(new RoundedCorners(8))
                .into(holder.reviewImage);

        // Hiển thị số sao theo rating
        holder.starContainer.removeAllViews(); // Xóa các sao cũ nếu có
        int rating = (int) Math.round(review.getRating()); // Làm tròn số sao

        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(context);
            if (i <= rating) {
                star.setImageResource(R.drawable.ic_star_active_16); // Sao sáng
            } else {
                star.setImageResource(R.drawable.ic_star_16); // Sao mờ
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(5, 0, 0, 0); // Khoảng cách giữa các sao
            star.setLayoutParams(params);
            holder.starContainer.addView(star); // Thêm vào LinearLayout
        }

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
        TextView customerName, rating, review, numberOfCustomerReview, answer, reviewTime, answerTime, orderFood;
        ImageView customerAvatar, reviewImage;
        LinearLayout starContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name);
            customerAvatar = itemView.findViewById(R.id.customer_avatar);
            numberOfCustomerReview = itemView.findViewById(R.id.number_of_customer_review);
            rating = itemView.findViewById(R.id.rating);
            review = itemView.findViewById(R.id.review);
            answer = itemView.findViewById(R.id.answer);
            reviewTime = itemView.findViewById(R.id.review_time);
            answerTime = itemView.findViewById(R.id.answer_time);
            orderFood = itemView.findViewById(R.id.order_food);
            reviewImage = itemView.findViewById(R.id.review_image);
            starContainer = itemView.findViewById(R.id.starContainer);
        }
    }
}

