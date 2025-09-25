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

import java.util.List;

public class SaintAdapter extends RecyclerView.Adapter<SaintAdapter.ViewHolder> {

    public interface OnSaintClickListener {
        void onClick(Saint saint);
    }

    private final List<Saint> saints;
    private final OnSaintClickListener listener;

    public SaintAdapter(List<Saint> saints, OnSaintClickListener listener) {
        this.saints = saints;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SaintAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_saint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaintAdapter.ViewHolder holder, int position) {
        Saint saint = saints.get(position);
        holder.nameText.setText(saint.Name);
        holder.saintMindText.setText(saint.SaintMind);
        Glide.with(holder.itemView).load(saint.MainImageUrl).into(holder.iconView);
        holder.itemView.setOnClickListener(v -> listener.onClick(saint));
    }

    @Override
    public int getItemCount() {
        return saints.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView saintMindText;
        ImageView iconView;

        ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textName);
            saintMindText = itemView.findViewById(R.id.textSaintMind);
            iconView = itemView.findViewById(R.id.imageIcon);
        }
    }
}
