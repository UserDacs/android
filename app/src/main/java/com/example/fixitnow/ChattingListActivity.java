package com.example.fixitnow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide; // Import Glide

import com.example.fixitnow.models.ChatUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattingListActivity extends AppCompatActivity {

    private ListView chatListView;
    private List<ChatUser> userList;
    private ChatUserAdapter adapter;
    private SharedPreferences sharedPreferences;

    private static final String API_URL = "http://192.168.1.104/api/v2/message/constac";  // API URL
    private static final String PREFS_NAME = "userPrefs";
    private static final String KEY_TOKEN = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String authToken = sharedPreferences.getString(KEY_TOKEN, null);

        chatListView = findViewById(R.id.chatListView);

        // Initialize the userList
        userList = new ArrayList<>();
        adapter = new ChatUserAdapter(this, userList);
        chatListView.setAdapter(adapter);

        // Fetch messages from the API
        fetchMessages(authToken);

        // Set click listener for the list view
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (userList != null && !userList.isEmpty() && position >= 0 && position < userList.size()) {
                    ChatUser selectedUser = userList.get(position);
                    Intent intent = new Intent(ChattingListActivity.this, ChatActivity.class);
                    intent.putExtra("user_id", selectedUser.getUser_id());
                    intent.putExtra("name", selectedUser.getName());
                    intent.putExtra("image", selectedUser.getProfileImageUrl());  // Pass image URL
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ChattingListActivity.this, ChatActivity.class);
                    intent.putExtra("user_id", "6");
                    intent.putExtra("name", "Admin Admin");
                    intent.putExtra("image", "/storage/profile_images/pyRJuA0KCutECJvNL8tSZiJwnOXJ1bgKNCuroUp8.png");  // Pass image URL
                    startActivity(intent);
                }
            }
        });
    }

    private void fetchMessages(String authToken) {
        if (authToken == null) {
            Toast.makeText(this, "Authentication Token is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set the headers for the API request
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + authToken);
        headers.put("Accept", "application/json");

        // Use JsonArrayRequest instead of JsonObjectRequest
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Clear existing data
                            userList.clear();

                            // Loop through the JSON array
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject message = response.getJSONObject(i);

                                // Get the necessary data for each chat user
                                String user_id = message.optString("user_id");
                                String userName = message.optString("user_name");
                                String messageContent = message.optString("message_content");
                                String messageTime = message.optString("created_at_human");
                                String userImagePath = "http://192.168.1.104" + message.optString("user_image_path");

                                // Create a new ChatUser object and add it to the list
                                ChatUser chatUser = new ChatUser(userName, messageContent, messageTime, userImagePath,user_id);
                                userList.add(chatUser);
                            }

                            // Notify the adapter that the data has been updated
                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ChattingListActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Request", "Error: " + error);
                        Toast.makeText(ChattingListActivity.this, "Failed to fetch messages", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }
}
