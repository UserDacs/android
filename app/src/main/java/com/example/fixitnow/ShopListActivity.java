package com.example.fixitnow;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.adapters.ShopAdapter;
import com.example.fixitnow.models.ServiceListModel;
import com.example.fixitnow.models.ShopModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fixitnow.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class ShopListActivity extends AppCompatActivity {

    RecyclerView recyclerShopList;
    List<ShopModel> shopList = new ArrayList<>();

    List<ServiceListModel> servicesList = new ArrayList<>();
    ShopAdapter shopAdapter;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        recyclerShopList = findViewById(R.id.recyclerShopList);
        recyclerShopList.setLayoutManager(new LinearLayoutManager(this));
        shopAdapter = new ShopAdapter(shopList);
        recyclerShopList.setAdapter(shopAdapter);
        fetchShopData();

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



//        recyclerShopList = findViewById(R.id.recyclerShopList);
//        recyclerShopList.setLayoutManager(new LinearLayoutManager(this));
//
//        // Static data
//        List<String> sampleServices = Arrays.asList("Haircut", "Shave", "Massage","Haircut", "Shave", "Massage","Haircut", "Shave", "Massage","Haircut", "Shave", "Massage");
//
//        shopList.add(new ShopModel("Barber Bros.", "★★★★★ 4.5", "123 Main St", sampleServices));
//        shopList.add(new ShopModel("Clip & Cut", "★★★★☆ 4.2", "456 2nd St", sampleServices));
//        shopList.add(new ShopModel("Salon Supreme", "★★★★★ 4.8", "789 Salon Ave", sampleServices));
//
//        shopAdapter = new ShopAdapter(shopList);
//        recyclerShopList.setAdapter(shopAdapter);
    }

    private void fetchShopData() {
        String url = Constants.getFullApiUrl("/api/v2/shop-list");
        String token = sharedPreferences.getString("auth_token", "");

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        shopList.clear(); // clear old data
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject shop = response.getJSONObject(i);

                            String name = shop.getString("shop_name");
                            String address = shop.getString("shop_address");

                            JSONArray servicesArray = shop.getJSONArray("services");
                            List<String> services = new ArrayList<>();
                            List<String> serviceIds = new ArrayList<>();

                            for (int j = 0; j < servicesArray.length(); j++) {
                                JSONObject service = servicesArray.getJSONObject(j);
                                String nameService = service.optString("service_name", "N/A");
                                String idService = service.optString("id", "0");

                                if (nameService != null && !nameService.equals("null")) {
                                    services.add(nameService);
                                    serviceIds.add(idService);
                                }
                            }
                            String rating = shop.optString("rating", "★★★★★ 4.0");

                            List<ServiceListModel> serviceList1 = new ArrayList<>();
                            for (int j = 0; j < serviceIds.size(); j++) {
                                serviceList1.add(new ServiceListModel(serviceIds.get(j), services.get(j)));
                            }


                            shopList.add(new ShopModel(name, rating, address, serviceList1));
                        }

                        shopAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("Volley", "Error fetching shop list: " + error.getMessage());
                }
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        queue.add(request);
    }
}