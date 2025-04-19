package com.example.fixitnow;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.adapters.BookingAdapter;
import com.example.fixitnow.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class TemplateActivity extends AppCompatActivity {

    private RecyclerView bookingRecyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_template);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bookingRecyclerView = findViewById(R.id.bookingRecyclerView);
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data
        bookingList = new ArrayList<>();
        bookingList.add(new Booking("", "AC Repair", "Juan Dela Cruz", "1 hour", "April 20, 2025 - 10:00 AM", "Pending"));
        bookingList.add(new Booking("", "Plumbing", "Pedro Santos", "2 hours", "April 19, 2025 - 1:30 PM", "Ongoing"));
        bookingList.add(new Booking("", "Cleaning", "Maria Lopez", "3 hours", "April 18, 2025 - 9:00 AM", "Done"));

        bookingAdapter = new BookingAdapter(this, bookingList);
        bookingRecyclerView.setAdapter(bookingAdapter);
    }
}