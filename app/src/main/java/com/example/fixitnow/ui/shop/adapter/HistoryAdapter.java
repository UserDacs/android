package com.example.fixitnow.ui.shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.ui.shop.models.HistoryModel;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<HistoryModel> historyList;

    public HistoryAdapter(List<HistoryModel> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        HistoryModel item = historyList.get(position);

        holder.imgProfile.setImageResource(item.getImageResId());
        holder.tvClientName.setText(item.getClientName());
        holder.tvDateTime.setText(item.getDateTime());
        holder.tvLocation.setText(item.getLocation());
        holder.tvRatings.setText("Ratings: " + item.getRatings());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView tvClientName, tvDateTime, tvLocation, tvRatings;
        Button btnView;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvRatings = itemView.findViewById(R.id.tvRatings);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}