package com.example.fixitnow.models;

public class ServiceModel {

    private int serviceId;

    private String serviceName;
    private String shopName;
    private String technicianName;
    private double rating;

    private String imageUrl;

    public ServiceModel(String serviceName, String shopName, String technicianName, double rating, String imageUrl,int service_id) {

        this.serviceName = serviceName;
        this.shopName = shopName;
        this.technicianName = technicianName;
        this.rating = rating;

        this.imageUrl = imageUrl;

        this.serviceId = service_id;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getShopName() {
        return shopName;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public double getRating() {
        return rating;
    }

    public int getServiceId() {
        return serviceId;
    }

}