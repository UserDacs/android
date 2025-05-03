package com.example.fixitnow.utils;

public class Constants {
    public static final String BASE_URL = "http://192.168.1.104";
    public static final String API_PORT = "80";
    public static final String SOCKET_PORT = "3000";

    public static String getFullApiUrl(String endpoint) {
        return BASE_URL + ":" + API_PORT + endpoint;
    }

    public static String getSocketUrl() {
        return BASE_URL + ":" + SOCKET_PORT;
    }
}