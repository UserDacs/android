package com.example.fixitnow.models;

import java.util.List;

public class ShopModel {
    public String shopName, rating, location;
    public List<ServiceListModel> services;
    public boolean isExpanded;

    public ShopModel(String shopName, String rating, String location, List<ServiceListModel> services) {
        this.shopName = shopName;
        this.rating = rating;
        this.location = location;
        this.services = services;

        this.isExpanded = false;
    }
}