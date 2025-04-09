package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.location.Location;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.ui.customer.account.location.EditLocationActivity;
import com.example.food_ordering_mobile_app.ui.customer.orders.OrderDetailActivity;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;
import com.example.food_ordering_mobile_app.viewmodels.LocationViewModel;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private Context context;
    private List<Location> locationList;
    private FragmentActivity activity;
    private OnLocationClickListener onLocationClickListener;
    public interface OnLocationClickListener {
        void onLocationClick(Location location);
    }

    public LocationAdapter(FragmentActivity activity, Context context, List<Location> locationList, OnLocationClickListener onLocationClickListener) {
        this.activity = activity;
        this.context = context;
        this.locationList = locationList;
        this.onLocationClickListener = onLocationClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = locationList.get(position);

        holder.tvLocationName.setText(location.getName());
        holder.tvLocationAddress.setText(location.getAddress());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditLocationActivity.class);
            intent.putExtra("locationId", location.getId());
            intent.putExtra("type", location.getType());
            context.startActivity(intent);
        });

        LocationViewModel locationViewModel = new ViewModelProvider(activity).get(LocationViewModel.class);

        holder.btnRemove.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa địa chỉ này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        locationViewModel.deleteLocation(location.getId());
                    })
                    .setNegativeButton("Không", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });

        holder.itemView.setOnClickListener(v -> {
            if (onLocationClickListener != null) {
                onLocationClickListener.onLocationClick(location);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocationName, tvLocationAddress;
        ImageView btnEdit, btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocationName = itemView.findViewById(R.id.tvLocationName);
            tvLocationAddress = itemView.findViewById(R.id.tvLocationAddress);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}

