package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodTypeAdapter extends RecyclerView.Adapter<FoodTypeAdapter.FoodTypeViewHolder> {

    private Context context;
    private List<FoodType> foodTypes;
    private OnFoodTypeClickListener onFoodTypeClickListener;

    // Danh sách các foodType đã được chọn
    private Set<String> selectedFoodTypes = new HashSet<>();

    public interface OnFoodTypeClickListener {
        void onFoodTypeClick(Set<String> selectedFoodTypeIds);
    }

    public FoodTypeAdapter(Context context, List<FoodType> foodTypes, Set<String> selectedFoodTypes, OnFoodTypeClickListener onFoodTypeClickListener) {
        this.context = context;
        this.foodTypes = foodTypes;
        this.selectedFoodTypes = selectedFoodTypes;
        this.onFoodTypeClickListener = onFoodTypeClickListener;
    }

    @NonNull
    @Override
    public FoodTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_type, parent, false);
        return new FoodTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodTypeViewHolder holder, int position) {
        FoodType foodType = foodTypes.get(position);
        String foodTypeId = foodType.getId();  // Lấy ID của foodType

        // Kiểm tra nếu item đang được chọn
        boolean isSelected = selectedFoodTypes.contains(foodTypeId);
        holder.ivFoodTypeImage.setBackgroundResource(isSelected ?
                R.drawable.food_type_border_selected : R.drawable.food_type_border);

        holder.checkIcon.setVisibility(isSelected ? View.VISIBLE : View.GONE);

        String foodTypeImgUrl = foodType.getImage() != null ? foodType.getImage().getUrl() : null;
        Glide.with(context)
                .asBitmap()
                .load(foodTypeImgUrl)
                .override(75, 75)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.ivFoodTypeImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedDrawable.setCornerRadius(999);
                        holder.ivFoodTypeImage.setImageDrawable(roundedDrawable);
                    }
                });

        holder.tvFoodTypeName.setText(foodType.getName());

        // Xử lý sự kiện khi bấm chọn
        holder.itemView.setOnClickListener(v -> {
            if (selectedFoodTypes.contains(foodTypeId)) {
                selectedFoodTypes.remove(foodTypeId);  // Nếu đã chọn thì bỏ chọn
            } else {
                selectedFoodTypes.add(foodTypeId);  // Nếu chưa chọn thì thêm vào danh sách chọn
            }

            notifyItemChanged(holder.getAdapterPosition()); // Chỉ cập nhật item được click

            // Trả về danh sách ID các FoodType đã chọn
            if (onFoodTypeClickListener != null) {
                onFoodTypeClickListener.onFoodTypeClick(selectedFoodTypes);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodTypes.size();
    }

    public static class FoodTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodTypeImage, checkIcon;
        TextView tvFoodTypeName;
        FrameLayout imageContainer;

        public FoodTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodTypeImage = itemView.findViewById(R.id.ivFoodTypeImage);
            tvFoodTypeName = itemView.findViewById(R.id.tvFoodTypeName);
            checkIcon = itemView.findViewById(R.id.checkIcon);
            imageContainer = itemView.findViewById(R.id.imgContainer);
        }
    }
}
