package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private OnCategoryClickListener onCategoryClickListener;
    private int selectedPosition = -1;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        // Use getAdapterPosition to get the position dynamically
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition != RecyclerView.NO_POSITION) {
            if (currentPosition == selectedPosition) {
                holder.imageContainer.setBackgroundResource(R.drawable.category_border_selected);  // Border when selected
                holder.checkIcon.setVisibility(View.VISIBLE);  // Show check icon
            } else {
                holder.imageContainer.setBackgroundResource(R.drawable.category_border);  // Normal border
                holder.checkIcon.setVisibility(View.GONE);  // Hide check icon
            }

            // Load image directly from resource ID
            int resourceId = Integer.parseInt(category.getImageUrl()); // Convert string back to resource ID
            Glide.with(context)
                    .load(resourceId) // Load using resource ID
                    .circleCrop()
                    .into(holder.imageView);

            holder.textView.setText(category.getName());

            holder.itemView.setOnClickListener(v -> {
                selectedPosition = currentPosition;  // Set selected position
                notifyDataSetChanged();  // Refresh the list to show the updated selection
                if (onCategoryClickListener != null) {
                    onCategoryClickListener.onCategoryClick(category);  // Notify listener
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, checkIcon;
        TextView textView;
        FrameLayout imageContainer;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.categoryImage);
            textView = itemView.findViewById(R.id.categoryName);
            checkIcon = itemView.findViewById(R.id.checkIcon);
            imageContainer = itemView.findViewById(R.id.imgContainer);
        }
    }
}
