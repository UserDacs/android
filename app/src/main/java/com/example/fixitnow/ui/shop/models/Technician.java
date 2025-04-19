package com.example.fixitnow.ui.shop.models;

public class Technician {
    private int imageResId;
    private String name;
    private String ratings;
    private String expertise;

    public Technician(int imageResId, String name, String ratings, String expertise) {
        this.imageResId = imageResId;
        this.name = name;
        this.ratings = ratings;
        this.expertise = expertise;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getRatings() {
        return ratings;
    }

    public String getExpertise() {
        return expertise;
    }
}
