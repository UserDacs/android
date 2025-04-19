package com.example.fixitnow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fixitnow.R;
import com.example.fixitnow.models.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private List<Booking> bookingList;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // Load image from URL
        Glide.with(context)
                .load(booking.getImageUrl())
                .placeholder(R.drawable.avatar) // optional
                .error(R.drawable.no_image_1)             // optional
                .into(holder.image);

        holder.name.setText(booking.getName());
        holder.techName.setText("Technician: " + booking.getTechnicianName());
        holder.duration.setText("Duration: " + booking.getDuration());
        holder.dateTime.setText(booking.getBookingDateTime());
        holder.status.setText("Status: " + booking.getStatus());

        switch (booking.getStatus().toLowerCase()) {
            case "pending":
                holder.status.setTextColor(0xFFFFA500); // orange
                break;
            case "ongoing":
                holder.status.setTextColor(0xFF1E90FF); // blue
                break;
            case "done":
                holder.status.setTextColor(0xFF32CD32); // green
                break;
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, techName, duration, dateTime, status;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.bookingImage);
            name = itemView.findViewById(R.id.bookingName);
            techName = itemView.findViewById(R.id.technicianName);
            duration = itemView.findViewById(R.id.duration);
            dateTime = itemView.findViewById(R.id.bookingDateTime);
            status = itemView.findViewById(R.id.status);
        }
    }
}
