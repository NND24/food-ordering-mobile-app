package com.example.food_ordering_mobile_app.ui.customer.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SortByActivity extends AppCompatActivity {
    private String name = "";
    private String sort = "";
    private RecyclerView categoryFilterRecyclerView;
    private Set<String> selectedFoodTypes = new HashSet<>();
    private Button btnApply, btnRefresh;
    private RadioGroup radioGroupSort;
    private LinearLayout layoutName, layoutStandout, layoutRating;
    private RadioButton radioName, radioStandout, radioRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sort_by);

        btnApply = findViewById(R.id.btnApply);
        btnRefresh = findViewById(R.id.btnRefresh);
        radioGroupSort = findViewById(R.id.radioGroupSort);
        layoutName = findViewById(R.id.layoutName);
        layoutStandout = findViewById(R.id.layoutStandout);
        layoutRating = findViewById(R.id.layoutRating);
        radioName = findViewById(R.id.radioName);
        radioStandout = findViewById(R.id.radioStandout);
        radioRating = findViewById(R.id.radioRating);

        layoutName.setOnClickListener(v -> radioGroupSort.check(R.id.radioName));
        layoutStandout.setOnClickListener(v -> radioGroupSort.check(R.id.radioStandout));
        layoutRating.setOnClickListener(v -> radioGroupSort.check(R.id.radioRating));

        Intent intent = getIntent();
        if (intent != null) {
            sort = intent.getStringExtra("sort") != null ? intent.getStringExtra("sort") : "";
            name = intent.getStringExtra("search") != null ? intent.getStringExtra("search") : "";
            List<String> selectedCategories = getIntent().getStringArrayListExtra("selected_categories");
            if (selectedCategories != null) {
                selectedFoodTypes.addAll(selectedCategories);
            }
        }

        if (sort.equals("name")) {
            radioGroupSort.check(R.id.radioName);
            radioGroupSort.check(radioName.getId());
        } else if (sort.equals("standout")) {
            radioGroupSort.check(R.id.radioStandout);
            radioGroupSort.check(radioStandout.getId());
        } else if (sort.equals("rating")) {
            radioGroupSort.check(R.id.radioRating);
            radioGroupSort.check(radioRating.getId());
        }

        if (radioGroupSort.getCheckedRadioButtonId() == -1) {
            btnRefresh.setVisibility(View.GONE);
        } else {
            btnRefresh.setVisibility(View.VISIBLE);
        }

        btnApply.setOnClickListener(v -> {
            Intent newIntent = new Intent(this, SearchActivity.class);

            int selectedId = radioGroupSort.getCheckedRadioButtonId();
            if (selectedId == R.id.radioName) {
                sort = "name";
            } else if (selectedId == R.id.radioStandout) {
                sort = "standout";
            } else if (selectedId == R.id.radioRating) {
                sort = "rating";
            } else {
                sort = "";
            }

            newIntent.putExtra("search", name);
            newIntent.putExtra("sort", sort);
            newIntent.putStringArrayListExtra("selected_categories", new ArrayList<>(selectedFoodTypes));
            finish();
            startActivity(newIntent);
        });

        btnRefresh.setOnClickListener(v -> {
            radioGroupSort.clearCheck();
            radioName.setChecked(false);
            radioStandout.setChecked(false);
            radioRating.setChecked(false);
            btnRefresh.setVisibility(View.GONE);
        });
    }

    public void closeFilter(View view) {
        finish();
    }
}