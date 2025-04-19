package com.example.fixitnow;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.Manifest;


import androidx.appcompat.widget.SearchView;

import org.json.JSONException;
import org.json.JSONObject;


public class BookActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView addressText, durationText, selectedDateTime;
    private LatLng pinnedLocation;  // Location where the user pins
    private LatLng shopLocation; // Default shop location (blue pin)
    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;
    private Calendar selectedCalendar;

    private int _serviceId;

    private String authToken;
    private String _user_id;

    ImageView shopImage;
    TextView shopName1, technicianName, price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        authToken = prefs.getString("auth_token", null); // make sure this is correct

        String userJson = prefs.getString("user_details", null);
        try {
            JSONObject userObject = new JSONObject(userJson);
            _user_id = userObject.optString("id");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        _serviceId = getIntent().getIntExtra("service_id", -1);

        addressText = findViewById(R.id.address);
        durationText = findViewById(R.id.duration);
        selectedDateTime = findViewById(R.id.selectedDateTime);

        shopImage = findViewById(R.id.shopImage);
        shopName1 = findViewById(R.id.shopName);
        technicianName = findViewById(R.id.technicianName);
        price = findViewById(R.id.price);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = new SupportMapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mapFragment, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        getCurrentLocation();

        fetchServiceDetails(_serviceId, authToken);

        // Search functionality
        SearchView addressSearchView = findViewById(R.id.addressSearchView);
        addressSearchView.setQueryHint("Search for address");
        addressSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        findViewById(R.id.selectDateTimeBtn).setOnClickListener(v -> showDateTimePicker());

        findViewById(R.id.submitBookingBtn).setOnClickListener(v -> {
            String payment = ((RadioButton) findViewById(
                    ((RadioGroup) findViewById(R.id.paymentMethodGroup)).getCheckedRadioButtonId()))
                    .getText().toString();

            if (selectedCalendar == null || pinnedLocation == null) {
                Toast.makeText(this, "Please select location and time", Toast.LENGTH_SHORT).show();
                return;
            }

            String bookingDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(selectedCalendar.getTime());
            String address = addressText.getText().toString().replace("Address: ", "");
            String duration = durationText.getText().toString().replace("Duration: ", "");

            // Get token from shared prefs


            if (authToken == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://192.168.1.104/api/v2/bookings";

            JSONObject postData = new JSONObject();
            try {
                postData.put("service_id", _serviceId);
                postData.put("customer_id", _user_id);
                postData.put("booking_date", bookingDate);
                postData.put("booking_address", address);
                postData.put("booking_duration", duration);
                postData.put("booking_lat", pinnedLocation.latitude);
                postData.put("booking_long", pinnedLocation.longitude);
                postData.put("booking_status", "pending");
                postData.put("payment_method", payment.toLowerCase());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                    response -> {
                        onBackPressed();
                        Toast.makeText(this, "Booking successful!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "✅ Response: " + response.toString());
                    },
                    error -> {
                        String errorMsg = "Booking failed";
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            errorMsg = new String(error.networkResponse.data);
                        }
                        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "❌ Error: " + errorMsg);
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + authToken);
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            Volley.newRequestQueue(this).add(request);
        });
    }

    private void showDateTimePicker() {
        selectedCalendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, day) -> {
            selectedCalendar.set(Calendar.YEAR, year);
            selectedCalendar.set(Calendar.MONTH, month);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, day);

            TimePickerDialog timePicker = new TimePickerDialog(this, (view1, hour, minute) -> {
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hour);
                selectedCalendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());
                selectedDateTime.setText("Selected: " + sdf.format(selectedCalendar.getTime()));
            }, 9, 0, false);
            timePicker.show();
        }, selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLocation = location;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        1f = world view (sobrang layo)
