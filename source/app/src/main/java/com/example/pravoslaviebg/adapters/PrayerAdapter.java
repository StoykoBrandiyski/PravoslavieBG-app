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
import com.example.pravoslaviebg.models.Saint;
import com.example.pravoslaviebg.models.prayer.Prayer;

import java.util.List;

public class PrayerAdapter extends RecyclerView.Adapter<PrayerAdapter.PrayerViewHolder> {

    public interface OnPrayerClickListener {
        void onClick(Prayer prayer);
    }

    private final List<Prayer> prayers;
    private final PrayerAdapter.OnPrayerClickListener listener;

    public PrayerAdapter(List<Prayer> prayers, OnPrayerClickListener listener) {
        this.prayers = prayers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PrayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_prayer, parent, false);
        return new PrayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrayerViewHolder holder, int position) {
        Prayer prayer = prayers.get(position);
        holder.title.setText(prayer.getTitle());
        Glide.with(holder.itemView).load(prayer.getImageUrl()).into(holder.iconView);
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onClick(prayer));
        }
    }

    @Override
    public int getItemCount() {
        return prayers.size();
    }

    static class PrayerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView iconView;

        public PrayerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textPrayerTitle);
            iconView = itemView.findViewById(R.id.imageIcon);
        }
    }
}
