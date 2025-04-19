package com.example.fixitnow.ui.shop.technician;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fixitnow.R;
import com.example.fixitnow.databinding.FragmentShopServiceBinding;
import com.example.fixitnow.databinding.FragmentTechnicianBinding;
import com.example.fixitnow.ui.shop.adapter.TechnicianAdapter;
import com.example.fixitnow.ui.shop.models.Technician;

import java.util.ArrayList;
import java.util.List;

public class TechnicianFragment extends Fragment {

    private FragmentTechnicianBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Toast.makeText(getContext(), "Technician Fragment Loaded", Toast.LENGTH_SHORT).show();
        TechnicianViewModel serviceViewModel =
                new ViewModelProvider(this).get(TechnicianViewModel.class);

        binding = FragmentTechnicianBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Technician> techList = new ArrayList<>();
        techList.add(new Technician(R.drawable.avatar, "Alex Tan", "★★★★☆", "Plumbing"));
        techList.add(new Technician(R.drawable.avatar, "Mara Reyes", "★★★★★", "Hair Styling"));
        techList.add(new Technician(R.drawable.avatar, "Jake Dela Cruz", "★★★☆☆", "AC Technician"));

        TechnicianAdapter adapter = new TechnicianAdapter(techList);
        binding.recyclerViewTechnicians.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewTechnicians.setAdapter(adapter);
        binding.recyclerViewTechnicians.setHasFixedSize(true);


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