package com.example.fixitnow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        etLastName = findViewById(R.id.lastNameLayout).findViewById(R.id.editTextField);
        etPhone = findViewById(R.id.phoneLayout).findViewById(R.id.editTextField);
        etEmail = findViewById(R.id.emailLayout).findViewById(R.id.editTextField);
        etStreet = findViewById(R.id.streetLayout).findViewById(R.id.editTextField);
        etCity = findViewById(R.id.cityLayout).findViewById(R.id.editTextField);
        etState = findViewById(R.id.stateLayout).findViewById(R.id.editTextField);
        etZip = findViewById(R.id.zipLayout).findViewById(R.id.editTextField);



        String userJson = sharedPreferences.getString("user_details", null);
        try {
            JSONObject userObject = new JSONObject(userJson);


            _userId = userObject.getString("id");

            etFirstName.setText(userObject.getString("firstname"));
            etLastName.setText(userObject.getString("lastname"));
            etPhone.setText(userObject.getString("phone"));
            etEmail.setText(userObject.getString("email"));
            etStreet.setText(userObject.getString("address_street"));
            etCity.setText(userObject.getString("address_city"));
            etState.setText(userObject.getString("address_state"));
            etZip.setText(userObject.getString("address_zip_code"));



        } catch (JSONException e) {
            throw new RuntimeException(e);
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
        String url = "http://192.168.1.104/api/update-account";

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



}