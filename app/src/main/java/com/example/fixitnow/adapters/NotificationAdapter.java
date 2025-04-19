package com.example.fixitnow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);  // Assuming notification_item.xml exists
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.name.setText(notification.getName());
        holder.description.setText(notification.getDescription());
        holder.time.setText(notification.getTime());
        holder.profileImage.setImageResource(notification.getProfileImageRes());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, time;
        ImageView profileImage;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.notification_name_1);  // Adjust as per ID in XML
            description = itemView.findViewById(R.id.notification_description_1);  // Adjust as per ID in XML
            time = itemView.findViewById(R.id.notification_time_1);  // Adjust as per ID in XML
            profileImage = itemView.findViewById(R.id.profile_image);  // Adjust as per ID in XML
        }
    }
}
