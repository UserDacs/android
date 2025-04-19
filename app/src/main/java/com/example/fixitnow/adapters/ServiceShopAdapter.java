package com.example.fixitnow.adapters;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.ServiceViewDetailsActivity;
import com.example.fixitnow.models.ServiceListModel;

import java.util.List;

public class ServiceShopAdapter extends RecyclerView.Adapter<ServiceShopAdapter.ViewHolder> {
    List<ServiceListModel> services;

    public ServiceShopAdapter(List<ServiceListModel> services) {
        this.services = services;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtService;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtService = itemView.findViewById(R.id.txtService);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceListModel model = services.get(position);
        holder.txtService.setText(model.serviceName);

        holder.txtService.setOnClickListener(v -> {
//            Log.d("SERVICE_ID", "Clicked serviceId: " + model.serviceId);
//
//            Log.d("SERVICE_ID", "Clicked serviceId: " + model.serviceName);

            Intent intent = new Intent(v.getContext(), ServiceViewDetailsActivity.class);
            intent.putExtra("service_id", Integer.parseInt(model.serviceId));
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}