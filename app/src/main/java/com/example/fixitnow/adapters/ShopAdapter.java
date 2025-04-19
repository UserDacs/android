package com.example.fixitnow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.models.ShopModel;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    List<ShopModel> shopList;

    public ShopAdapter(List<ShopModel> shopList) {
        this.shopList = shopList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtShopName, txtRatings, txtLocation;
        ImageView imgShop, imgDropdown;
        RecyclerView recyclerServices;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShopName = itemView.findViewById(R.id.txtShopName);
            txtRatings = itemView.findViewById(R.id.txtRatings);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            imgShop = itemView.findViewById(R.id.imgShop);
            imgDropdown = itemView.findViewById(R.id.imgDropdown);
            recyclerServices = itemView.findViewById(R.id.recyclerServices);
        }
    }

    @NonNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {
        ShopModel shop = shopList.get(position);

        holder.txtShopName.setText(shop.shopName);
        holder.txtRatings.setText(shop.rating);
        holder.txtLocation.setText(shop.location);

        // Service RecyclerView
        holder.recyclerServices.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerServices.setAdapter(new ServiceShopAdapter(shop.services));
        holder.recyclerServices.setVisibility(shop.isExpanded ? View.VISIBLE : View.GONE);

        // Dropdown animation
        holder.imgDropdown.setRotation(shop.isExpanded ? 180 : 0);
        holder.imgDropdown.setOnClickListener(v -> {
            shop.isExpanded = !shop.isExpanded;
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
}
