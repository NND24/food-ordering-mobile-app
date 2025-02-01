package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.SideDish;

import java.util.List;

public class SideDishAdapter extends RecyclerView.Adapter<SideDishAdapter.ViewHolder> {
    private Context context;
    private List<SideDish> sideDishList;
    private OnSideDishClickListener onSideDishClickListener;
    public interface OnSideDishClickListener {
        void onSideDishClick(SideDish dish);
    }

    public SideDishAdapter(Context context, List<SideDish> dishList) {
        this.context = context;
        this.sideDishList = dishList;
    }

    public SideDishAdapter(Context context, List<SideDish> sideDishList, OnSideDishClickListener onSideDishClickListener) {
        this.context = context;
        this.sideDishList = sideDishList;
        this.onSideDishClickListener = onSideDishClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_side_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SideDish sideDish = sideDishList.get(position);

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
        return sideDishList.size();
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
