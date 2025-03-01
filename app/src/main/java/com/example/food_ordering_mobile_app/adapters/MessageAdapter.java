package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<Message> messageList;
    private OnMessageClickListener onMessageClickListener;
    public interface OnMessageClickListener {
        void onMessageClick(Message message);
    }

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    public MessageAdapter(Context context, List<Message> messageList, OnMessageClickListener onMessageClickListener) {
        this.context = context;
        this.messageList = messageList;
        this.onMessageClickListener = onMessageClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(position);

        holder.name.setText(message.getName());
        holder.message.setText(message.getMessage());
        holder.createdTime.setText(message.getCreatedTime());

        int resourceId = Integer.parseInt(message.getAvatar());
        Glide.with(context)
                .load(resourceId)
                .circleCrop()
                .into(holder.avatar);

        holder.itemView.setOnClickListener(v -> {
            if (onMessageClickListener != null) {
                onMessageClickListener.onMessageClick(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, message, createdTime;
        ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            createdTime = itemView.findViewById(R.id.createdTime);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}

