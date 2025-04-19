package com.example.fixitnow.ui.shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fixitnow.R;
import com.example.fixitnow.ui.shop.models.ShopService;

import java.util.List;

public class ShopServiceAdapter extends RecyclerView.Adapter<ShopServiceAdapter.ViewHolder> {

    private final List<ShopService> serviceList;

    public ShopServiceAdapter(List<ShopService> serviceList) {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ShopServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopServiceAdapter.ViewHolder holder, int position) {
        ShopService service = serviceList.get(position);
        holder.tvServiceName.setText(service.getServiceName());
        holder.tvPrice.setText("₱" + service.getPrice());
        holder.tvTechnician.setText("Technician: " + service.getTechnician());

        holder.tvRatings.setText("Ratings: " + service.getRatings());


        holder.btnEdit.setOnClickListener(v -> {
            // TODO: Handle edit logic
        });

        holder.btnRemove.setOnClickListener(v -> {
            // TODO: Handle remove logic
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvPrice, tvTechnician ,tvRatings;
        Button btnEdit, btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTechnician = itemView.findViewById(R.id.tvTechnician);

            tvRatings = itemView.findViewById(R.id.tvRatings);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
