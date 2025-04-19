package com.example.fixitnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class ServicesViewListActivity extends AppCompatActivity {

    AppCompatButton idViewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_services_view_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idViewDetails = findViewById(R.id.idViewDetails);

        idViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesViewListActivity.this, ServiceViewDetailsActivity.class);
                startActivity(intent);
            }
        });

        ImageButton backButton = findViewById(R.id.btn_back); // Assuming you have an ImageButton with this ID
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ServicesViewListActivity.this, ServicesActivity.class);
            startActivity(intent);
        });
    }
}