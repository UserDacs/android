package com.example.fixitnow.ui.shop.models;

public class ShopService {
    private String serviceName;
    private String price;
    private String technician;

    private String ratings;

    public ShopService(String serviceName, String price, String technician, String ratings) {
        this.serviceName = serviceName;
        this.price = price;
        this.technician = technician;
        this.ratings = ratings;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getPrice() {
        return price;
    }

    public String getTechnician() {
        return technician;
    }

    public String getRatings() {
        return ratings;
    }
}
