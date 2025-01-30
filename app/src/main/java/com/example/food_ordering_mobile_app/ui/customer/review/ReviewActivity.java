package com.example.food_ordering_mobile_app.ui.customer.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.ReviewAdapter;
import com.example.food_ordering_mobile_app.adapters.ReviewShortAdapter;
import com.example.food_ordering_mobile_app.models.Review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {
    private TextView tvAverageRating, tvTotalReviews;
    private LinearLayout ratingList;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);

        tvAverageRating = findViewById(R.id.tv_average_rating);
        tvTotalReviews = findViewById(R.id.tv_total_reviews);
        ratingList = findViewById(R.id.rating_list);

        // Dữ liệu giả lập số lượng đánh giá
        Map<Integer, Integer> ratings = new HashMap<>();
        ratings.put(5, 5);
        ratings.put(4, 30);
        ratings.put(3, 10);
        ratings.put(2, 5);
        ratings.put(1, 2);

        // Gọi phương thức để hiển thị danh sách đánh giá
        setRatings(ratings);

        // Show review
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviewList = new ArrayList<>();
        reviewList.add(new Review("Dat Nguyen", 21, String.valueOf(R.drawable.res_1), 4.9, "Sợi mỳ dai ngon", "6", "Cam on ban da danh gia", "8", "1 to pho", String.valueOf(R.drawable.res_1)));
        reviewList.add(new Review("Dat Nguyen", 21, String.valueOf(R.drawable.res_1), 3.0, "Sợi mỳ dai ngon", "6", "Cam on ban da danh gia", "8", "1 to pho", String.valueOf(R.drawable.res_1)));

        reviewAdapter = new ReviewAdapter(this, reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);
    }

    public void setRatings(Map<Integer, Integer> ratings) {
        int totalRatings = 0;
        int totalScore = 0;

        for (Map.Entry<Integer, Integer> entry : ratings.entrySet()) {
            totalRatings += entry.getValue();
            totalScore += entry.getKey() * entry.getValue();
        }

        float averageRating = totalRatings > 0 ? (float) totalScore / totalRatings : 0;
        tvAverageRating.setText(String.format("%.1f", averageRating));
        tvTotalReviews.setText(String.valueOf(totalRatings));

        ratingList.removeAllViews();

        for (int i = 5; i >= 1; i--) {
            int count = ratings.getOrDefault(i, 0);
            int percentage = totalRatings > 0 ? (int) Math.round((count * 100.0) / totalRatings) : 0;

            View ratingRow = LayoutInflater.from(this).inflate(R.layout.item_rating, ratingList, false);
            TextView tvStar = ratingRow.findViewById(R.id.tv_star);
            ProgressBar progressBar = ratingRow.findViewById(R.id.progress_bar);

            tvStar.setText(i + "★");
            progressBar.setMax(100);
            progressBar.setProgress(percentage);

            ratingList.addView(ratingRow);
        }
    }

    public void goBack(View view) {
        onBackPressed();
    }
}