package com.example.fixitnow.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fixitnow.ChatActivity;
import com.example.fixitnow.ChattingListActivity;
import com.example.fixitnow.R;
import com.example.fixitnow.ServiceListActivity;
import com.example.fixitnow.ServicesActivity;
import com.example.fixitnow.ServicesViewListActivity;
import com.example.fixitnow.ShopListActivity;
import com.example.fixitnow.adapters.ServiceTabAdapter;
import com.example.fixitnow.databinding.FragmentHomeBinding;
import com.example.fixitnow.models.ServiceItem;

import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private LinearLayout serviceContainer;
    private List<ServiceItem> serviceList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        HorizontalScrollView horizontalScrollView = root.findViewById(R.id.horizontalScrollView);
        LinearLayout serviceContainer = root.findViewById(R.id.service_container);
        TextView textHome = root.findViewById(R.id.text_home);
        View idShopSeeMore = root.findViewById(R.id.idShopSeeMore);
        View tvSeeMore = root.findViewById(R.id.tvSeeMore);

        this.serviceContainer = serviceContainer;

        if (horizontalScrollView != null) {
            horizontalScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                View view = horizontalScrollView.getChildAt(horizontalScrollView.getChildCount() - 1);
                int diff = (view.getRight() - (horizontalScrollView.getScrollX() + horizontalScrollView.getWidth()));

                if (diff == 0) {
                    fetchServices();
                }
            });
        }

        sharedPreferences = getActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        fetchServices();

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getText().observe(getViewLifecycleOwner(), textHome::setText);

        idShopSeeMore.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ShopListActivity.class);
            startActivity(intent);
        });

        tvSeeMore.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ServiceListActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void fetchServices() {
        String token = sharedPreferences.getString("auth_token", "");
        String url = "http://192.168.1.104/api/v2/service-list";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    serviceList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            int id = obj.getInt("service_id");
                            String name = obj.getString("service_name");
                            String image = obj.getString("service_image_path");

                            serviceList.add(new ServiceItem(id, name, image));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ServiceTabAdapter adapter = new ServiceTabAdapter(getContext(), serviceList);
                    adapter.populateServices(serviceContainer);
                },
                error -> {
                    Toast.makeText(getContext(), "Failed to load services", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }


}