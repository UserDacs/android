package com.example.fixitnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ServicesActivity extends AppCompatActivity {

    private boolean isDropdownVisible = false; // Track the visibility of the dropdown section
    private LinearLayout bottomSection , sampleClickService;
    private ImageView dropdownIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_services);

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.idServices), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back Button functionality
        ImageButton backButton = findViewById(R.id.btn_back); // Assuming you have an ImageButton with this ID
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ServicesActivity.this, MainActivity.class);
            startActivity(intent);
        });


        // Find views
        bottomSection = findViewById(R.id.bottomSection);
        dropdownIcon = findViewById(R.id.dropdownIcon);

        sampleClickService = findViewById(R.id.sampleClickService);

        // Set onClickListener to toggle dropdown visibility
        dropdownIcon.setOnClickListener(v -> {
            if (isDropdownVisible) {
                // Hide the dropdown section
                bottomSection.setVisibility(View.GONE);
                dropdownIcon.setImageResource(R.drawable.baseline_arrow_drop_down_circle_24); // Show down arrow
            } else {
                // Show the dropdown section
                bottomSection.setVisibility(View.VISIBLE);
                dropdownIcon.setImageResource(R.drawable.baseline_arrow_drop_up_24); // Show up arrow
            }
            isDropdownVisible = !isDropdownVisible; // Toggle the visibility state
        });

        sampleClickService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this, ServicesViewListActivity.class);
                startActivity(intent);
            }
        });
    }
}
