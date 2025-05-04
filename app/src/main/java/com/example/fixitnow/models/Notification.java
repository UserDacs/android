package com.example.fixitnow.models;

public class Notification {
    private String name;
    private String description;
    private String time;
    private String imageUrl; // <-- URL instead of int drawable

    public Notification(String name, String description, String time, String imageUrl) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getTime() { return time; }
    public String getImageUrl() { return imageUrl; }
}
