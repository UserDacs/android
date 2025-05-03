package com.example.fixitnow.shopactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.adapters.MyServiceAdapter;
import com.example.fixitnow.models.MyService;

import java.util.ArrayList;
import java.util.List;

public class MyServicesActivity extends AppCompatActivity {
    private RecyclerView rvServices;
    private MyServiceAdapter myServiceAdapter;
    private ImageButton btnBack;
    private TextView tvTopBarTitle;
    private List<MyService> services; //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_services);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Data
        services = new ArrayList<>();
        services.add(new MyService("Serbisyo 1", "Paglalarawan ng Serbisyo 1."));
        services.add(new MyService("Serbisyo 2", "Paglalarawan ng Serbisyo 2."));
        services.add(new MyService("Serbisyo 3", "Paglalarawan ng Serbisyo 3."));

        // RecyclerView
        rvServices = findViewById(R.id.rvServices);
        rvServices.setLayoutManager(new LinearLayoutManager(this));
        myServiceAdapter = new MyServiceAdapter(services);
        rvServices.setAdapter(myServiceAdapter);
    }
}