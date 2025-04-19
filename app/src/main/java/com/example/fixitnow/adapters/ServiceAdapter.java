package com.example.fixitnow.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fixitnow.BookActivity;
import com.example.fixitnow.R;
import com.example.fixitnow.ServiceViewDetailsActivity;
import com.example.fixitnow.ServicesViewListActivity;
import com.example.fixitnow.models.ServiceModel;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    List<ServiceModel> serviceList;
    Context context;
    public ServiceAdapter(Context context, List<ServiceModel> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtServiceName, txtShopName, txtTechnician, txtRatings, txtRatingtext;
        ImageButton btnViewDetails, btnBookNow;

        ImageView imgService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtShopName = itemView.findViewById(R.id.txtShopName);
            txtTechnician = itemView.findViewById(R.id.txtTechnician);
            txtRatings = itemView.findViewById(R.id.txtRatings);
            txtRatingtext = itemView.findViewById(R.id.txtRatingtext);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnBookNow = itemView.findViewById(R.id.btnBookNow);

            imgService = itemView.findViewById(R.id.imgService);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // If serviceList is empty, load item_empty.xml, else load item_service_detail.xml
        View v;
        if (serviceList.isEmpty()) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_detail, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!serviceList.isEmpty()) {
            ServiceModel s = serviceList.get(position);
            holder.txtServiceName.setText(s.getServiceName());
            holder.txtShopName.setText(s.getShopName());
            holder.txtTechnician.setText(s.getTechnicianName());

            double rating = s.getRating();
            String starRating;

            if (rating <= 0.0) {
                starRating = "☆☆☆☆☆";
            } else if (rating == 1.0) {
                starRating = "★☆☆☆☆";
            } else if (rating > 1.0 && rating < 2.0) {
                starRating = "★✬☆☆☆";
            } else if (rating == 2.0) {
                starRating = "★★☆☆☆";
            } else if (rating > 2.0 && rating < 3.0) {
                starRating = "★★✬☆☆";
            } else if (rating == 3.0) {
                starRating = "★★★☆☆";
            } else if (rating > 3.0 && rating < 4.0) {
                starRating = "★★★✬☆";
            } else if (rating == 4.0) {
                starRating = "★★★★☆";
            } else if (rating > 4.0 && rating < 5.0) {
                starRating = "★★★★✬";
            } else { // rating == 5.0 or above
                starRating = "★★★★★";
            }

            holder.txtRatings.setText(starRating);
            holder.txtRatingtext.setText("("+s.getRating()+")");

            Glide.with(context)
                    .load(s.getImageUrl()) // the URL from your model
                    .placeholder(R.drawable.circle_bg) // optional: show while loading
                    .error(R.drawable.avatar) // optional: show if there's an error
                    .into(holder.imgService);

            holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ServiceViewDetailsActivity.class);
                    intent.putExtra("service_id", s.getServiceId()); // ← ipapasa ang ID
                    context.startActivity(intent);
                }
            });

            holder.btnBookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BookActivity.class);
                    intent.putExtra("service_id", s.getServiceId()); // ← ipapasa ang ID
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        // If serviceList is empty, return 1 for the empty view, otherwise return the actual size
        return serviceList.isEmpty() ? 1 : serviceList.size();
    }
}
