package com.example.fixitnow;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.adapters.ShopAdapter;
import com.example.fixitnow.models.ShopModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopListActivity extends AppCompatActivity {

    RecyclerView recyclerShopList;
    List<ShopModel> shopList = new ArrayList<>();
    ShopAdapter shopAdapter;

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


        recyclerShopList = findViewById(R.id.recyclerShopList);
        recyclerShopList.setLayoutManager(new LinearLayoutManager(this));

        // Static data
        List<String> sampleServices = Arrays.asList("Haircut", "Shave", "Massage","Haircut", "Shave", "Massage","Haircut", "Shave", "Massage","Haircut", "Shave", "Massage");

        shopList.add(new ShopModel("Barber Bros.", "★★★★★ 4.5", "123 Main St", sampleServices));
        shopList.add(new ShopModel("Clip & Cut", "★★★★☆ 4.2", "456 2nd St", sampleServices));
        shopList.add(new ShopModel("Salon Supreme", "★★★★★ 4.8", "789 Salon Ave", sampleServices));

        shopAdapter = new ShopAdapter(shopList);
        recyclerShopList.setAdapter(shopAdapter);
    }
}