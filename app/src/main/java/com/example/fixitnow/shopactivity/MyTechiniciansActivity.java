package com.example.fixitnow.shopactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.ui.shop.adapter.TechnicianAdapter;
import com.example.fixitnow.ui.shop.models.Technician;

import java.util.ArrayList;
import java.util.List;

public class MyTechiniciansActivity extends AppCompatActivity {
    private RecyclerView recyclerViewTechnicians;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_techinicians);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewTechnicians = findViewById(R.id.recyclerViewTechnicians);

        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

        List<Technician> techList = new ArrayList<>();
        techList.add(new Technician(R.drawable.avatar, "Alex Tan", "★★★★☆", "Plumbing"));
        techList.add(new Technician(R.drawable.avatar, "Mara Reyes", "★★★★★", "Hair Styling"));
        techList.add(new Technician(R.drawable.avatar, "Jake Dela Cruz", "★★★☆☆", "AC Technician"));

        TechnicianAdapter adapter = new TechnicianAdapter(techList);
        recyclerViewTechnicians.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTechnicians.setAdapter(adapter);
        recyclerViewTechnicians.setHasFixedSize(true);

    }
}