package com.example.fixitnow.models;

public class Booking {
    private String imageUrl;
    private String name;
    private String technicianName;
    private String duration;
    private String bookingDateTime;
    private String status;

    public Booking(String imageUrl, String name, String technicianName, String duration, String bookingDateTime, String status) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.technicianName = technicianName;
        this.duration = duration;
        this.bookingDateTime = bookingDateTime;
        this.status = status;
    }

    public String getImageUrl() { return imageUrl; }
    public String getName() { return name; }
    public String getTechnicianName() { return technicianName; }
    public String getDuration() { return duration; }
    public String getBookingDateTime() { return bookingDateTime; }
    public String getStatus() { return status; }
}
