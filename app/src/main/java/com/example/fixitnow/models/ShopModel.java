package com.example.fixitnow.models;

import java.util.List;

public class ShopModel {
    public String shopName, rating, location;
    public List<String> services;
    public boolean isExpanded;

    public ShopModel(String shopName, String rating, String location, List<String> services) {
        this.shopName = shopName;
        this.rating = rating;
        this.location = location;
        this.services = services;
        this.isExpanded = false;
    }
}