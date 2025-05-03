package com.example.fixitnow.models;

public class MyService {
    private String name;
    private String description;

    public MyService(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}