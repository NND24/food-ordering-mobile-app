package com.example.food_ordering_mobile_app.ui.customer.rating;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.RatingAdapter;
import com.example.food_ordering_mobile_app.models.dish.Rating;
import com.example.food_ordering_mobile_app.viewmodels.RatingViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RatingViewModel ratingViewModel;
    private TextView tvAverageRating, tvTotalReviews;
    private RecyclerView reviewRecyclerView;
    private RatingAdapter ratingAdapter;
    private List<Rating> ratingList;
    private LinearLayout layoutRatingBar;
    private String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rating);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tvAverageRating = findViewById(R.id.tv_average_rating);
        tvTotalReviews = findViewById(R.id.tv_total_reviews);
        layoutRatingBar = findViewById(R.id.layoutRatingBar);

        Intent intent = getIntent();
        if (intent != null) {
            storeId = intent.getStringExtra("storeId") != null ? intent.getStringExtra("storeId") : "";
        }

        ratingViewModel = new ViewModelProvider(this).get(RatingViewModel.class);

        setupRating();
    }

    private void setupRating() {
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ratingList = new ArrayList<>();

        ratingAdapter = new RatingAdapter(this, ratingList);
        reviewRecyclerView.setAdapter(ratingAdapter);

        ratingViewModel.getAllStoreRatingResponse().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    swipeRefreshLayout.setRefreshing(true);
                    break;
                case SUCCESS:
                    swipeRefreshLayout.setRefreshing(false);
                    ratingList.clear();
                    ratingList.addAll(resource.getData().getData());

                    Map<Integer, Integer> ratings = countRatings(ratingList);
                    setRatings(ratings);

                    ratingAdapter.notifyDataSetChanged();
                    break;
                case ERROR:
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        });

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("sort", "");
        queryParams.put("limit", "3");
        queryParams.put("page", "1");

        ratingViewModel.getAllStoreRating(storeId, queryParams);
    }

    public Map<Integer, Integer> countRatings(List<Rating> reviews) {
        Map<Integer, Integer> ratings = new HashMap<>();

        // Khởi tạo giá trị mặc định cho 5 mức rating
        for (int i = 1; i <= 5; i++) {
            ratings.put(i, 0);
        }

        // Duyệt qua danh sách đánh giá và đếm số lượng ratingValue
        for (Rating review : reviews) {
            int rating = review.getRatingValue();
            ratings.put(rating, ratings.getOrDefault(rating, 0) + 1);
        }

        return ratings;
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

        layoutRatingBar.removeAllViews();

        for (int i = 5; i >= 1; i--) {
            int count = ratings.getOrDefault(i, 0);
            int percentage = totalRatings > 0 ? (int) Math.round((count * 100.0) / totalRatings) : 0;

            View ratingRow = LayoutInflater.from(this).inflate(R.layout.item_rating_bar, layoutRatingBar, false);
            TextView tvStar = ratingRow.findViewById(R.id.tv_star);
            ProgressBar progressBar = ratingRow.findViewById(R.id.progress_bar);

            tvStar.setText(i + "★");
            progressBar.setMax(100);
            progressBar.setProgress(percentage);

            layoutRatingBar.addView(ratingRow);
        }
    }

    public void goBack(View view) {
        onBackPressed();
    }
}