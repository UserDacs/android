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
import com.example.fixitnow.ui.shop.models.Technician;

import java.util.List;

public class TechnicianAdapter extends RecyclerView.Adapter<TechnicianAdapter.TechnicianViewHolder> {

    private final List<Technician> technicianList;

    public TechnicianAdapter(List<Technician> technicianList) {
        this.technicianList = technicianList;
    }

    @NonNull
    @Override
    public TechnicianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_technician, parent, false);
        return new TechnicianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechnicianViewHolder holder, int position) {
        Technician technician = technicianList.get(position);
        holder.imageProfile.setImageResource(technician.getImageResId());
        holder.tvTechName.setText(technician.getName());
        holder.tvRatings.setText("Ratings: " + technician.getRatings());
        holder.tvExpertise.setText("Expertise: " + technician.getExpertise());

        // Optional: setup click listeners
    }

    @Override
    public int getItemCount() {
        return technicianList.size();
    }

    static class TechnicianViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView tvTechName, tvRatings, tvExpertise;
        Button btnEditTech, btnRemoveTech;

        TechnicianViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            tvTechName = itemView.findViewById(R.id.tvTechName);
            tvRatings = itemView.findViewById(R.id.tvRatings);
            tvExpertise = itemView.findViewById(R.id.tvExpertise);
            btnEditTech = itemView.findViewById(R.id.btnEditTech);
            btnRemoveTech = itemView.findViewById(R.id.btnRemoveTech);
        }
    }
}
