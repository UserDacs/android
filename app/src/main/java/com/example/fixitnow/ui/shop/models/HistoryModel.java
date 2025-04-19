package com.example.fixitnow.ui.shop.models;

public class HistoryModel {
    private int imageResId;
    private String clientName;
    private String dateTime;
    private String location;
    private String ratings;

    public HistoryModel(int imageResId, String clientName, String dateTime, String location, String ratings) {
        this.imageResId = imageResId;
        this.clientName = clientName;
        this.dateTime = dateTime;
        this.location = location;
        this.ratings = ratings;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getLocation() {
        return location;
    }

    public String getRatings() {
        return ratings;
    }
}
