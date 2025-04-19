package com.example.fixitnow.ui.shop.models;

public class Booking {
    private int imageResId;
    private String clientName;
    private String dateTime;
    private String location;

    public Booking(int imageResId, String clientName, String dateTime, String location) {
        this.imageResId = imageResId;
        this.clientName = clientName;
        this.dateTime = dateTime;
        this.location = location;
    }

    public int getImageResId() { return imageResId; }
    public String getClientName() { return clientName; }
    public String getDateTime() { return dateTime; }
    public String getLocation() { return location; }
}
