package com.example.fixitnow;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ChangePasswordActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText etOld = findViewById(R.id.etOldPassword);
        EditText etNew = findViewById(R.id.etNewPassword);
        EditText etConfirm = findViewById(R.id.etConfirmPassword);
        Button btnUpdate = findViewById(R.id.btnUpdatePassword);
        ImageButton btnBack = findViewById(R.id.btn_back);
        sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        btnBack.setOnClickListener(v -> finish());

//        btnUpdate.setOnClickListener(v -> {
//            String oldPass = etOld.getText().toString().trim();
//            String newPass = etNew.getText().toString().trim();
//            String confirmPass = etConfirm.getText().toString().trim();
//
//            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
//                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (!newPass.equals(confirmPass)) {
//                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Do update logic here
//            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
//        });

        btnUpdate.setOnClickListener(v -> {
            String oldPass = etOld.getText().toString().trim();
            String newPass = etNew.getText().toString().trim();
            String confirmPass = etConfirm.getText().toString().trim();

//            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
//                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (!newPass.equals(confirmPass)) {
//                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//                return;
//            }

            new Thread(() -> {
                try {
                    URL url = new URL("http://192.168.1.104/api/v2/change-password");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "Bearer " + getUserToken());
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setDoOutput(true);

                    String postData = "old_password=" + URLEncoder.encode(oldPass, "UTF-8") +
                            "&new_password=" + URLEncoder.encode(newPass, "UTF-8") +
                            "&confirm_new_password=" + URLEncoder.encode(confirmPass, "UTF-8");

                    OutputStream os = conn.getOutputStream();
                    os.write(postData.getBytes());
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    InputStream is = (responseCode >= 400) ? conn.getErrorStream() : conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) result.append(line);
                    reader.close();

                    runOnUiThread(() -> {

                        JSONObject json = null; // `result` is your JSON string
                        String message =  "";
                        try {
                            json = new JSONObject(String.valueOf(result));

                            message = json.getString("message");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if (responseCode == 200) {
                            finish();
                        }

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "Res::: "+ result);

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(this, "Network error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }).start();
        });


    }

    public String getUserToken(){
        return sharedPreferences.getString("auth_token", null);
    }
}