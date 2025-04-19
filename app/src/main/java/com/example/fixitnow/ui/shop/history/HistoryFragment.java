package com.example.fixitnow.ui.shop.history;

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

import com.example.fixitnow.R;
import com.example.fixitnow.databinding.FragmentBookingBinding;
import com.example.fixitnow.databinding.FragmentHistoryBinding;
import com.example.fixitnow.ui.shop.adapter.HistoryAdapter;
import com.example.fixitnow.ui.shop.models.HistoryModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        HistoryViewModel serviceViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        List<HistoryModel> historyList = new ArrayList<>();
        historyList.add(new HistoryModel(R.drawable.avatar, "Juan Dela Cruz", "2024-04-18 10:00 AM", "Quezon City", "4.5"));
        historyList.add(new HistoryModel(R.drawable.avatar, "Maria Clara", "2024-04-17 2:30 PM", "Makati", "5.0"));

        HistoryAdapter adapter = new HistoryAdapter(historyList);
        binding.recyclerViewHistory.setAdapter(adapter);

//        final TextView textView = binding.textHistory
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