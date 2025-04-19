package com.example.fixitnow;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.*;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private static final int CAMERA_REQUEST = 101;

    private ImageView profileImage;
    private Uri imageUri = null;

    EditText etFirstname, etLastname, etEmail, etPhone, etPassword, etConfirmPassword;
    Button registerBtn;

    private final String REGISTER_URL = "http://192.168.1.104/api/register"; // Replace with actual URL

    private static final String PREFS_NAME = "userPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profileImage = findViewById(R.id.profileImage);
        etFirstname = findViewById(R.id.etFirstname);
        etLastname = findViewById(R.id.etLastname);
        etEmail = findViewById(R.id.etEmails);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        registerBtn = findViewById(R.id.registerBtn);

        profileImage.setOnClickListener(v -> openGallery());
        registerBtn.setOnClickListener(v -> registerUser());

        // Request permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
    }

    private void openGallery() {
        Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pick, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }

    private void registerUser() {
        String firstname = etFirstname.getText().toString();
        String lastname = etLastname.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBuilder.addFormDataPart("firstname", firstname);
        multipartBuilder.addFormDataPart("lastname", lastname);
        multipartBuilder.addFormDataPart("email", email);
        multipartBuilder.addFormDataPart("phone", phone);
        multipartBuilder.addFormDataPart("password", password);
        multipartBuilder.addFormDataPart("password_confirmation", confirmPassword);
        multipartBuilder.addFormDataPart("role", "customer");

        if (imageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                byte[] imageBytes = getBytes(inputStream);
                RequestBody requestFile = RequestBody.create(imageBytes, MediaType.parse("image/*"));
                multipartBuilder.addFormDataPart("image_path", "profile.jpg", requestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        RequestBody requestBody = multipartBuilder.build();

        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(requestBody)
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Request Failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = Objects.requireNonNull(response.body()).string();
                runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result);


                        if (response.isSuccessful()) {
                            // Access the token inside the "data" object
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            String token = dataJson.getString("token");
                            JSONObject userJson = dataJson.getJSONObject("user");

                            System.out.println("userJson :::::: " + userJson.toString());

                            // Save the token and user details in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("auth_token", token);
                            editor.putString("user_details", userJson.toString());  // Store user details as JSON string
                            editor.apply();

                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                            // Redirect to MainActivity
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + result, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Invalid response format", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void saveUserData(String token, String userJson) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    private byte[] getBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
