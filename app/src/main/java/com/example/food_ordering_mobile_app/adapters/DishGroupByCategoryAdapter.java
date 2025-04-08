package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.dish.DishGroupByCategory;
import com.example.food_ordering_mobile_app.ui.customer.dish.DishActivity;

import java.util.List;

public class DishGroupByCategoryAdapter extends RecyclerView.Adapter<DishGroupByCategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<DishGroupByCategory> categoryList;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Cart currentCart = null;
    private FragmentActivity activity;

    public DishGroupByCategoryAdapter(FragmentActivity activity, Context context, List<DishGroupByCategory> categoryList) {
        this.activity = activity;
        this.context = context;
        this.categoryList = categoryList;
    }

    public void setCurrentCart(Cart currentCart) {
        this.currentCart = currentCart;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dish_group_by_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        DishGroupByCategory category = categoryList.get(position);
        holder.categoryName.setText(category.getCategory().getName());

        if (holder.dishAdapter == null) {
            holder.dishAdapter = new DishAdapter(activity, context, category.getDishes(), dish -> {
                Intent intent = new Intent(context, DishActivity.class);
                intent.putExtra("dishId", dish.getId());
                context.startActivity(intent);
            });
            holder.dishAdapter.setDishCurrentCart(currentCart);
            holder.dishRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.dishRecyclerView.setAdapter(holder.dishAdapter);
            holder.dishRecyclerView.setRecycledViewPool(viewPool);
        } else {
            holder.dishAdapter.updateData(category.getDishes());
            holder.dishAdapter.setDishCurrentCart(currentCart);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView dishRecyclerView;
        DishAdapter dishAdapter;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            dishRecyclerView = itemView.findViewById(R.id.dishRecyclerView);
        }
    }
}
