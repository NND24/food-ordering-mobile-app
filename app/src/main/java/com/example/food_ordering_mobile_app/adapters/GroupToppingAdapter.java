package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroup;

import java.util.List;

public class GroupToppingAdapter extends RecyclerView.Adapter<GroupToppingAdapter.CategoryViewHolder> {

    private Context context;
    private List<ToppingGroup> groupToppingList;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public GroupToppingAdapter(Context context, List<ToppingGroup> groupToppingList) {
        this.context = context;
        this.groupToppingList = groupToppingList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topping_group, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        ToppingGroup groupTopping = groupToppingList.get(position);
        holder.tvToppingGroupName.setText(groupTopping.getName());

        if (holder.toppingAdapter == null) {
            holder.toppingAdapter = new ToppingAdapter(context, groupTopping.getToppings());
            holder.toppingRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.toppingRecyclerView.setAdapter(holder.toppingAdapter);
            holder.toppingRecyclerView.setRecycledViewPool(viewPool);
        } else {
            holder.toppingAdapter.updateData(groupTopping.getToppings());
        }
    }

    @Override
    public int getItemCount() {
        return groupToppingList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvToppingGroupName;
        RecyclerView toppingRecyclerView;
        ToppingAdapter toppingAdapter;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvToppingGroupName = itemView.findViewById(R.id.tvToppingGroupName);
            toppingRecyclerView = itemView.findViewById(R.id.toppingRecyclerView);
        }
    }
}
