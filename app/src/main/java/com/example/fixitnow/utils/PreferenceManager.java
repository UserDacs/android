package com.example.fixitnow.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class PreferenceManager {
    private static final String PREFS_NAME = "userPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER = "user_details";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save token
    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    // Get token
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Save user (as JSON string)
    public void saveUser(String userJson) {
        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    // Get user JSON string
    public String getUser() {
        return sharedPreferences.getString(KEY_USER, null);
    }

    // Get full JSONObject of user
    public JSONObject getUserObject() {
        String userJson = getUser();
        if (userJson != null) {
            try {
                return new JSONObject(userJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Get specific field as String
    public String getUserField(String fieldName) {
        JSONObject userObj = getUserObject();
        if (userObj != null) {
            return userObj.optString(fieldName, null);
        }
        return null;
    }

    // Get specific field as String with default value
    public String getUserField(String fieldName, String defaultValue) {
        JSONObject userObj = getUserObject();
        if (userObj != null) {
            return userObj.optString(fieldName, defaultValue);
        }
        return defaultValue;
    }

    // Get specific field as int
    public int getUserFieldAsInt(String fieldName, int defaultValue) {
        JSONObject userObj = getUserObject();
        if (userObj != null) {
            return userObj.optInt(fieldName, defaultValue);
        }
        return defaultValue;
    }

    // Get specific field as boolean
    public boolean getUserFieldAsBoolean(String fieldName, boolean defaultValue) {
        JSONObject userObj = getUserObject();
        if (userObj != null) {
            return userObj.optBoolean(fieldName, defaultValue);
        }
        return defaultValue;
    }
    // User Role
    public String getUserRole() {
        String userJson = sharedPreferences.getString(KEY_USER, null);
        if (userJson != null) {
            try {
                JSONObject userObject = new JSONObject(userJson);
                return userObject.optString("role", "customer"); // default to "customer"
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "customer";
    }

    // Clear all saved preferences
    public void clear() {
        editor.clear();
        editor.apply();
    }
}
