package com.example.fixitnow.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixitnow.R;
import com.example.fixitnow.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    List<CategoryModel> categoryList;

    public CategoryAdapter(Context context, List<CategoryModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName, txtCategoryDesc;
        ImageView imgDropdown;
        RecyclerView nestedRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            txtCategoryDesc = itemView.findViewById(R.id.txtCategoryDesc);
            imgDropdown = itemView.findViewById(R.id.imgDropdown);
            nestedRecycler = itemView.findViewById(R.id.nestedRecyclerView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_service_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel model = categoryList.get(position);
        holder.txtCategoryName.setText(model.getName());

        String Descri = model.getDescription();
        if (Descri.length() > 20) {
            Descri = Descri.substring(0, 20) + "...";
        }

        holder.txtCategoryDesc.setText(Descri);

        ServiceAdapter adapter = new ServiceAdapter(context,model.getServices());
        holder.nestedRecycler.setLayoutManager(new LinearLayoutManager(context));
        holder.nestedRecycler.setAdapter(adapter);


        holder.nestedRecycler.setVisibility(model.isExpanded ? View.VISIBLE : View.GONE);

        float rotationAngle = model.isExpanded ? 180f : 0f;
        holder.imgDropdown.setRotation(rotationAngle);

        holder.imgDropdown.setOnClickListener(v -> {
            model.isExpanded = !model.isExpanded;

            // Animate the arrow rotation
            float fromRotation = model.isExpanded ? 0f : 180f;
            float toRotation = model.isExpanded ? 180f : 0f;

            ObjectAnimator rotation = ObjectAnimator.ofFloat(holder.imgDropdown, "rotation", fromRotation, toRotation);
            rotation.setDuration(300);
            rotation.start();

            // Refresh only this item
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
