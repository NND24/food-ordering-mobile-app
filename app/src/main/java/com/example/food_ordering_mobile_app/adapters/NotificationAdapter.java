package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.notification.Notification;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

        holder.tvTitle.setText(notification.getTitle());
        holder.tvMessage.setText(notification.getMessage());

        Timestamp timestamp = notification.getUpdatedAt();

        // Lấy múi giờ Việt Nam
        TimeZone vietnamTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");

        // Đặt SimpleDateFormat sử dụng múi giờ Việt Nam
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        sdf.setTimeZone(vietnamTimeZone);

        // Chuyển đổi thời gian từ UTC sang giờ Việt Nam
        Date vietnamDate = new Date(timestamp.getTime() + vietnamTimeZone.getOffset(timestamp.getTime()));

        String formattedDate = sdf.format(vietnamDate);
        holder.tvDate.setText(formattedDate);

        Boolean isRead = "read".equals(notification.getStatus());

        if (isRead) {
            holder.notificationContainer.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
            holder.tvTitle.setTextColor((context.getResources().getColor(R.color.onTertiary)));
            holder.tvMessage.setTextColor((context.getResources().getColor(R.color.onTertiary)));
            holder.tvDate.setTextColor((context.getResources().getColor(R.color.onTertiary)));
            holder.tvStatus.setTextColor((context.getResources().getColor(R.color.onTertiary)));
        } else {
            holder.notificationContainer.setBackgroundColor(context.getResources().getColor(R.color.notificationBgColor));
            holder.tvTitle.setTextColor((context.getResources().getColor(R.color.onPrimary)));
            holder.tvMessage.setTextColor((context.getResources().getColor(R.color.onPrimary)));
            holder.tvDate.setTextColor((context.getResources().getColor(R.color.onSecondary)));
            holder.tvStatus.setTextColor((context.getResources().getColor(R.color.primaryColor)));
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
        TextView tvTitle, tvMessage, tvDate, tvStatus;
        LinearLayout notificationContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationContainer = itemView.findViewById(R.id.notification_container);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}

