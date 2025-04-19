package com.example.fixitnow.models;

import java.util.List;

public class CategoryModel {
    private int id;
    private String name;
    private String description;
    private List<ServiceModel> services;

    public boolean isExpanded;

    public CategoryModel(int id, String name, String description, List<ServiceModel> services) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.services = services;

        this.isExpanded = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ServiceModel> getServices() {
        return services;
    }

    public void setServices(List<ServiceModel> services) {
        this.services = services;
    }
}