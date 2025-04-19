package com.example.fixitnow.models;

public class ChatUser {
    private String user_id;
    private String name;
    private String messageContent;
    private String messageTime;
    private String profileImageUrl;

    public ChatUser(String name, String messageContent, String messageTime, String profileImageUrl, String user_id) {
        this.user_id = user_id;
        this.name = name;
        this.messageContent = messageContent;
        this.messageTime = messageTime;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
