package com.example.fixitnow.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fixitnow.R;
import com.example.fixitnow.adapters.NotificationAdapter;
import com.example.fixitnow.models.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = root.findViewById(R.id.notification_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(notificationAdapter);

        fetchNotifications(root);

        return root;
    }

    private void fetchNotifications(View root) {
        String url = "http://192.168.1.100/api/notifications/list?userId=3"; // Replace with your local IP
        TextView noDataText = root.findViewById(R.id.no_data_text);
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject notifObj = response.getJSONObject(i);

                            JSONObject data = new JSONObject(notifObj.getString("data"));
                            String title = data.getString("title");
                            String message = data.getString("message");

                            String userName = notifObj.getString("user_name");
                            String imageUrl = "http://192.168.1.100" + notifObj.getString("user_image");
                            String createdAt = notifObj.getString("created_at");

                            String timeAgo = getTimeAgo(createdAt);

                            notificationList.add(new Notification(userName, title + ": " + message, timeAgo, imageUrl));
                        }

                        if (notificationList.isEmpty()) {
                            noDataText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noDataText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            notificationAdapter.notifyDataSetChanged();
                        }

                        notificationAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Volley", "Error: " + error.getMessage())
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer 5|IUgzlSBWL6kdpupJx0z41eG90cUR9taoVVI7db5o344de468");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(request);
    }

    private String getTimeAgo(String createdAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date past = sdf.parse(createdAt);
            Date now = new Date();
            long seconds = (now.getTime() - past.getTime()) / 1000;

            if (seconds < 60)
                return "Just now";
            else if (seconds < 3600)
                return (seconds / 60) + " mins ago";
            else if (seconds < 86400)
                return (seconds / 3600) + " hrs ago";
            else
                return (seconds / 86400) + " days ago";

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
