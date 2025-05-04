package com.example.fixitnow;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fixitnow.ui.dashboard.DashboardFragment;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.fixitnow.utils.Constants;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyAccountActivity extends AppCompatActivity {
    EditText etFirstName, etLastName, etPhone, etEmail, etStreet, etCity, etState, etZip;
    Button btnUpdate;

    SharedPreferences sharedPreferences;

    String updatedName = "";
    String _userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);

        // Find EditTexts from included layouts
        etFirstName = findViewById(R.id.firstNameLayout).findViewById(R.id.editTextField);
        etFirstName.setHint("First Name");

        etLastName = findViewById(R.id.lastNameLayout).findViewById(R.id.editTextField);
        etLastName.setHint("Last Name");

        etPhone = findViewById(R.id.phoneLayout).findViewById(R.id.editTextField);
        etPhone.setHint("Phone");

        etEmail = findViewById(R.id.emailLayout).findViewById(R.id.editTextField);
        etEmail.setHint("Email");

        etStreet = findViewById(R.id.streetLayout).findViewById(R.id.editTextField);
        etStreet.setHint("Street");

        etCity = findViewById(R.id.cityLayout).findViewById(R.id.editTextField);
        etCity.setHint("City");

        etState = findViewById(R.id.stateLayout).findViewById(R.id.editTextField);
        etState.setHint("State");

        etZip = findViewById(R.id.zipLayout).findViewById(R.id.editTextField);
        etZip.setHint("ZIP Code");




        String userJson = sharedPreferences.getString("user_details", null);
        if (userJson != null) {
            try {
                JSONObject userObject = new JSONObject(userJson);

                _userId = cleanValue(userObject.optString("id", ""));

                etFirstName.setText(cleanValue(userObject.optString("firstname", "")));
                etLastName.setText(cleanValue(userObject.optString("lastname", "")));
                etPhone.setText(cleanValue(userObject.optString("phone", "")));
                etEmail.setText(cleanValue(userObject.optString("email", "")));
                etStreet.setText(cleanValue(userObject.optString("address_street", "")));
                etCity.setText(cleanValue(userObject.optString("address_city", "")));
                etState.setText(cleanValue(userObject.optString("address_state", "")));
                etZip.setText(cleanValue(userObject.optString("address_zip_code", "")));

            } catch (JSONException e) {
                e.printStackTrace(); // Or use Log.e(TAG, "JSON error", e);
            }
        }


//        // Set default or retrieved user data (example)
//        etFirstName.setText("Juan");
//        etLastName.setText("Dela Cruz");
//        etPhone.setText("09123456789");
//        etEmail.setText("juan@example.com");
//        etStreet.setText("123 Street");
//        etCity.setText("Quezon City");
//        etState.setText("NCR");
//        etZip.setText("1100");

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(v -> {


            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            String street = etStreet.getText().toString();
            String city = etCity.getText().toString();
            String state = etState.getText().toString();
            String zip = etZip.getText().toString();

            updatedName = firstName + " " +  lastName;

            updateAccount(
                    _userId , // your actual user_id
                    firstName,
                    lastName,
                    email,
                    phone,
                    street,
                    city,
                    state,
                    zip
            );
        });


        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
          finish();
        });


    }

    private void updateAccount(String userId, String firstName, String lastName, String email,
                               String phone, String street, String city, String state, String zip) {
        String url = Constants.getFullApiUrl("/api/update-account");

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", userId);
            jsonBody.put("firstname", firstName);
            jsonBody.put("lastname", lastName);
            jsonBody.put("email", email);
            jsonBody.put("phone", phone);
            jsonBody.put("address_street", street);
            jsonBody.put("address_city", city);
            jsonBody.put("address_state", state);
            jsonBody.put("address_zip_code", zip);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    Toast.makeText(this, "Account updated successfully!", Toast.LENGTH_SHORT).show();

                    // ✅ Update SharedPreferences
                    String userDetailsString = sharedPreferences.getString("user_details", null);
                    if (userDetailsString != null) {
                        try {
                            JSONObject userDetails = new JSONObject(userDetailsString);

                            userDetails.put("firstname", firstName);
                            userDetails.put("lastname", lastName);
                            userDetails.put("email", email);
                            userDetails.put("phone", phone);
                            userDetails.put("address_street", street);
                            userDetails.put("address_city", city);
                            userDetails.put("address_state", state);
                            userDetails.put("address_zip_code", zip);

                            sharedPreferences.edit()
                                    .putString("user_details", userDetails.toString())
                                    .apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    Toast.makeText(this, "Update failed: " + error.toString(), Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + getUserToken());
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonRequest);
    }


    public String getUserToken(){
        return sharedPreferences.getString("auth_token", null);
    }

    private String cleanValue(String value) {
        return (value == null || value.equalsIgnoreCase("null")) ? "" : value;
    }

}