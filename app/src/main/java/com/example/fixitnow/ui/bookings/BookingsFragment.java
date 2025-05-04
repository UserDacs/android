package com.example.fixitnow.ui.bookings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fixitnow.R;
import com.example.fixitnow.adapters.BookingAdapter;
import com.example.fixitnow.databinding.FragmentBookingsBinding;
import com.example.fixitnow.models.Booking;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.fixitnow.utils.Constants;
import com.google.android.gms.maps.MapsInitializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingsFragment extends Fragment {

    private FragmentBookingsBinding binding;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private SharedPreferences sharedPreferences;

    String _token ;

    String _userJson;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBookingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // ✅ Initialize Google Maps SDK
        MapsInitializer.initialize(requireContext().getApplicationContext());


        sharedPreferences = getActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);

        _token = sharedPreferences.getString("auth_token", "");

        _userJson = sharedPreferences.getString("user_details", "");

        // Initialize list and adapter
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(getContext(), bookingList);

        // Setup RecyclerView
        binding.bookingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.bookingRecyclerView.setAdapter(bookingAdapter); // Attach the empty adapter

        // Fetch bookings from API
        try {
            fetchBookings();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchBookings() throws JSONException {



        Log.d("BookingsFragment", "user_details: " + _userJson);
        Log.d("BookingsFragment", "token: " + _token);
        if (_userJson == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject userObject = new JSONObject(_userJson);


        String url = Constants.getFullApiUrl("/api/v2/bookings-show?customer_id="+userObject.optString("id"));

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    bookingList.clear(); // Clear existing data

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            int id = obj.getInt("service_id");

                            String booking_id = obj.getString("booking_id");
                            String name = obj.getString("service_name");
                            String image = Constants.getFullApiUrl(obj.getString("service_image_path"));

                            String fullname = obj.getString("technician_firstname") + " " + obj.getString("technician_lastname");
                            String booking_duration = obj.getString("booking_duration");

                            String booking_address = obj.getString("booking_address");
                            String booking_date = obj.getString("booking_date");
                            String booking_status = obj.getString("booking_status");

                            String lat = obj.getString("booking_lat");
                            String lon = obj.getString("booking_long");
                            String customer_id = obj.getString("customer_id");

                            String customer_image = obj.getString("customer_image");
                            String cus_full_name = obj.getString("fullname");

                            String shop_user_id = obj.getString("shop_user_id");
                            String shop_user_fullname = obj.getString("shop_user_fullname");
                            String shop_user_image_path = obj.getString("shop_user_image_path");

                            bookingList.add(new Booking(
                                    shop_user_id,
                                    shop_user_fullname,
                                    shop_user_image_path,
                                    cus_full_name,
                                    customer_id,
                                    booking_id,
                                    image,
                                    name,
                                    fullname,
                                    booking_address,
                                    booking_duration,
                                    booking_date,
                                    booking_status,
                                    lat,
                                    lon,
                                    customer_image
                            ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    bookingAdapter.notifyDataSetChanged(); // Notify adapter to refresh
                },
                error -> Toast.makeText(getContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + _token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }
}
