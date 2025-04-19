package com.example.fixitnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fixitnow.models.ChatUser;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChatUserAdapter extends BaseAdapter {

    private Context context;
    private List<ChatUser> userList;

    public ChatUserAdapter(Context context, List<ChatUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.isEmpty() ? 1 : userList.size(); // Show 1 item for "No data"
    }

    @Override
    public Object getItem(int position) {
        return userList.isEmpty() ? null : userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (userList.isEmpty()) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_no_data, parent, false);
            return convertView;
        }

//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false);
//        }

        convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false);


        // Get the current ChatUser object
        ChatUser chatUser = userList.get(position);

        // Initialize the views
        TextView nameTextView = convertView.findViewById(R.id.userName);
        TextView messageTextView = convertView.findViewById(R.id.lastMessage);
        TextView timeTextView = convertView.findViewById(R.id.messageTime);
        ImageView imageView = convertView.findViewById(R.id.userImage);



        // Set the data into the views
        nameTextView.setText(chatUser.getName());
        timeTextView.setText(getRelativeTime(chatUser.getMessageTime()));  // Display relative time

        // Set the message and truncate if necessary
        String message = chatUser.getMessageContent();
        if (message.length() > 30) {
            message = message.substring(0, 30) + "...";  // Truncate to 50 chars and add ellipsis
        }
        messageTextView.setText(message);

        // Set up the messageTextView to handle ellipsis in case the text is longer than one line
        messageTextView.setEllipsize(android.text.TextUtils.TruncateAt.END);
        messageTextView.setMaxLines(1);  // Ensure it's only one line

        // Load the image using Glide
        Glide.with(context)
                .load(chatUser.getProfileImageUrl())  // URL for the profile image
                .into(imageView);

        return convertView;
    }

    // Method to convert timestamp into a relative time
    private String getRelativeTime(String timestamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mma", Locale.getDefault());
            long timeInMillis = dateFormat.parse(timestamp).getTime();
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - timeInMillis;

            long seconds = timeDifference / 1000;
            long minutes = timeDifference / 60000;
            long hours = timeDifference / 3600000;
            long days = timeDifference / 86400000;
            long weeks = days / 7;
            long months = weeks / 5;

            if (seconds < 60) {
                return seconds + (seconds == 1 ? "s" : "s");
            } else if (minutes < 60) {
                return minutes + (minutes == 1 ? "min" : "min");
            } else if (hours < 24) {
                return hours + (hours == 1 ? "h" : "h");
            } else if (days < 7) {
                return days + (days == 1 ? "d" : "d");
            } else if (weeks < 5) {
                return weeks + (weeks == 1 ? "w" : "w");
            } else {
                // Looping months (every 5 weeks = 1 month)
                return months + (months == 1 ? "mon" : "mon");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return timestamp;  // In case of an error, return the original timestamp
        }
    }
}
