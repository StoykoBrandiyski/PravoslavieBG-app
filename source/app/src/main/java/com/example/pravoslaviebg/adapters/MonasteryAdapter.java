package com.example.pravoslaviebg.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pravoslaviebg.R;
import com.example.pravoslaviebg.models.monastery.Monastery;
import com.example.pravoslaviebg.models.prayer.Prayer;

import java.util.ArrayList;
import java.util.List;

public class MonasteryAdapter extends RecyclerView.Adapter<MonasteryAdapter.MonasteryViewHolder> {

    public interface OnMonasteryClickListener {
        void onClick(Monastery prayer);
    }

    private final MonasteryAdapter.OnMonasteryClickListener listener;

    private final List<Monastery> monasteries;

    public MonasteryAdapter(List<Monastery> monasteries, OnMonasteryClickListener listener) {
        this.monasteries = new ArrayList<>(monasteries);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MonasteryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_monastery, parent, false);
        return new MonasteryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonasteryViewHolder holder, int position) {
        Monastery monastery = monasteries.get(position);
        holder.name.setText(monastery.getName());
        holder.location.setText(monastery.getLocation());
        Glide.with(holder.itemView).load(monastery.getImageUrl()).into(holder.imageView);
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onClick(monastery));
        }
    }

    @Override
    public int getItemCount() {
        return monasteries.size();
    }

    public void updateData(List<Monastery> newList) {
        monasteries.clear();
        monasteries.addAll(newList);
        notifyDataSetChanged();
    }

    static class MonasteryViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView location;
        ImageView imageView;

        public MonasteryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textMonasteryName);
            location = itemView.findViewById(R.id.textMonasteryLocation);
            imageView = itemView.findViewById(R.id.imageMainMonastery);
        }
    }
}
