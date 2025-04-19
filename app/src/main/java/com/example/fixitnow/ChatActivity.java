package com.example.fixitnow;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout messageContainer;
    private ScrollView messageScroll;
    private EditText editTextMessage;
    private ImageButton sendButton;
    private ImageView profileImage;
    private TextView userName;

    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "userPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String MESSAGE_COUNT_KEY = "message_count";
    private static final String TAG = "SocketIO";




    private Socket mSocket;
    private int senderId = 5;
    private int receiverId = 6;

    private View typingIndicatorView;

    private Handler typingTimeoutHandler = new Handler();
    private Runnable typingTimeoutRunnable;
    private static final int TYPING_TIMEOUT = 1000;

    private Handler typingHandler = new Handler();
    private Runnable stopTypingRunnable;

    private boolean isTyping = false;

    private String user_id = "";

    private String IMAGE_URL = "http://192.168.1.104";

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        progressBar = findViewById(R.id.progressBar);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {



                emitStopTyping(); // your method
                editTextMessage.setText("");
                editTextMessage.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editTextMessage.getWindowToken(), 0);
                }
                finish(); // finish the activity (acts like super.onBackPressed())
            }
        });

        messageContainer = findViewById(R.id.messageContainer);
        messageScroll = findViewById(R.id.messageScroll);
        editTextMessage = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.sendButton);
//        profileImage = findViewById(R.id.userProfile);
        userName = findViewById(R.id.chatUserName);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String name = getIntent().getStringExtra("name");
        String img = getIntent().getStringExtra("image");

        user_id = getIntent().getStringExtra("user_id");


        receiverId = Integer.parseInt(user_id);

        Log.e(TAG, "user_id :::"+ user_id);

        ImageView avatarImage = findViewById(R.id.userProfile);
        Glide.with(this)
                .load(img)
                .placeholder(R.drawable.avatar) // optional fallback image
                .error(R.drawable.baseline_check_box_outline_blank_24)         // optional error image
                .into(avatarImage);


        userName.setText(name);
