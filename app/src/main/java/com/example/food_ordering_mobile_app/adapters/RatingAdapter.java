package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.dish.Rating;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private Context context;
    private List<Rating> ratingList;
    private OnReviewClickListener onReviewClickListener;
    public interface OnReviewClickListener {
        void onRestaurantClick(Rating review);
    }

    public RatingAdapter(Context context, List<Rating> ratingList) {
        this.context = context;
        this.ratingList = ratingList;
    }

    public RatingAdapter(Context context, List<Rating> ratingList, OnReviewClickListener onReviewClickListener) {
        this.context = context;
        this.ratingList = ratingList;
        this.onReviewClickListener = onReviewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_rating, parent, false);
        return new ViewHolder(view);
    }

    private String formatTimeAgo(long timeDiff) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
        long days = TimeUnit.MILLISECONDS.toDays(timeDiff);

        if (seconds < 60) return seconds + " giây trước";
        if (minutes < 60) return minutes + " phút trước";
        if (hours < 24) return hours + " giờ trước";
        return days + " ngày trước";
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rating rating = ratingList.get(position);

        holder.tvCustomerName.setText(rating.getUser().getName());
        holder.tvComment.setText(rating.getComment());
        long timeDiff = System.currentTimeMillis() - rating.getTimestamps().getTime();
        holder.tvRatingTime.setText(formatTimeAgo(timeDiff));
        holder.tvListOrderedDish.setText(rating.getDishes().toString());

        String customerAvatarUrl = rating.getUser().getAvatar() != null ? rating.getUser().getAvatar().getUrl() : null;
        Glide.with(context)
                .asBitmap()
                .load(customerAvatarUrl)
                .override(50, 50)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.ivCustomerAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedDrawable.setCornerRadius(6);
                        holder.ivCustomerAvatar.setImageDrawable(roundedDrawable);
                    }
                });

        String ratingImageUrl = rating.getImages() != null ? rating.getImages().get(0).getUrl() : null;
        Glide.with(context)
                .asBitmap()
                .load(ratingImageUrl)
                .override(50, 50)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.ivRatingImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedDrawable.setCornerRadius(6);
                        holder.ivRatingImage.setImageDrawable(roundedDrawable);
                    }
                });

        // Hiển thị số sao theo rating
        holder.starContainer.removeAllViews(); // Xóa các sao cũ nếu có
        int ratingValue = (int) Math.round(rating.getRatingValue()); // Làm tròn số sao

        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(context);
            if (i <= ratingValue) {
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

//        holder.answer.setText(review.getAnswer());
//        holder.answerTime.setText(String.valueOf(review.getAnswerTime()));

        holder.itemView.setOnClickListener(v -> {
            if (onReviewClickListener != null) {
                onReviewClickListener.onRestaurantClick(rating);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvComment, answer, tvRatingTime, answerTime, tvListOrderedDish;
        ImageView ivCustomerAvatar, ivRatingImage;
        LinearLayout starContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            ivCustomerAvatar = itemView.findViewById(R.id.ivCustomerAvatar);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvRatingTime = itemView.findViewById(R.id.tvRatingTime);
            tvListOrderedDish = itemView.findViewById(R.id.tvListOrderedDish);
            ivRatingImage = itemView.findViewById(R.id.ivRatingImage);
            starContainer = itemView.findViewById(R.id.starContainer);
//            answer = itemView.findViewById(R.id.answer);
//            answerTime = itemView.findViewById(R.id.answer_time);
        }
    }
}

