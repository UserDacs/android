package com.example.fixitnow.models;

public class ChatUserList {
    private String name;
    private String lastMessage;
    private String time;
    private int profileImageResId;

    public ChatUserList(String name, String lastMessage, String time, int profileImageResId) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.profileImageResId = profileImageResId;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }
}
