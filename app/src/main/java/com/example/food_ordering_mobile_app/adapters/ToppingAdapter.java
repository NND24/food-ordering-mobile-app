package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.dish.Topping;

import java.util.List;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ViewHolder> {
    private Context context;
    private List<Topping> toppingList;
    private OnSideDishClickListener onSideDishClickListener;
    public interface OnSideDishClickListener {
        void onSideDishClick(Topping dish);
    }

    public ToppingAdapter(Context context, List<Topping> toppingList) {
        this.context = context;
        this.toppingList = toppingList;
    }

    public ToppingAdapter(Context context, List<Topping> toppingList, OnSideDishClickListener onSideDishClickListener) {
        this.context = context;
        this.toppingList = toppingList;
        this.onSideDishClickListener = onSideDishClickListener;
    }

    public void updateData(List<Topping> newToppings) {
        if (newToppings != null) {
            toppingList.clear();
            toppingList.addAll(newToppings);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topping, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topping sideDish = toppingList.get(position);

        holder.name.setText(sideDish.getName());
        holder.price.setText(String.valueOf(sideDish.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            if (onSideDishClickListener != null) {
                onSideDishClickListener.onSideDishClick(sideDish);
            }
        });
    }

    @Override
    public int getItemCount() {
        return toppingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.side_dish_name);
            price = itemView.findViewById(R.id.side_dish_price);
        }
    }
}
