package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.cart.CartItem;
import com.example.food_ordering_mobile_app.models.dish.Topping;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupToppingAdapter extends RecyclerView.Adapter<GroupToppingAdapter.CategoryViewHolder> {

    private Context context;
    private List<ToppingGroup> groupToppingList;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<ToppingAdapter> toppingAdapters = new ArrayList<>();
    private OnToppingSelectedListener  onSelectedToppingsChanged;
    private CartItem matchedItem;
    public interface OnToppingSelectedListener  {
        void onSelectedToppingsChanged(List<Topping> selectedToppings);
    }

    public GroupToppingAdapter(Context context, List<ToppingGroup> groupToppingList, CartItem matchedItem, OnToppingSelectedListener onSelectedToppingsChanged) {
        this.context = context;
        this.groupToppingList = groupToppingList;
        this.matchedItem = matchedItem;
        this.onSelectedToppingsChanged = onSelectedToppingsChanged;
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

        ToppingAdapter adapter = new ToppingAdapter(context, groupTopping.getToppings(), matchedItem, (topping, isChecked) -> {
            if (onSelectedToppingsChanged != null) {
                onSelectedToppingsChanged.onSelectedToppingsChanged(getSelectedTopping());
            }
        });
        holder.toppingAdapter = adapter;

        holder.toppingRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.toppingRecyclerView.setAdapter(adapter);
        holder.toppingRecyclerView.setRecycledViewPool(viewPool);

        // Đảm bảo không thêm trùng adapter
        if (!toppingAdapters.contains(adapter)) {
            toppingAdapters.add(adapter);
        }
    }

    public List<Topping> getSelectedTopping() {
        List<Topping> allSelected = new ArrayList<>();
        for (ToppingAdapter adapter : toppingAdapters) {
            allSelected.addAll(adapter.getSelectedTopping());
        }
        return allSelected;
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
