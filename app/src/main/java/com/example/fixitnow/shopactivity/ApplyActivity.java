package com.example.fixitnow.shopactivity;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import com.example.fixitnow.R;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.fixitnow.adapters.VolleyMultipartRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import android.content.SharedPreferences;

public class ApplyActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PICK_IMAGE_SHOP = 1;
    private static final int PICK_IMAGE_ID = 2;
    private static final int PICK_IMAGE_COR = 3;

    private EditText shopName, shopAddress, shopDetails;
    Button btnChooseImage, btnChooseID, btnChooseCOR, btnSubmit;
    private ImageView imgShopImage, imgID, imgCOR;

    private Uri shopImageUri = null, idImageUri = null, corImageUri = null;
    Bitmap bitmapShop, bitmapID, bitmapCOR;
    SharedPreferences sharedPreferences;
    String authToken_user = ""; // Replace with your actual token
    String _user_id = "";
    private static final String PREFS_NAME = "userPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER = "user_details";

    private GoogleMap mMap;
    private LatLng selectedLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_apply);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        shopDetails = findViewById(R.id.shopDetails);

        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnChooseID = findViewById(R.id.btnChooseID);
        btnChooseCOR = findViewById(R.id.btnChooseCOR);
        btnSubmit = findViewById(R.id.btnSubmit);

        imgShopImage = findViewById(R.id.imgShopImage);
        imgID = findViewById(R.id.imgID);
        imgCOR = findViewById(R.id.imgCOR);

        btnChooseImage.setOnClickListener(v -> pickImage(PICK_IMAGE_SHOP));
        btnChooseID.setOnClickListener(v -> pickImage(PICK_IMAGE_ID));
        btnChooseCOR.setOnClickListener(v -> pickImage(PICK_IMAGE_COR));
        btnSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                uploadShopData();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng defaultLocation = new LatLng(10.0381, 124.3515);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));

        mMap.setOnMapClickListener(latLng -> {
            if (selectedLocation != null) {
                mMap.clear();
            }

            selectedLocation = latLng;
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
        });
    }

    private void pickImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            Bitmap selectedBitmap = null;

            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (requestCode == PICK_IMAGE_SHOP) {
                shopImageUri = selectedImageUri;
                bitmapShop = selectedBitmap;
                imgShopImage.setImageURI(shopImageUri);
                imgShopImage.setVisibility(View.VISIBLE);
            } else if (requestCode == PICK_IMAGE_ID) {
                idImageUri = selectedImageUri;
                bitmapID = selectedBitmap;
                imgID.setImageURI(idImageUri);
                imgID.setVisibility(View.VISIBLE);
            } else if (requestCode == PICK_IMAGE_COR) {
                corImageUri = selectedImageUri;
                bitmapCOR = selectedBitmap;
                imgCOR.setImageURI(corImageUri);
                imgCOR.setVisibility(View.VISIBLE);
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] imageBytes = stream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void uploadShopData() {
        if (bitmapShop == null || bitmapID == null || bitmapCOR == null) {
            Toast.makeText(this, "Please select all required images", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedLocation == null) {
            Toast.makeText(this, "Please select a location on the map.", Toast.LENGTH_SHORT).show();
            return;
        }

        double lat = selectedLocation.latitude;
        double lng = selectedLocation.longitude;

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String authToken = sharedPreferences.getString(KEY_TOKEN, null);
        String userDetailsJson = sharedPreferences.getString(KEY_USER, null);

        if (authToken == null || userDetailsJson == null) {
            Toast.makeText(this, "No user info found!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject userObject = new JSONObject(userDetailsJson);
            int userId = userObject.getInt("id");

            new Thread(() -> {
                String boundary = "===" + System.currentTimeMillis() + "===";
                String LINE_FEED = "\r\n";
                String charset = "UTF-8";
                String requestURL = "http://192.168.1.104/api/v2/shop-create";

                try {
                    URL url = new URL(requestURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setUseCaches(false);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    conn.setRequestProperty("Authorization", "Bearer " + authToken);

                    DataOutputStream request = new DataOutputStream(conn.getOutputStream());

                    // Add form fields
                    writeFormField(request, "user_id", String.valueOf(userId), boundary);
                    writeFormField(request, "shop_name", shopName.getText().toString(), boundary);
                    writeFormField(request, "shop_address", shopAddress.getText().toString(), boundary);
                    writeFormField(request, "shop_details", shopDetails.getText().toString(), boundary);
                    writeFormField(request, "shop_lat", String.valueOf(lat), boundary);
                    writeFormField(request, "shop_long", String.valueOf(lng), boundary);

                    // Add images
                    writeFileField(request, "shop_image", "shop.jpg", bitmapShop, boundary);
                    writeFileField(request, "national_id", "id.jpg", bitmapID, boundary);
                    writeFileField(request, "cor", "cor.jpg", bitmapCOR, boundary);

                    // End of multipart
                    request.writeBytes("--" + boundary + "--" + LINE_FEED);
                    request.flush();
                    request.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        runOnUiThread(() -> {


                            AlertDialog.Builder builder = new AlertDialog.Builder(ApplyActivity.this);
                            builder.setTitle("Success");
                            builder.setMessage("Upload successful!");
                            builder.setCancelable(false); // Hindi pwedeng i-dismiss manually

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            // Isara ang dialog at activity pagkatapos ng 2 segundo
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                dialog.dismiss();
                                finish();
                            }, 2000);
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Upload Failed: " + responseCode, Toast.LENGTH_SHORT).show());
                        Log.e(TAG, "Upload Error: HTTP " + responseCode);
                    }

                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Upload Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

            }).start();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid user data", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeFormField(DataOutputStream out, String name, String value, String boundary) throws Exception {
        String LINE_FEED = "\r\n";
        out.writeBytes("--" + boundary + LINE_FEED);
        out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED);
        out.writeBytes("Content-Type: text/plain; charset=UTF-8" + LINE_FEED);
        out.writeBytes(LINE_FEED);
        out.writeBytes(value + LINE_FEED);
    }

    private void writeFileField(DataOutputStream out, String fieldName, String fileName, Bitmap bitmap, String boundary) throws Exception {
        String LINE_FEED = "\r\n";
        out.writeBytes("--" + boundary + LINE_FEED);
        out.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"" + LINE_FEED);
        out.writeBytes("Content-Type: image/jpeg" + LINE_FEED);
        out.writeBytes(LINE_FEED);
        out.write(getFileDataFromBitmap(bitmap));
        out.writeBytes(LINE_FEED);
    }


    private boolean validateInputs() {
        if (shopName.getText().toString().trim().isEmpty()) {
            shopName.setError("Required");
            shopName.requestFocus();
            return false;
        }
        if (shopAddress.getText().toString().trim().isEmpty()) {
            shopAddress.setError("Required");
            shopAddress.requestFocus();
            return false;
        }
        if (shopDetails.getText().toString().trim().isEmpty()) {
            shopDetails.setError("Required");
            shopDetails.requestFocus();
            return false;
        }

        if (shopImageUri == null) {
            Toast.makeText(this, "Please choose a shop image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (idImageUri == null) {
            Toast.makeText(this, "Please choose a national ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (corImageUri == null) {
            Toast.makeText(this, "Please choose a COR", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private byte[] getFileDataFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
