package com.example.food_ordering_mobile_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.models.location.SuggestionItem;

import java.util.List;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.SuggestionViewHolder> {
    private List<SuggestionItem> suggestions;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(SuggestionItem item);
    }

    public SuggestionsAdapter(List<SuggestionItem> suggestions, OnItemClickListener listener) {
        this.suggestions = suggestions;
        this.onItemClickListener = listener;
    }

    public void updateData(List<SuggestionItem> newSuggestions) {
        this.suggestions = newSuggestions;
        notifyDataSetChanged();
    }

    @Override
    public SuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SuggestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuggestionViewHolder holder, int position) {
        SuggestionItem item = suggestions.get(position);
        holder.suggestionTextView.setText(item.getPlaceName());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView suggestionTextView;

        public SuggestionViewHolder(View itemView) {
            super(itemView);
            suggestionTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}

