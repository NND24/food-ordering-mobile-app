package com.example.food_ordering_mobile_app.ui.customer.account.location;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.ProvinceAdapter;
import com.example.food_ordering_mobile_app.models.location.Province;
import com.example.food_ordering_mobile_app.models.location.ProvinceList;

import java.util.ArrayList;
import java.util.List;

public class ChooseProvinceActivity extends AppCompatActivity {
    private EditText searchEditText;
    private RecyclerView suggestionsRecyclerView;
    private ProvinceAdapter provinceAdapter;
    private List<Province> provinceList;
    private List<Province> filteredProvinceList;
    private TextView tvProvinceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_province);

        searchEditText = findViewById(R.id.searchEditText);
        suggestionsRecyclerView = findViewById(R.id.suggestionsRecyclerView);
        tvProvinceName = findViewById(R.id.tvProvinceName);


        // Dữ liệu mẫu về các tỉnh
        provinceList = ProvinceList.getProvinces();

        filteredProvinceList = new ArrayList<Province>(provinceList);

        String currentProvince = getIntent().getStringExtra("currentProvince"); // Nhận tỉnh đã chọn

        tvProvinceName.setText(currentProvince);

        provinceAdapter = new ProvinceAdapter(filteredProvinceList, new ProvinceAdapter.OnProvinceClickListener() {
            @Override
            public void onProvinceClick(String province) {
                Intent intent = new Intent();
                intent.putExtra("selectedProvince", province);
                setResult(RESULT_OK, intent);
                finish();

            }
        }, currentProvince);

        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        suggestionsRecyclerView.setAdapter(provinceAdapter);

        // Lắng nghe sự kiện tìm kiếm
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                String query = charSequence.toString().trim();
                if (!TextUtils.isEmpty(query)) {
                    filterProvinces(query);
                } else {
                    filteredProvinceList.clear();
                    filteredProvinceList.addAll(provinceList);
                    provinceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });
    }

    private void filterProvinces(String query) {
        filteredProvinceList.clear();
        for (Province province : provinceList) {
            if (province.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProvinceList.add(province);
            }
        }
        provinceAdapter.notifyDataSetChanged();
    }
}