package com.example.fixitnow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String API_URL = "http://192.168.1.104/api/login";
    private static final String PREFS_NAME = "userPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER = "user_details";

    private EditText emailEditText, passwordEditText;
    private Button loginBtn;
    private TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        if (token != null) {
            // If token exists, skip login and go directly to MainActivity
            navigateToMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginBtn = findViewById(R.id.loginBtn);
        signUpText = findViewById(R.id.signUpText);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()) {
                    loginUser(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API_URL, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 200) {
                                String token = response.getJSONObject("data").getString("token");
                                JSONObject user = response.getJSONObject("data").getJSONObject("user");

                                // Save the token and user details in SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_TOKEN, token);
                                editor.putString(KEY_USER, user.toString());  // Store user details as a JSON string
                                editor.apply();

                                // Navigate to MainActivity
                                navigateToMainActivity();
                            } else {
                                String message = response.getString("message");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.toString());
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();  // Finish the LoginActivity so the user can't go back to it
    }
}
