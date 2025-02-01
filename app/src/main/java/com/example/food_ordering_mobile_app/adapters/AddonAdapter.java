package com.example.food_ordering_mobile_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;

import java.util.List;

public class AddonAdapter extends RecyclerView.Adapter<AddonAdapter.AddonViewHolder> {
    private List<String> addonList;

    public AddonAdapter(List<String> addonList) {
        this.addonList = addonList;
    }

    @NonNull
    @Override
    public AddonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addon, parent, false);
        return new AddonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddonViewHolder holder, int position) {
        holder.txtAddon.setText(addonList.get(position));
    }

    @Override
    public int getItemCount() {
        return addonList.size();
    }

    public static class AddonViewHolder extends RecyclerView.ViewHolder {
        TextView txtAddon;

        public AddonViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAddon = itemView.findViewById(R.id.txtAddon);
        }
    }
}
