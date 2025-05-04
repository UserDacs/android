package com.example.fixitnow.adapters;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.fixitnow.ChatActivity;
import com.example.fixitnow.ChattingListActivity;
import com.example.fixitnow.R;
import com.example.fixitnow.models.Booking;
import com.example.fixitnow.utils.Constants;
import com.example.fixitnow.utils.PreferenceManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        holder.status.setText("" + booking.getStatus());

        holder.idLocation.setText("Location: "+ booking.getLocation());

        switch (booking.getStatus().toLowerCase()) {
            case "pending":
                holder.status.setTextColor(0xFFFFA500); // orange
                break;
            case "accept":
                holder.status.setTextColor(0xFF1E90FF); // blue
                break;
            case "done":
                holder.status.setTextColor(0xFF32CD32); // green
                break;
            case "cancel":
                holder.status.setTextColor(0xFFFF0000); // red
                break;
        }

        String lat = booking.getLat();
        String _long = booking.getLon();

        // Long press listener
        holder.itemView.setOnLongClickListener(v -> {
            PreferenceManager preferenceManager = new PreferenceManager(context);
            String role = preferenceManager.getUserRole();

            final String[] options;
            if ("provider".equals(role)) {
                options = new String[]{
                        "✅   Accept",
                        "✔️   Done",
                        "❌   Cancel",
                        "📍   Open Location Pin",
                        "💬   Send Message"
                };
            } else {
                options = new String[]{
                        "❌   Cancel",
                        "💬   Send Message"
                };
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, options) {
                @NonNull
                @Override
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = view.findViewById(android.R.id.text1);
                    text.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    text.setPadding(48, 24, 24, 24);
                    text.setTextSize(18);
                    return view;
                }
            };

            new AlertDialog.Builder(context)
                    .setAdapter(adapter, (dialog, which) -> {
                        if ("provider".equals(role)) {
                            switch (which) {
                                case 0:
                                    updateBookingStatus(context, Integer.parseInt(booking.getBooking_id()), "accept", position);
                                    break;
                                case 1:
                                    updateBookingStatus(context, Integer.parseInt(booking.getBooking_id()), "done", position);
                                    break;
                                case 2:
                                    updateBookingStatus(context, Integer.parseInt(booking.getBooking_id()), "cancel", position);
                                    break;
                                case 3:
                                    showMapDialog(booking.getLat(), booking.getLon());
                                    break;
                                case 4:
                                    openChatCus(booking);
                                    break;
                            }
                        } else {
                            switch (which) {
                                case 0:
                                    updateBookingStatus(context, Integer.parseInt(booking.getBooking_id()), "cancel", position);
                                    break;
                                case 1:
                                    openChatShop(booking);
                                    break;
                            }
                        }
                    })
                    .show();

            return true;
        });

    }
    private void showMapDialog(String latStr, String lonStr) {
        double lat = Double.parseDouble(latStr);
        double lon = Double.parseDouble(lonStr);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_map, null);
        builder.setView(dialogView);
        android.app.AlertDialog dialog = builder.create();

        MapView mapView = dialogView.findViewById(R.id.mapView);
        mapView.onCreate(null);
        mapView.onResume();

        mapView.getMapAsync(googleMap -> {
            LatLng location = new LatLng(lat, lon);
            googleMap.addMarker(new MarkerOptions().position(location).title("Booking Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        });

        dialog.show();
    }

    private void openChatCus(Booking booking) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("user_id", booking.getCustomer_id());
        intent.putExtra("name", booking.getCusFullname());
        intent.putExtra("image", Constants.getFullApiUrl(booking.getCustomer_image()));
        Log.d(TAG,"SMMM: "+ booking.getCustomer_image());
        context.startActivity(intent);
    }

    private void openChatShop(Booking booking) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("user_id", booking.getShop_user_id());
        intent.putExtra("name", booking.getShop_user_fullname());
        intent.putExtra("image", Constants.getFullApiUrl(booking.getShop_user_image_path()));
        context.startActivity(intent);
    }

    private void updateBookingStatus(Context context, int bookingId, String status, int position) {
        String url = Constants.getFullApiUrl("/api/booking/update-status");
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", bookingId);
            jsonBody.put("status", status);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    response -> {
                        Toast.makeText(context, "Status updated to: " + status, Toast.LENGTH_SHORT).show();

                        // ✅ Update status in the list
                        bookingList.get(position).setStatus(status);

                        // ✅ Notify adapter about the item change
                        notifyItemChanged(position);
                    },
                    error -> {
                        Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };

            Volley.newRequestQueue(context).add(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "JSON error", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, techName, duration, dateTime, status, idLocation;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.bookingImage);
            name = itemView.findViewById(R.id.bookingName);
            techName = itemView.findViewById(R.id.technicianName);
            duration = itemView.findViewById(R.id.duration);
            dateTime = itemView.findViewById(R.id.bookingDateTime);
            status = itemView.findViewById(R.id.status);
            idLocation = itemView.findViewById(R.id.idLocation);
        }
    }
}
