package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.store.CategoryWithStores;
import com.example.food_ordering_mobile_app.ui.customer.store.StoreActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<CategoryWithStores> categoryList;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();


    public CategoryAdapter(Context context, List<CategoryWithStores> categoryList) {
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
        CategoryWithStores category = categoryList.get(position);
        holder.categoryName.setText(category.getCategory().getName());

        if (holder.storeAdapter == null) {
            holder.storeAdapter = new StoreAdapter(context, category.getStores(), store -> {
                Intent intent = new Intent(context, StoreActivity.class);
                intent.putExtra("storeId", store.getId());
                context.startActivity(intent);
            });
            holder.restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.restaurantRecyclerView.setAdapter(holder.storeAdapter);
            holder.restaurantRecyclerView.setRecycledViewPool(viewPool);
        } else {
            holder.storeAdapter.updateData(category.getStores());
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView restaurantRecyclerView;
        StoreAdapter storeAdapter;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            restaurantRecyclerView = itemView.findViewById(R.id.restaurantRecyclerView);
        }
    }
}
