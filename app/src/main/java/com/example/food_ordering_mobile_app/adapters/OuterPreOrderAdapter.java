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
import com.example.food_ordering_mobile_app.models.OrderDateGroup;

import java.util.List;

public class OuterPreOrderAdapter extends RecyclerView.Adapter<OuterPreOrderAdapter.ViewHolder> {

    private List<OrderDateGroup> orderDateGroupList;

    public OuterPreOrderAdapter(List<OrderDateGroup> orderDateGroupList) {
        this.orderDateGroupList = orderDateGroupList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDateGroup dateGroup = orderDateGroupList.get(position);
        holder.dateText.setText(dateGroup.getDate());

        InnerPreOrderAdapter childAdapter = new InnerPreOrderAdapter(dateGroup.getOrders());
        holder.innerRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.innerRecyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return orderDateGroupList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        RecyclerView innerRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date_text);
            innerRecyclerView = itemView.findViewById(R.id.inner_recycler_view);
        }
    }
}