//
//        10f = city level
//
//        15f = street level (usually best for locations)
//
//        20f = building level (sobrang lapit)

        // Always show shop location (Blue Pin)
        mMap.addMarker(new MarkerOptions().position(shopLocation).title("Shop Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopLocation, 14));

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            // Pin the shop location (Blue Pin)
            mMap.addMarker(new MarkerOptions().position(shopLocation).title("Shop Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            // Pin the user location (Red Pin)
            mMap.addMarker(new MarkerOptions().position(latLng).title("Booking Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
            pinnedLocation = latLng;


            fetchAddressFromLatLng(latLng);
            calculateDuration(latLng);
        });
    }

    private void fetchAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Log.d(TAG, "✏️  is addressList: " + addressList);
            if (!addressList.isEmpty()) {
                addressText.setText("Address: " + addressList.get(0).getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
            addressText.setText("Address: Unable to fetch");
        }
    }

    private void calculateDuration(LatLng destination) {
        if (userLocation == null) {
            durationText.setText("Duration: Unknown\nDistance: Unknown");
            return;
        }

        float[] results = new float[1];

        Location.distanceBetween(
                userLocation.getLatitude(), userLocation.getLongitude(),
                destination.latitude, destination.longitude,
                results
        );



        float distanceInKm = results[0] / 1000;
        float estimatedDuration = distanceInKm / 40 * 60; // Assume 40km/h average
//        Log.d(TAG, "✏️  Full results array: " + Arrays.toString(results));
        durationText.setText("Duration: " + Math.round(estimatedDuration) + " mins");
//        durationText.setText("Duration: ~" + Math.round(estimatedDuration) + " mins\nDistance: " + String.format("%.2f", distanceInKm) + " km");

    }

    private void searchLocation(String query) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(query, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.clear(); // clear any existing markers
                mMap.addMarker(new MarkerOptions().position(location).title("Pinned Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
                addressText.setText("Address: " + address.getAddressLine(0));
                pinnedLocation = location;

                calculateDuration(location);
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchServiceDetails(int serviceId, String authTokens) {
        String url = "http://192.168.1.104/api/v2/service-list?service_id=" + serviceId;
        Log.e(TAG, "❌ url : " + url);
        Log.e(TAG, "❌ authTokens : " + authTokens);

        // Use JsonArrayRequest instead of JsonObjectRequest
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Check if the response is not empty
                        if (response.length() > 0) {
                            // Get the first service from the array
                            JSONObject service = response.getJSONObject(0);

                            // Extract service details
                            String serviceName = service.getString("service_name");
                            String servicePrice = service.getString("price");
                            String shopName = service.getString("shop_name");
                            String imagePath = service.getString("service_image_path");
                            String technicianFullName = service.getString("technician_fullname");

                            // Log the details (for debugging purposes)
                            Log.d(TAG, "✅ Service Name: " + serviceName);
                            Log.d(TAG, "✅ Price: " + servicePrice);
                            Log.d(TAG, "✅ Shop Name: " + shopName);
                            Log.d(TAG, "✅ Image Path: " + imagePath);
                            Log.d(TAG, "✅ Technician: " + technicianFullName);

                            String _path = "http://192.168.1.104"+imagePath;

                            double lt = Double.parseDouble(service.getString("shop_lat"));
                            double lo = Double.parseDouble(service.getString("shop_long"));

//                            9.642009580690843, 123.8563567763376
                            shopLocation = new LatLng(lt, lo);

                            // Update your UI with the extracted values
                            shopName1.setText("Shop: " + shopName);
                            technicianName.setText("Technician: " + technicianFullName);
                            price.setText("Price: ₱" + servicePrice);

                            // Load the image using Glide (or any image loading library)
                            Glide.with(this)
                                    .load(_path)
                                    .placeholder(R.drawable.ic_launcher_background)  // Optional loading placeholder
                                    .error(R.drawable.ic_launcher_foreground)      // Optional error fallback
                                    .into(shopImage);

                        } else {
                            Log.e(TAG, "❌ No service details found.");
                            Toast.makeText(this, "No service details found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "❌ JSON Parsing Error: " + e.getMessage());
                        Toast.makeText(this, "Error parsing service details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String errorMsg = "Fetching service details failed.";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMsg = new String(error.networkResponse.data);
                    }
                    Log.e(TAG, "❌ Error: " + errorMsg);
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authTokens);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }

}
