package com.example.fixitnow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fixitnow.adapters.CategoryAdapter;
import com.example.fixitnow.models.CategoryModel;
import com.example.fixitnow.models.ServiceModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CategoryModel> categoryList = new ArrayList<>();
    private Map<Integer, List<ServiceModel>> serviceMap = new HashMap<>();
    private CategoryAdapter adapter;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;

    private static final String CATEGORY_URL = "http://192.168.1.104/api/v2/category-list";
    private static final String SERVICE_URL = "http://192.168.1.104/api/v2/service-list";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(this, categoryList);
        recyclerView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);

        fetchCategories();
    }

    private void fetchCategories() {
        String token = sharedPreferences.getString("auth_token", "");

        JsonArrayRequest categoryRequest = new JsonArrayRequest(Request.Method.GET, CATEGORY_URL, null,
                response -> {
                    categoryList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            int id = obj.getInt("id");
                            String name = obj.getString("category_name");
                            String description = obj.getString("description");
                            categoryList.add(new CategoryModel(id, name, description, new ArrayList<>()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    fetchServices();
                },
                error -> Toast.makeText(this, "Failed to load categories", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(categoryRequest);
    }

    private void fetchServices() {
        String token = sharedPreferences.getString("auth_token", "");

        JsonArrayRequest serviceRequest = new JsonArrayRequest(Request.Method.GET, SERVICE_URL, null,
                response -> {
                    serviceMap.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            int serviceId = obj.getInt("service_id");
                            int categoryId = obj.getInt("category_id");
                            String serviceName = obj.getString("service_name");
                            String shopName = obj.getString("shop_name");
                            String technicianName = obj.getString("technician_fullname");
                            double rating = obj.isNull("average_rating") ? 0.0 : obj.getDouble("average_rating");

                            String imageUrl = "http://192.168.1.104"+obj.getString("service_image_path"); // adjust key as needed


                            ServiceModel service = new ServiceModel(serviceName, shopName, technicianName, rating, imageUrl,serviceId);

                            if (!serviceMap.containsKey(categoryId)) {
                                serviceMap.put(categoryId, new ArrayList<>());
                            }
                            serviceMap.get(categoryId).add(service);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // I-link ang mga serbisyo sa kanilang mga kategorya
                    for (CategoryModel category : categoryList) {
                        List<ServiceModel> services = serviceMap.get(category.getId());
                        if (services != null) {
                            category.setServices(services);
                        }
                    }

                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Failed to load services", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(serviceRequest);
    }

//    private void loadData() {
//        // service name, shop name, technician , rating
//        List<ServiceModel> hairServices = Arrays.asList(
//                new ServiceModel("Basic Haircut", "Barber Bros.", "Technicians: 2", "★★★★★ 4.5"),
//                new ServiceModel("Hair Coloring", "Hair Haven", "Technicians: 1", "★★★★☆ 4.2")
//        );
//        // service name, shop name, technician , rating
//        List<ServiceModel> nailServices = Arrays.asList(
//                new ServiceModel("Manicure", "Nail Central", "Technicians: 3", "★★★★★ 5.0"),
//                new ServiceModel("Pedicure", "Nail Central", "Technicians: 2", "★★★★☆ 4.3")
//        );
//         // name , description , list of service
//        categoryList.add(new CategoryModel("Hair Services", "Haircuts and Styling", hairServices));
//        categoryList.add(new CategoryModel("Nail Services", "Manicure and Pedicure", nailServices));
//    }
}