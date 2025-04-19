package com.example.fixitnow.builder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fixitnow.R;


public class ReviewBuilder {
    public static View buildReviewView(Context context, String name, String comment, float rating, String imagePath) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_item, null);

        TextView reviewerName = view.findViewById(R.id.reviewerName);
        TextView reviewContent = view.findViewById(R.id.reviewContent);
        ImageView reviewerImage = view.findViewById(R.id.reviewerImage);
        LinearLayout ratingStars = view.findViewById(R.id.ratingStars);

        reviewerName.setText(name);
        reviewContent.setText(comment);
        Glide.with(context).load("http://192.168.1.104" + imagePath).into(reviewerImage);

        ratingStars.removeAllViews();
        int fullStars = (int) rating;
        boolean half = rating - fullStars >= 0.5;

        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(35, 35);
            params.setMargins(2, 0, 2, 0);
            star.setLayoutParams(params);
            if (i < fullStars) {
                star.setImageResource(R.drawable.ic_star_full);
            } else if (i == fullStars && half) {
                star.setImageResource(R.drawable.ic_star_half);
            } else {
                star.setImageResource(R.drawable.ic_star_empty);
            }
            ratingStars.addView(star);
        }

        return view;
    }
}