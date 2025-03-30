package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryFilterAdapter extends RecyclerView.Adapter<CategoryFilterAdapter.FoodTypeViewHolder> {
    private Context context;
    private List<FoodType> foodTypes;
    private OnFoodTypeClickListener onFoodTypeClickListener;

    private Set<String> selectedFoodTypes = new HashSet<>();

    public interface OnFoodTypeClickListener {
        void onFoodTypeClick(Set<String> selectedFoodTypeIds);
    }

    public CategoryFilterAdapter(Context context, List<FoodType> foodTypes, Set<String> selectedFoodTypes, OnFoodTypeClickListener onFoodTypeClickListener) {
        this.context = context;
        this.foodTypes = foodTypes;
        this.selectedFoodTypes = selectedFoodTypes;
        this.onFoodTypeClickListener = onFoodTypeClickListener;
    }

    @NonNull
    @Override
    public FoodTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_filter, parent, false);
        return new FoodTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodTypeViewHolder holder, int position) {
        FoodType foodType = foodTypes.get(position);
        String foodTypeId = foodType.getId();  // Lấy ID của foodType

        // Kiểm tra nếu item đang được chọn
        boolean isSelected = selectedFoodTypes.contains(foodTypeId);
        holder.cbCategory.setOnCheckedChangeListener(null);
        holder.cbCategory.setChecked(isSelected);
        holder.tvCategoryName.setText(foodType.getName());

        // Xử lý khi bấm vào `CheckBox`
        holder.cbCategory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedFoodTypes.add(foodTypeId);
            } else {
                selectedFoodTypes.remove(foodTypeId);
            }
            Log.d("DEBUG", "Selected FoodType IDs: " + selectedFoodTypes);
            if (onFoodTypeClickListener != null) {
                onFoodTypeClickListener.onFoodTypeClick(selectedFoodTypes);
            }
        });

        // Xử lý sự kiện khi bấm chọn
        holder.itemView.setOnClickListener(v -> holder.cbCategory.performClick());

    }

    @Override
    public int getItemCount() {
        return foodTypes.size();
    }

    public static class FoodTypeViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        CheckBox cbCategory;

        public FoodTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            cbCategory = itemView.findViewById(R.id.cbCategory);
        }
    }
}