//        profileImage.setImageResource(img);

        initSocket();
        loadConversation();

        sendButton.setOnClickListener(v -> {
            emitStopTyping();
            String message = editTextMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessageToServer(message);

                editTextMessage.setText("");
                editTextMessage.clearFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editTextMessage.getWindowToken(), 0);
                }

                // ✅ Increment and show message count
                int count = sharedPreferences.getInt(MESSAGE_COUNT_KEY, 0);
                count++;
                sharedPreferences.edit().putInt(MESSAGE_COUNT_KEY, count).apply();
                Toast.makeText(this, "📤 Messages sent: " + count, Toast.LENGTH_SHORT).show();
            }
        });


        // Set focus listener
        editTextMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // User has focused on the EditText, start typing indication
                emitTyping();
                startTypingTimer(); // Start the 7-second timer
            }
        });

        // Add TextWatcher for detecting typing
        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!isTyping) {
                    // If the user wasn't typing previously, start emitting typing
                    emitTyping();
                    isTyping = true;
                }

                // Reset the typing timer every time text is changed
                resetTypingTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });





    }

    private void startDotBounceAnimation(View dot1, View dot2, View dot3) {
        long delay = 150;

        animateDot(dot1, 0);
        animateDot(dot2, delay);
        animateDot(dot3, delay * 2);
    }

    private void animateDot(View dot, long startDelay) {
        ObjectAnimator bounceAnim = ObjectAnimator.ofFloat(dot, "translationY", 0f, -15f, 0f);
        bounceAnim.setDuration(600);
        bounceAnim.setStartDelay(startDelay);
        bounceAnim.setRepeatCount(ValueAnimator.INFINITE);
        bounceAnim.setRepeatMode(ValueAnimator.RESTART);
        bounceAnim.start();
    }

    // Start or restart the 7-second timer
    private void startTypingTimer() {
        if (stopTypingRunnable != null) {
            typingHandler.removeCallbacks(stopTypingRunnable);
        }

        stopTypingRunnable = new Runnable() {
            @Override
            public void run() {
                emitStopTyping();  // Emit stop typing if no typing after the timeout
                isTyping = false;   // Reset typing flag
            }
        };

        typingHandler.postDelayed(stopTypingRunnable, TYPING_TIMEOUT);
    }

    // Reset the typing timer
    private void resetTypingTimer() {
        typingHandler.removeCallbacks(stopTypingRunnable);
        startTypingTimer(); // Restart the 7-second timer every time the user types
    }

    private void initSocket() {
        try {
            mSocket = IO.socket("http://192.168.1.104:3000");
            mSocket.connect();

            mSocket.on(Socket.EVENT_CONNECT, args -> Log.d(TAG, "✅ Connected to Socket.IO server"));
            mSocket.on("receive-message", onReceiveMessage);
            mSocket.on("typing", onTyping);
            mSocket.on("stop-typing", onStopTyping);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "❌ Socket connection failed.");
        }
    }

    private final Emitter.Listener onReceiveMessage = args -> runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        try {
            int receivedId = data.getInt("receiver_id");
            if (receivedId == senderId) {
                removeTypingIndicator();
                String msg = data.getString("message");
                addBubble(msg, false,IMAGE_URL + data.getString("image_path"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    private final Emitter.Listener onTyping = args -> runOnUiThread(() -> {
        // Assuming that args[0] contains a JSON object with typing data
        if (args != null && args.length > 0) {
            // Extract the data from args (assuming it is a JSON object)
            JSONObject typingData = (JSONObject) args[0];

            // Extract specific data from the typing data object
            String userId = typingData.optString("userId", "Unknown User");
            String message = typingData.optString("message", "");

            String image_pa = typingData.optString("image_path", "");

            // Log the typing information
            Log.d(TAG, "✏️ " + userId + " is typing: " + typingData);

            String fullpath = IMAGE_URL + image_pa;

            // Show typing indicator
            showTypingIndicator(fullpath);
        }
    });

    private final Emitter.Listener onStopTyping = args -> runOnUiThread(() -> {
        Log.d(TAG, "🛑 Typing stopped.");
        removeTypingIndicator();
    });

    private void emitSocketMessage(JSONObject data) {
        try {
            JSONObject messagePayload = new JSONObject();
            messagePayload.put("sender_name", data.getString("sender_name"));
            messagePayload.put("sender_id", data.getString("sender_id"));
            messagePayload.put("receiver_id", data.getString("receiver_id"));
            messagePayload.put("message", data.getString("message"));
            messagePayload.put("image_path", data.getString("sender_image"));
            messagePayload.put("reply_to_id", data.getString("reply_to_id"));
            messagePayload.put("created_at", data.getString("created_at"));
            messagePayload.put("created_at_human", data.getString("created_at_human"));
            messagePayload.put("updated_at", data.getString("updated_at"));

            mSocket.emit("send-message", messagePayload);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void emitTyping() {
        String userJson = sharedPreferences.getString("user_details", null);

        if (userJson != null) {
            try {
                JSONObject userObject = new JSONObject(userJson);
                JSONObject typingData = new JSONObject();
                typingData.put("sender_id", userObject.optString("id"));
                typingData.put("receiver_id", receiverId);
                typingData.put("sender_name", userObject.optString("name"));
                typingData.put("image_path", userObject.optString("image_path"));

                mSocket.emit("typing", typingData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void emitStopTyping() {
        try {
            JSONObject typingData = new JSONObject();
            typingData.put("sender_id", senderId);
            typingData.put("receiver_id", receiverId);
            mSocket.emit("stop-typing", typingData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showTypingIndicator(String image_path) {
        Log.d(TAG, "✏️  is image_path: " + image_path);
        if (typingIndicatorView == null) {
            typingIndicatorView = LayoutInflater.from(this).inflate(R.layout.item_typing, messageContainer, false);
            messageContainer.addView(typingIndicatorView);
            scrollToBottom();

            // Get your dot views and animate them
            View dot1 = typingIndicatorView.findViewById(R.id.dot1);
            View dot2 = typingIndicatorView.findViewById(R.id.dot2);
            View dot3 = typingIndicatorView.findViewById(R.id.dot3);


            ImageView avatarImage = typingIndicatorView.findViewById(R.id.avatar_t);
            Glide.with(this)
                    .load(image_path)
                    .placeholder(R.drawable.avatar) // optional fallback image
                    .error(R.drawable.baseline_check_box_outline_blank_24)         // optional error image
                    .into(avatarImage);

            startDotBounceAnimation(dot1, dot2, dot3);
        }
    }

    private void removeTypingIndicator() {
        if (typingIndicatorView != null) {
            messageContainer.removeView(typingIndicatorView);
            typingIndicatorView = null;
        }
    }

    private void addBubble(String text, boolean isSender, String image_path) {
        View bubble = LayoutInflater.from(this)
                .inflate(isSender ? R.layout.item_message_right : R.layout.item_message_left,
                        messageContainer, false);

        ((TextView) bubble.findViewById(R.id.messageText)).setText(text);

        ImageView avatarImage = bubble.findViewById(R.id.avatar_);
        Glide.with(this)
                .load(image_path)
                .placeholder(R.drawable.avatar) // optional fallback image
                .error(R.drawable.baseline_check_box_outline_blank_24)         // optional error image
                .into(avatarImage);

        messageContainer.addView(bubble);
        scrollToBottom();
    }

    private void loadConversation() {
        progressBar.setVisibility(View.VISIBLE); // ⬅️ Show spinner before request starts

        String token = sharedPreferences.getString(KEY_TOKEN, "");
        String url = "http://192.168.1.104/api/v2/message/conversation/" + user_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    progressBar.setVisibility(View.GONE); // ⬅️ Hide spinner after getting response

                    try {
                        org.json.JSONArray jsonArray = new org.json.JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject messageObj = jsonArray.getJSONObject(i);

                            String messageText = messageObj.getString("message");
                            boolean isSender = messageObj.getBoolean("user_message_position");
                            String time = messageObj.getString("created_at_human");
                            String image = "http://192.168.1.104" + messageObj.getString("sender_image");

                            addBubble(messageText, isSender, image);
                        }

                        // Auto-scroll to bottom after loading messages
                        scrollToBottom();

                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE); // ⬅️ Hide spinner on JSON error
                        e.printStackTrace();
                        Toast.makeText(ChatActivity.this, "JSON error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE); // ⬅️ Hide spinner on request failure
                    Log.e("Volley", "Error: " + error.getMessage());
                    Toast.makeText(ChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void scrollToBottom() {
        messageScroll.post(() -> {
            // Smooth scroll to the bottom of the ScrollView
            messageScroll.smoothScrollTo(0, messageContainer.getBottom());
        });
    }

    private void sendMessageToServer(String message) {
        String url = "http://192.168.1.104/api/v2/message/send";
        String token = sharedPreferences.getString(KEY_TOKEN, "");

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Auth token missing", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject res = new JSONObject(response);
                        int status = res.getInt("status");
                        if (status == 200) {
                            JSONObject data = res.getJSONObject("data");
                            String sentMessage = data.getString("message");

                            emitSocketMessage(data);
                            String userJson = sharedPreferences.getString("user_details", null);
                            JSONObject userObject = new JSONObject(userJson);


                            addBubble(sentMessage, true,IMAGE_URL + userObject.getString("image_path"));
                            editTextMessage.setText("");
                        } else {
                            Toast.makeText(this, "Message failed to send.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Volley", "Error: " + error.toString());
                    Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("receiver_id", String.valueOf(receiverId));
                params.put("message", message);
                params.put("reply_to_id", "");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void startTypingTimeout() {
        stopTypingTimeout(); // clear previous
        typingTimeoutRunnable = () -> {
            emitStopTyping();
            runOnUiThread(() -> {
                editTextMessage.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editTextMessage.getWindowToken(), 0);
                }
            });
        };
        typingTimeoutHandler.postDelayed(typingTimeoutRunnable, TYPING_TIMEOUT);
    }

    private void stopTypingTimeout() {
        if (typingTimeoutRunnable != null) {
            typingTimeoutHandler.removeCallbacks(typingTimeoutRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off("receive-message", onReceiveMessage);
            mSocket.off("typing", onTyping);
            mSocket.off("stop-typing", onStopTyping);
        }
    }


}
