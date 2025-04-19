package com.example.fixitnow.shop_pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.adapters.ServiceAdapter;
import com.example.fixitnow.adapters.ShopServiceAdapter;
import com.example.fixitnow.models.ServiceModel;
import com.example.fixitnow.models.ServiceShopModel;

import java.util.ArrayList;
import java.util.List;

public class ServicesShopFragment extends Fragment {

    RecyclerView recyclerView;
    ShopServiceAdapter adapter;
    List<ServiceShopModel> serviceList1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_services, container, false);

        recyclerView = view.findViewById(R.id.recyclerServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        serviceList1 = new ArrayList<>();

        serviceList1.add(new ServiceShopModel("Haircut", "₱150", "John"));
        serviceList1.add(new ServiceShopModel("Shave", "₱100", "Mike"));
        serviceList1.add(new ServiceShopModel("Hair Color", "₱300", "Anne"));

        adapter = new ShopServiceAdapter(serviceList1);
        recyclerView.setAdapter(adapter);

        return view;
    }
}