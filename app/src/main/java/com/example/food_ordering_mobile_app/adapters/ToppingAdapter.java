package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.cart.CartItem;
import com.example.food_ordering_mobile_app.models.dish.Topping;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ViewHolder> {
    private Context context;
    private List<Topping> toppingList;
    private OnToppingSelectedListener onToppingSelectedListener;
    private Set<Topping> selectedTopping = new HashSet<>();
    private CartItem matchedItem;


    public interface OnToppingSelectedListener {
        void onToppingSelected(Topping topping, boolean isChecked);
    }

    public ToppingAdapter(Context context, List<Topping> toppingList) {
        this.context = context;
        this.toppingList = toppingList;
    }

    public ToppingAdapter(Context context, List<Topping> toppingList, CartItem matchedItem, OnToppingSelectedListener onToppingSelectedListener) {
        this.context = context;
        this.toppingList = toppingList;
        this.matchedItem = matchedItem;
        this.onToppingSelectedListener = onToppingSelectedListener;
    }

    public List<Topping> getSelectedTopping() {
        return new ArrayList<>(selectedTopping);
    }

    public void updateData(List<Topping> newToppings) {
        if (newToppings != null) {
            toppingList.clear();
            toppingList.addAll(newToppings);
            selectedTopping.clear();
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
        Topping topping = toppingList.get(position);

        holder.name.setText(topping.getName());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.price.setText(String.valueOf(formatter.format(topping.getPrice())));

        if (matchedItem != null && matchedItem.getToppings() != null) {
            for (Topping selected : matchedItem.getToppings()) {
                if (selected.getId().equals(topping.getId())) {
                    selectedTopping.add(topping);
                    break;
                }
            }
        }

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedTopping.contains(topping));

        holder.container.setOnClickListener(v -> {
            boolean isChecked = !holder.checkBox.isChecked();
            holder.checkBox.setChecked(isChecked);
        });

        // Set listener mới
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedTopping.add(topping);
                if (onToppingSelectedListener != null) {
                    onToppingSelectedListener.onToppingSelected(topping, true);
                }
            } else {
                selectedTopping.remove(topping);
                if (onToppingSelectedListener != null) {
                    onToppingSelectedListener.onToppingSelected(topping, false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return toppingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        CheckBox checkBox;
        LinearLayout container;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.side_dish_name);
            price = itemView.findViewById(R.id.side_dish_price);
            checkBox = itemView.findViewById(R.id.checkBox);
            container = itemView.findViewById(R.id.topping_item_container);
        }
    }
}
