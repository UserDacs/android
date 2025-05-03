package com.example.fixitnow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.fixitnow.BookActivity;
import com.example.fixitnow.R;
import com.example.fixitnow.builder.ReviewBuilder;
import com.example.fixitnow.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServiceViewDetailsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "userPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER = "user_details";

    TextView txtTotalRev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_view_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_TOKEN, null);

        int serviceId = getIntent().getIntExtra("service_id", -1);

        ImageButton backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> onBackPressed());
        txtTotalRev = findViewById(R.id.txtTotalRev);
        Button btn_book_now = findViewById(R.id.btn_book_now);
        btn_book_now.setOnClickListener(v -> {
            Intent intent = new Intent(ServiceViewDetailsActivity.this, BookActivity.class);
            intent.putExtra("service_id", serviceId); // ← ipapasa ang ID
            startActivity(intent);
        });



        fetchServiceDetails(serviceId, token);
        fetchServiceReviews(serviceId, token);
    }

    private void fetchServiceDetails(int serviceId, String token) {
        String url = Constants.getFullApiUrl("/api/v2/service-list?service_id=" + serviceId);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject service = response.getJSONObject(0);

                        // Populate TextViews and other UI elements with service details
                        String name = service.getString("service_name");
                        String description = service.getString("service_description");
                        String price = service.getString("price");
                        String imagePath = service.getString("service_image_path");
                        String imageUrl = Constants.getFullApiUrl(imagePath);

                        TextView titleView = findViewById(R.id.textViewServiceTitle);
                        TextView descView = findViewById(R.id.textViewServiceDesc);
                        TextView priceView = findViewById(R.id.textViewServicePrice);
                        TextView shopName = findViewById(R.id.textViewShopName);
                        TextView techni = findViewById(R.id.textViewTechnician);
                        TextView txtUpTitle = findViewById(R.id.txtUpTitle);




                        txtUpTitle.setTypeface(null, Typeface.BOLD);
                        txtUpTitle.setText(name);

                        titleView.setText(name);
                        descView.setText(description);
                        priceView.setText("₱" + price);
                        shopName.setText("Shop: " + service.getString("shop_name"));
                        techni.setText("Technician: " + service.getString("technician_fullname"));

                        // Handle rating and star UI
                        float averageRating = 0.0f;
                        if (!service.isNull("average_rating")) {
                            averageRating = (float) service.optDouble("average_rating", 0.0);
                        }

                        LinearLayout starContainer = findViewById(R.id.star_container1);
                        starContainer.removeAllViews();

                        for (int i = 1; i <= 5; i++) {
                            ImageView star = new ImageView(this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(16), dpToPx(16));
                            params.setMargins(2, 0, 2, 0);
                            star.setLayoutParams(params);

                            if (i <= averageRating) {
                                star.setImageResource(R.drawable.ic_star_full);
                            } else if (i - averageRating < 1) {
                                star.setImageResource(R.drawable.ic_star_half);
                            } else {
                                star.setImageResource(R.drawable.ic_star_empty);
                            }

                            starContainer.addView(star);
                        }

                        // Load service image
                        ImageView productImage = findViewById(R.id.imageProduct);
                        Glide.with(this).load(imageUrl).into(productImage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    private void fetchServiceReviews(int serviceId, String token) {
        String url = Constants.getFullApiUrl("/api/v2/comment-list?service_id="  + serviceId);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        LinearLayout reviewsContainer = findViewById(R.id.reviewsContainer);
                        reviewsContainer.removeAllViews();

                        txtTotalRev.setText("Comment (" + response.length() + ")");

                        if (response.length() == 0) {
                            // Add "No reviews" message
                            TextView noReviewsText = new TextView(this);
                            noReviewsText.setText("No reviews");
                            noReviewsText.setTextSize(16);
                            noReviewsText.setTextColor(Color.GRAY);
                            noReviewsText.setTypeface(null, Typeface.ITALIC); // italic style
                            noReviewsText.setGravity(Gravity.CENTER); // full center
                            noReviewsText.setPadding(0, 20, 0, 20);
                            reviewsContainer.addView(noReviewsText);
                            return;
                        }

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject review = response.getJSONObject(i);

                            String name = review.getString("name");
                            String comment = review.getString("comment");
                            float rating = (float) review.optDouble("rating", 0);
                            String imagePath = review.getString("image_path");

                            View reviewView = ReviewBuilder.buildReviewView(this, name, comment, rating, imagePath);
                            reviewsContainer.addView(reviewView);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }


    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
