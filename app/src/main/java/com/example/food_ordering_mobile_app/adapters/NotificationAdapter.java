package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private List<Notification> notificationList;
    private OnNotificationClickListener onNotificationClickListener;
    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    public NotificationAdapter(Context context, List<Notification> cartList) {
        this.context = context;
        this.notificationList = cartList;
    }

    public NotificationAdapter(Context context, List<Notification> notificationList, OnNotificationClickListener onNotificationClickListener) {
        this.context = context;
        this.notificationList = notificationList;
        this.onNotificationClickListener = onNotificationClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.noti.setText(notification.getNoti());
        holder.time.setText(notification.getTime());

        Boolean isRead = notification.getIsRead();

        if (isRead) {
            holder.notificationContainer.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
            holder.noti.setTextColor((context.getResources().getColor(R.color.onTertiary)));
            holder.time.setTextColor((context.getResources().getColor(R.color.onTertiary)));
            holder.status.setTextColor((context.getResources().getColor(R.color.onTertiary)));
        } else {
            holder.notificationContainer.setBackgroundColor(context.getResources().getColor(R.color.notificationBgColor));
            holder.noti.setTextColor((context.getResources().getColor(R.color.onPrimary)));
            holder.time.setTextColor((context.getResources().getColor(R.color.onSecondary)));
            holder.status.setTextColor((context.getResources().getColor(R.color.primaryColor)));
        }

        holder.itemView.setOnClickListener(v -> {
            if (onNotificationClickListener != null) {
                onNotificationClickListener.onNotificationClick(notification);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noti, time, status;
        LinearLayout notificationContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationContainer = itemView.findViewById(R.id.notification_container);
            noti = itemView.findViewById(R.id.noti);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
        }
    }
}

