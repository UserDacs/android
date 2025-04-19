package com.example.fixitnow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.models.ServiceShopModel;

import java.util.List;

public class ShopServiceAdapter extends RecyclerView.Adapter<ShopServiceAdapter.ViewHolder> {

    List<ServiceShopModel> serviceList1;

    public ShopServiceAdapter(List<ServiceShopModel> serviceList) {
        this.serviceList1 = serviceList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtTech;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtServiceName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtTech = itemView.findViewById(R.id.txtTech);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceShopModel s = serviceList1.get(position);
        holder.txtName.setText(s.name);
        holder.txtPrice.setText(s.price);
        holder.txtTech.setText("Technician: " + s.technician);
    }

    @Override
    public int getItemCount() {
        return serviceList1.size();
    }
}
