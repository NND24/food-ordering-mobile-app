package com.example.food_ordering_mobile_app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishImage;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.rating.DishRating;
import com.example.food_ordering_mobile_app.models.rating.Rating;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.ui.customer.rating.EditRatingActivity;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewmodels.LocationViewModel;
import com.example.food_ordering_mobile_app.viewmodels.RatingViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private Context context;
    private List<Rating> ratingList;
    private OnReviewClickListener onReviewClickListener;
    private FragmentActivity activity;

    public interface OnReviewClickListener {
        void onRestaurantClick(Rating review);
    }

    public RatingAdapter(FragmentActivity activity, Context context, List<Rating> ratingList) {
        this.activity = activity;
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
        long timeDiff = System.currentTimeMillis() - rating.getUpdatedAt().getTime();
        holder.tvRatingTime.setText(formatTimeAgo(timeDiff));
        holder.tvListOrderedDish.setText(rating.getDishes().toString());

        User savedUser = SharedPreferencesHelper.getInstance(this.context).getCurrentUser();

        if(savedUser != null) {
            if (savedUser.getId().equals(rating.getUser().getId())) {
                holder.btnOption.setVisibility(View.VISIBLE);
            } else {
                holder.btnOption.setVisibility(View.GONE);
            }
        } else {
            holder.btnOption.setVisibility(View.GONE);
        }

        holder.btnOption.setOnClickListener(v -> {
            if (savedUser != null && savedUser.getId().equals(rating.getUser().getId())) {
                showOptionsDialog(rating);
            }
        });

        if (rating.getDishes() != null && !rating.getDishes().isEmpty()) {
            StringBuilder dishes = new StringBuilder();
            for (DishRating dish : rating.getDishes()) {
                dishes.append(dish.getName()).append(", ");
            }
            // Xóa dấu phẩy cuối cùng
            String dishText = dishes.toString().replaceAll(", $", "");
            holder.tvListOrderedDish.setText(dishText);
        } else {
            holder.tvListOrderedDish.setVisibility(View.GONE);
        }

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

        holder.imageContainer.removeAllViews(); // Xóa ảnh cũ nếu có

        if (rating.getImages() != null && !rating.getImages().isEmpty()) {
            int marginInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());

            int sizeInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 200, context.getResources().getDisplayMetrics());

            List<DishImage> images = rating.getImages();
            for (int i = 0; i < images.size(); i++) {
                DishImage image = images.get(i);
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizeInDp, sizeInDp);

                // Thêm margin phải 10dp trừ ảnh cuối cùng
                if (i != images.size() - 1) {
                    params.setMargins(0, 0, marginInDp, 0);
                }

                imageView.setLayoutParams(params);

                Glide.with(context)
                        .asBitmap()
                        .load(image.getUrl())
                        .override(50, 50)
                        .centerCrop()
                        .into(new BitmapImageViewTarget(imageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable roundedDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                roundedDrawable.setCornerRadius(6);
                                imageView.setImageDrawable(roundedDrawable);
                            }
                        });

                holder.imageContainer.addView(imageView);
            }
        } else {
            holder.imageContainer.setVisibility(View.GONE);
        }


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

    private void showOptionsDialog(Rating rating) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn hành động");

        builder.setItems(new CharSequence[] {"Chỉnh sửa", "Xóa"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Tùy chọn "Chỉnh sửa"
                    onEditRating(rating);
                    break;
                case 1:
                    // Tùy chọn "Xóa"
                    onDeleteRating(rating);
                    break;
            }
        });

        builder.create().show();
    }

    private void onEditRating(Rating rating) {
        Intent intent = new Intent(context, EditRatingActivity.class);
        intent.putExtra("", rating.getId());
        intent.putExtra("storeId", rating.getStore().getId());
        context.startActivity(intent);
    }

    private void onDeleteRating(Rating rating) {
        RatingViewModel ratingViewModel = new ViewModelProvider(activity).get(RatingViewModel.class);
        new AlertDialog.Builder(context)
                .setTitle("Xóa đánh giá")
                .setMessage("Bạn có chắc chắn muốn xóa đánh giá này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    ratingViewModel.getDetailRating(rating.getId());
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvComment, answer, tvRatingTime, answerTime, tvListOrderedDish;
        ImageView ivCustomerAvatar;
        LinearLayout starContainer;
        ImageButton btnOption;
        LinearLayout imageContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            ivCustomerAvatar = itemView.findViewById(R.id.ivCustomerAvatar);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvRatingTime = itemView.findViewById(R.id.tvRatingTime);
            tvListOrderedDish = itemView.findViewById(R.id.tvListOrderedDish);
            starContainer = itemView.findViewById(R.id.starContainer);
            btnOption = itemView.findViewById(R.id.btnOption);
            imageContainer = itemView.findViewById(R.id.imageContainer);

//            answer = itemView.findViewById(R.id.answer);
//            answerTime = itemView.findViewById(R.id.answer_time);
        }
    }
}

