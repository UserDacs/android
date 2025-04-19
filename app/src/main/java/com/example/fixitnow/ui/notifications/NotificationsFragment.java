package com.example.fixitnow.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.adapters.NotificationAdapter;
import com.example.fixitnow.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = root.findViewById(R.id.notification_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize list and add sample data
        notificationList = new ArrayList<>();
        notificationList.add(new Notification("John Doe", "Sent you a friend request.", "1 hr ago", R.drawable.ic_launcher_foreground));
        notificationList.add(new Notification("Jane Smith", "Liked your post.", "2 hrs ago", R.drawable.ic_launcher_foreground));
        notificationList.add(new Notification("Mark Dacoylo", "Commented on your photo.", "3 hrs ago", R.drawable.ic_launcher_foreground));
        notificationList.add(new Notification("Lilly Brown", "Tagged you in a post.", "4 hrs ago", R.drawable.ic_launcher_foreground));
        notificationList.add(new Notification("Sophia Lee", "Shared a new post.", "5 hrs ago", R.drawable.ic_launcher_foreground));
        notificationList.add(new Notification("David Johnson", "Started following you.", "6 hrs ago", R.drawable.ic_launcher_foreground));



        // Set the adapter
        notificationAdapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(notificationAdapter);

        return root;
    }
}
