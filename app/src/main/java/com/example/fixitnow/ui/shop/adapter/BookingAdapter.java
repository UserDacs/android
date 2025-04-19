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
import com.example.fixitnow.ui.shop.models.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.imgProfile.setImageResource(booking.getImageResId());
        holder.tvClientName.setText(booking.getClientName());
        holder.tvDateTime.setText(booking.getDateTime());
        holder.tvLocation.setText(booking.getLocation());

        // Optional: Set click listeners for buttons
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView tvClientName, tvDateTime, tvLocation;
        Button btnAccept, btnDecline, btnView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}