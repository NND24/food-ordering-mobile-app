package com.example.food_ordering_mobile_app.adapters;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.location.Province;

import java.util.List;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> {

    private List<Province> provinces;
    private OnProvinceClickListener onProvinceClickListener;
    private String selectedProvince;

    public interface OnProvinceClickListener {
        void onProvinceClick(String province);
    }

    public ProvinceAdapter(List<Province> provinces, OnProvinceClickListener listener, String selectedProvince) {
        this.provinces = provinces;
        this.onProvinceClickListener = listener;
        this.selectedProvince = selectedProvince;
    }

    @Override
    public ProvinceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province, parent, false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProvinceViewHolder holder, int position) {
        Province province = provinces.get(position);
        holder.tvProvinceName.setText(province.getName());

        if (province.getName().equals(selectedProvince)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY); // Màu xám
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE); // Màu nền mặc định
        }

        holder.itemView.setOnClickListener(v -> {
            onProvinceClickListener.onProvinceClick(province.getName());
        });
    }

    @Override
    public int getItemCount() {
        return provinces.size();
    }

    public static class ProvinceViewHolder extends RecyclerView.ViewHolder {
        TextView tvProvinceName;

        public ProvinceViewHolder(View itemView) {
            super(itemView);
            tvProvinceName = itemView.findViewById(R.id.tvProvinceName);
        }
    }
}
