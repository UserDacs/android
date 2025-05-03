package com.example.fixitnow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.models.MyService;

import java.util.List;

public class MyServiceAdapter extends RecyclerView.Adapter<MyServiceAdapter.MyServiceViewHolder> {

    private List<MyService> services; // Changed from Service to MyService

    public MyServiceAdapter(List<MyService> services) {
        this.services = services;
    }

    public static class MyServiceViewHolder extends RecyclerView.ViewHolder {
        public TextView tvServiceName;
        public TextView tvServiceDescription;

        public MyServiceViewHolder(View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServiceDescription = itemView.findViewById(R.id.tvServiceDescription);
        }
    }

    @NonNull
    @Override
    public MyServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_service, parent, false);
        return new MyServiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyServiceViewHolder holder, int position) {
        MyService currentService = services.get(position); // Changed from Service to MyService
        holder.tvServiceName.setText(currentService.getName());
        holder.tvServiceDescription.setText(currentService.getDescription());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}