package com.example.fixitnow.ui.shop.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.fixitnow.ui.shop.adapter.ShopServiceAdapter;
import com.example.fixitnow.databinding.FragmentGalleryBinding;
import com.example.fixitnow.databinding.FragmentShopServiceBinding;
import com.example.fixitnow.ui.shop.models.ShopService;

import java.util.ArrayList;
import java.util.List;


public class ServiceFragment extends Fragment {

    private FragmentShopServiceBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);


        ServiceViewModel serviceViewModel =
                new ViewModelProvider(this).get(ServiceViewModel.class);

        binding = FragmentShopServiceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textShopFrag;
//        serviceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        List<ShopService> staticData = new ArrayList<>();
        staticData.add(new ShopService("Haircut", "150", "John","★★★★★ 4.5"));
        staticData.add(new ShopService("Shave", "80", "Paul","★★★★★ 4.5"));
        staticData.add(new ShopService("Massage", "300", "Anne","★★★★★ 4.5"));
        staticData.add(new ShopService("Haircut", "150", "John","★★★★★ 4.5"));
        staticData.add(new ShopService("Shave", "80", "Paul","★★★★★ 4.5"));
        staticData.add(new ShopService("Massage", "300", "Anne","★★★★★ 4.5"));
        staticData.add(new ShopService("Haircut", "150", "John","★★★★★ 4.5"));
        staticData.add(new ShopService("Shave", "80", "Paul","★★★★★ 4.5"));
        staticData.add(new ShopService("Massage", "300", "Anne","★★★★★ 4.5"));
        staticData.add(new ShopService("Haircut", "150", "John","★★★★★ 4.5"));
        staticData.add(new ShopService("Shave", "80", "Paul","★★★★★ 4.5"));
        staticData.add(new ShopService("Massage", "300", "Anne","★★★★★ 4.5"));

        ShopServiceAdapter adapter = new ShopServiceAdapter(staticData);

        binding.recyclerViewServices.setLayoutManager(new LinearLayoutManager(getContext())); // important!
        binding.recyclerViewServices.setAdapter(adapter);
        binding.recyclerViewServices.setHasFixedSize(true);

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