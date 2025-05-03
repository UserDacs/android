package com.example.fixitnow.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.fixitnow.R;
import com.example.fixitnow.ServiceViewDetailsActivity;
import com.example.fixitnow.models.ServiceItem;

import com.example.fixitnow.utils.Constants;

import java.util.List;

public class ServiceTabAdapter {
    private Context context;
    private List<ServiceItem> serviceList;

    public ServiceTabAdapter(Context context, List<ServiceItem> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    public void populateServices(LinearLayout container) {
        LayoutInflater inflater = LayoutInflater.from(context);
        container.removeAllViews();
//        idSer

        for (ServiceItem item : serviceList) {

            View view = inflater.inflate(R.layout.item_service_2, container, false);

            CardView idSer = view.findViewById(R.id.idSer);

            TextView nameText = view.findViewById(R.id.text_service_name);
            ImageView imageView = view.findViewById(R.id.image_service);

            String name = item.getServiceName();
            if (name.length() > 7) {
                name = name.substring(0, 7) + "...";
            }
            nameText.setText(name);

            // Load image (using Glide for best performance)
            String fullImageUrl = Constants.getFullApiUrl(item.getImagePath());
            Glide.with(context).load(fullImageUrl).placeholder(R.drawable.ic_launcher_foreground).into(imageView);

            idSer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ServiceViewDetailsActivity.class);
                    intent.putExtra("service_id", item.getServiceId()); // ← ipapasa ang ID
                    context.startActivity(intent);
                }
            });

            container.addView(view);
        }
    }
}
