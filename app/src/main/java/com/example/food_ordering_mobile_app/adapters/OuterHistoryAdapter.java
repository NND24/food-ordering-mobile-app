package com.example.food_ordering_mobile_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.OrderDateStatusGroup;

import java.util.List;

public class OuterHistoryAdapter extends RecyclerView.Adapter<OuterHistoryAdapter.ViewHolder>{
    private List<OrderDateStatusGroup> orderDateStatusGroups;

    public OuterHistoryAdapter(List<OrderDateStatusGroup> orderDateStatusGroups) {
        this.orderDateStatusGroups = orderDateStatusGroups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_status_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDateStatusGroup dateGroup = orderDateStatusGroups.get(position);
        holder.dateText.setText(dateGroup.getDate());
        holder.statusText.setText(dateGroup.getStatus());

        InnerHistoryAdapter childAdapter = new InnerHistoryAdapter(dateGroup.getOrders());
        holder.innerRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.innerRecyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return orderDateStatusGroups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, statusText;
        RecyclerView innerRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date_text);
            statusText = itemView.findViewById(R.id.status_text);
            innerRecyclerView = itemView.findViewById(R.id.inner_recycler_view);
        }
    }
}
