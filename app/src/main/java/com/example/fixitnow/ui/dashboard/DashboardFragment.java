package com.example.fixitnow.ui.dashboard;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.fixitnow.ChangePasswordActivity;
import com.example.fixitnow.ChattingListActivity;
import com.example.fixitnow.LoginActivity;
import com.example.fixitnow.MyAccountActivity;
import com.example.fixitnow.R;
import com.example.fixitnow.adapters.VolleyMultipartRequest;
import com.example.fixitnow.databinding.FragmentDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "userPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private FragmentDashboardBinding binding;

    ImageView profileImage;

    TextView userName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        CardView cardMessenger = binding.idCardMessenger;
        CardView idMyaccount = binding.idMyaccount;
        CardView idChangePass = binding.idChangePass;

        CardView idLogout = binding.idLogout;

        idLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Step 1: I-clear ang SharedPreferences
                                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();

                                // Step 2: Redirect to LoginActivity
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                requireActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });


        userName = binding.userName;
        profileImage = binding.profileImage;
        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1001); // Use any request code
        });



        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String authToken = sharedPreferences.getString(KEY_TOKEN, null);

        String userJson = sharedPreferences.getString("user_details", null);
        try {
            JSONObject userObject = new JSONObject(userJson);
            Log.d(TAG, "✅ Technician: " + userObject);
            String _image_path = "http://192.168.1.104"+userObject.optString("image_path");
//            profileImage.setImageResource(_image_path);
            // Load image using Glide
            Glide.with(requireContext())
                    .load(_image_path)
                    .placeholder(R.drawable.avatar) // Optional: fallback image
                    .error(R.drawable.no_image_1)             // Optional: error image
                    .into(profileImage);


            String firstName = userObject.optString("firstname");
            String lastName = userObject.optString("lastname");
            String name = "";
// Set defaults if empty or null
            if (firstName == null || firstName.trim().isEmpty() && lastName == null || lastName.trim().isEmpty()) {
                name = userObject.optString("name");
            }else {
                name = userObject.optString("firstname")+" "+ userObject.optString("lastname");
            }


            userName.setText(name);



        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        idChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });


        idMyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(intent);
            }
        });


        cardMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChattingListActivity.class);
                startActivity(intent);
            }
        });

//        final TextView textView = binding.userName;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri); // Preview selected image
            uploadProfileImage(imageUri);
        }

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            String updatedName = data.getStringExtra("name");
            if (updatedName != null) {
                // 👇 Update TextView or anything in your Dashboard
                userName.setText(updatedName); // replace with your actual TextView
            }
        }
    }

    private void uploadProfileImage(Uri imageUri) {
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String authToken = sharedPreferences.getString(KEY_TOKEN, null);
        String userJson = sharedPreferences.getString("user_details", null);

        try {
            JSONObject userObject = new JSONObject(userJson);
            String userId = userObject.getString("id");

            InputStream iStream = requireActivity().getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = getBytes(iStream);

            new Thread(() -> {
                try {
                    String boundary = "Boundary-" + System.currentTimeMillis();
                    URL url = new URL("http://192.168.1.104/api/update-profile");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    connection.setRequestProperty("Authorization", "Bearer " + authToken);
                    connection.setRequestProperty("Accept", "application/json");

                    DataOutputStream requestStream = new DataOutputStream(connection.getOutputStream());

                    // user_id part
                    requestStream.writeBytes("--" + boundary + "\r\n");
                    requestStream.writeBytes("Content-Disposition: form-data; name=\"user_id\"\r\n\r\n");
                    requestStream.writeBytes(userId + "\r\n");

                    // image part
                    requestStream.writeBytes("--" + boundary + "\r\n");
                    requestStream.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"user_icon.png\"\r\n");
                    requestStream.writeBytes("Content-Type: image/png\r\n\r\n");
                    requestStream.write(imageBytes);
                    requestStream.writeBytes("\r\n");

                    // End boundary
                    requestStream.writeBytes("--" + boundary + "--\r\n");
                    requestStream.flush();
                    requestStream.close();

                    int responseCode = connection.getResponseCode();
                    InputStream responseStream = (responseCode == HttpURLConnection.HTTP_OK)
                            ? connection.getInputStream()
                            : connection.getErrorStream();

                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = responseStream.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }

                    String responseStr = result.toString("UTF-8");
                    JSONObject jsonResponse = new JSONObject(responseStr);

                    if (jsonResponse.getInt("status") == 200) {
                        JSONObject updatedUser = jsonResponse.getJSONObject("user");

                        requireActivity().runOnUiThread(() -> {
                            sharedPreferences.edit().putString("user_details", updatedUser.toString()).apply();
                            String imageUrl = "http://192.168.1.104" + updatedUser.optString("image_path");
                            Glide.with(requireContext())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.avatar)
                                    .error(R.drawable.no_image_1)
                                    .into(profileImage);
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Image upload error: " + e.getMessage());
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
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