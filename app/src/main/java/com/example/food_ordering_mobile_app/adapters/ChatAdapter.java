package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.chat.Chat;
import com.example.food_ordering_mobile_app.models.chat.Message;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context context;
    private List<Chat> chatList;
    private OnChatClickListener onChatClickListener;
    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    public ChatAdapter(Context context, List<Chat> chatList, OnChatClickListener onChatClickListener) {
        this.context = context;
        this.chatList = chatList;
        this.onChatClickListener = onChatClickListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        holder.tvUserName.setText(chat.getUsers().get(1).getName());
        holder.tvLatestMessage.setText(chat.getLatestMessage().getContent());

        Timestamp timestamp = chat.getUpdatedAt();
        Date date = new Date(timestamp.getTime());
        Calendar now = Calendar.getInstance();
        Calendar chatTime = Calendar.getInstance();
        chatTime.setTime(date);

        // Kiểm tra nếu cùng một ngày
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());

        String formattedDate;
        if (now.get(Calendar.YEAR) == chatTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == chatTime.get(Calendar.DAY_OF_YEAR)) {
            formattedDate = timeFormat.format(date); // Cùng ngày, hiển thị HH:mm
        } else {
            formattedDate = dateFormat.format(date); // Khác ngày, hiển thị dd/MM
        }

        holder.tvCreatedTime.setText(formattedDate);


        String userAvatarUrl = chat.getUsers().get(1).getAvatar() != null ? chat.getUsers().get(1).getAvatar().getUrl() : null;
        Glide.with(context)
                .asBitmap()
                .load(userAvatarUrl)
                .override(60, 60)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.ivUserAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedDrawable.setCornerRadius(6);
                        holder.ivUserAvatar.setImageDrawable(roundedDrawable);
                    }
                });

        holder.itemView.setOnClickListener(v -> {
            if (onChatClickListener != null) {
                onChatClickListener.onChatClick(chat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvLatestMessage, tvCreatedTime;
        ImageView ivUserAvatar;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvLatestMessage = itemView.findViewById(R.id.tvLatestMessage);
            tvCreatedTime = itemView.findViewById(R.id.tvCreatedTime);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
        }
    }
}

