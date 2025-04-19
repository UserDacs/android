package com.example.fixitnow.models;

public class Notification {
    private String name;
    private String description;
    private String time;
    private int profileImageRes;

    // Constructor, getters, and setters
    public Notification(String name, String description, String time, int profileImageRes) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.profileImageRes = profileImageRes;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public int getProfileImageRes() {
        return profileImageRes;
    }
}
