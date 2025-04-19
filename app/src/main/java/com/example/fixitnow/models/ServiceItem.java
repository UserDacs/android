package com.example.fixitnow.models;

public class ServiceItem {
    private int serviceId;
    private String serviceName;
    private String imagePath;

    public ServiceItem(int serviceId, String serviceName, String imagePath) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.imagePath = imagePath;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getImagePath() {
        return imagePath;
    }
}
