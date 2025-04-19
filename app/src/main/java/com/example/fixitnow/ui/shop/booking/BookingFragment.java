package com.example.fixitnow.ui.shop.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.ui.shop.adapter.BookingAdapter;
import com.example.fixitnow.databinding.FragmentBookingBinding;
import com.example.fixitnow.databinding.FragmentTechnicianBinding;
import com.example.fixitnow.ui.shop.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private List<Booking> bookingList;
    private FragmentBookingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        BookingViewModel serviceViewModel =
                new ViewModelProvider(this).get(BookingViewModel.class);

        binding = FragmentBookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.recyclerViewBookings.setLayoutManager(new LinearLayoutManager(getContext()));

        bookingList = new ArrayList<>();
        bookingList.add(new Booking(R.drawable.avatar, "John Doe", "2025-04-18 10:00 AM", "Manila"));
        bookingList.add(new Booking(R.drawable.avatar, "Jane Smith", "2025-04-19 2:00 PM", "Cebu"));
        bookingList.add(new Booking(R.drawable.avatar, "Carlos Reyes", "2025-04-20 9:30 AM", "Davao"));

        adapter = new BookingAdapter(bookingList);
        binding.recyclerViewBookings.setAdapter(adapter);

//        final TextView textView = binding.textBooking
//                ;
//        serviceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            toggleSearchBar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleSearchBar() {
        LinearLayout searchBar = getView().findViewById(R.id.idtop);

        if (searchBar.getVisibility() == View.VISIBLE) {
            searchBar.animate()
                    .translationY(-searchBar.getHeight())  // slide pataas
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction(() -> {
                        searchBar.setVisibility(View.GONE);
                        searchBar.setTranslationY(0); // reset
                    })
                    .start();
        } else {
            searchBar.setVisibility(View.VISIBLE);
            searchBar.setAlpha(0f);
            searchBar.setTranslationY(-searchBar.getHeight()); // simula sa taas
            searchBar.animate()
                    .translationY(0)  // slide pababa
                    .alpha(1f)
                    .setDuration(300)
                    .start();
        }
    }
}