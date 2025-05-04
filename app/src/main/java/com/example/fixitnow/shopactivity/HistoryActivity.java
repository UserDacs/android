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
import com.example.fixitnow.ui.shop.adapter.HistoryAdapter;
import com.example.fixitnow.ui.shop.models.HistoryModel;

import java.util.ArrayList;
import java.util.List;



public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerViewHistory;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);

        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        List<HistoryModel> historyList = new ArrayList<>();
        historyList.add(new HistoryModel(R.drawable.avatar, "Juan Dela Cruz", "2024-04-18 10:00 AM", "Quezon City", "4.5"));
        historyList.add(new HistoryModel(R.drawable.avatar, "Maria Clara", "2024-04-17 2:30 PM", "Makati", "5.0"));

        HistoryAdapter adapter = new HistoryAdapter(historyList);
        recyclerViewHistory.setAdapter(adapter);
    }
}