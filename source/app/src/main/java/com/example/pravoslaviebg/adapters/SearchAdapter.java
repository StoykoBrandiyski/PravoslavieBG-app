package com.example.pravoslaviebg.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pravoslaviebg.R;
import com.example.pravoslaviebg.models.Saint;
import com.example.pravoslaviebg.models.search.ResultItem;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> {
    private final OnSearchClickListener listener;

    private List<ResultItem> results;

    public interface OnSearchClickListener {
        void onClick(ResultItem item);
    }

    public SearchAdapter(List<ResultItem> results, OnSearchClickListener listener) {
        this.results = results;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        ResultItem item = results.get(position);
        holder.title.setText(item.title);
        holder.subtitle.setText(item.locationOrOther);
        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateResults(List<ResultItem> newResults) {
        this.results = newResults;
        notifyDataSetChanged();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        public ResultViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.resultTitle);
            subtitle = itemView.findViewById(R.id.resultSubtitle);
        }
    }
}
