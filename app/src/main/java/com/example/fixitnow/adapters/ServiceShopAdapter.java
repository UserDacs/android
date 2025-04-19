package com.example.fixitnow.adapters;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.LoginActivity;
import com.example.fixitnow.MainActivity;
import com.example.fixitnow.R;
import com.example.fixitnow.ServiceViewDetailsActivity;

import java.util.List;

public class ServiceShopAdapter extends RecyclerView.Adapter<ServiceShopAdapter.ViewHolder> {
    List<String> services;

    public ServiceShopAdapter(List<String> services) {
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

        holder.txtService.setText(services.get(position));
        holder.txtService.setOnClickListener(v -> {
//            Toast.makeText(v.getContext(), "Clicked: " + services.get(position), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(v.getContext(), ServiceViewDetailsActivity.class);
            v.getContext().startActivity(intent);

//            // Finish the current activity safely
//            if (v.getContext() instanceof Activity) {
//                ((Activity) v.getContext()).finish();
//            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}