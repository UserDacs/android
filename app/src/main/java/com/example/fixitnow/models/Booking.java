package com.example.fixitnow.models;

public class Booking {
    private String cusFullname;
    private String booking_id;
    private String customer_id;
    private String imageUrl;
    private String name;
    private String technicianName;
    private String location;
    private String duration;
    private String lat;

    private String lon;

    private String bookingDateTime;
    private String status;

    private String customer_image;

    private String shop_user_id;
    private String shop_user_fullname;
    private String shop_user_image_path;

    public Booking(String shop_user_id , String shop_user_fullname, String shop_user_image_path,String cusFullname, String customer_id,String booking_id ,String imageUrl, String name, String technicianName,String location, String duration, String bookingDateTime, String status, String lat, String lon, String customer_image) {
        this.shop_user_id = shop_user_id;
        this.shop_user_fullname = shop_user_fullname;
        this.shop_user_image_path = shop_user_image_path;

        this.cusFullname = cusFullname;
        this.customer_id = customer_id;
        this.booking_id = booking_id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.technicianName = technicianName;
        this.location = location;
        this.duration = duration;
        this.bookingDateTime = bookingDateTime;
        this.status = status;
        this.lat = lat;
        this.lon = lon;
        this.customer_image = customer_image;
    }

    public String getShop_user_fullname() {
        return shop_user_fullname;
    }

    public String getShop_user_id() {
        return shop_user_id;
    }

    public String getShop_user_image_path() {
        return shop_user_image_path;
    }

    public String getCusFullname() {
        return cusFullname;
    }

    public String getCustomer_image() {
        return customer_image;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public String getLocation() {
        return location;
    }

    public String getImageUrl() { return imageUrl; }
    public String getName() { return name; }
    public String getTechnicianName() { return technicianName; }
    public String getDuration() { return duration; }
    public String getBookingDateTime() { return bookingDateTime; }
    public String getStatus() { return status; }


    public void setStatus(String status) {
        this.status = status;
    }
}
