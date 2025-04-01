package com.example.food_ordering_mobile_app.ui.customer.account.location;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.SuggestionsAdapter;
import com.example.food_ordering_mobile_app.models.location.Province;
import com.example.food_ordering_mobile_app.models.location.ProvinceList;
import com.example.food_ordering_mobile_app.models.location.SuggestionItem;
import com.example.food_ordering_mobile_app.utils.Functions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.geojson.Point;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class ChooseLocationActivity extends AppCompatActivity {
    private MapView mapView;
    private EditText searchEditText;
    private RecyclerView suggestionsRecyclerView;
    private SuggestionsAdapter suggestionsAdapter;
    private LinearLayout btnChooseProvince;
    private TextView tvProvinceName;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1Ijoibm5kMjQiLCJhIjoiY204eTluZzl6MDhtcjJqc2FsMzN3Z3ozaCJ9.wigbyhwGl22d2pdzdntD_g"; // Replace with your token
    private Province province; // Declare the province variable
    private PointAnnotationManager pointAnnotationManager;
    private PointAnnotation currentLocationMarker;
    private PointAnnotation draggableMarker;
    private boolean isSelectingSuggestion = false;
    private static final int REQUEST_CODE_CHOOSE_PROVINCE = 1001;
    private static final int REQUEST_CODE_PROVINCE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_choose);

        mapView = findViewById(R.id.mapView);
        searchEditText = findViewById(R.id.searchEditText);
        suggestionsRecyclerView = findViewById(R.id.suggestionsRecyclerView);
        btnChooseProvince = findViewById(R.id.btnChooseProvince);
        tvProvinceName = findViewById(R.id.tvProvinceName);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (checkLocationPermission()) {
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }

        btnChooseProvince.setOnClickListener(v -> {
            if (province != null) {
                Intent intent = new Intent(ChooseLocationActivity.this, ChooseProvinceActivity.class);
                intent.putExtra("currentProvince", province.getName());
                startActivityForResult(intent, REQUEST_CODE_PROVINCE);
            } else {
                Toast.makeText(ChooseLocationActivity.this, "No province selected", Toast.LENGTH_SHORT).show();
            }
        });

        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                suggestionsRecyclerView.setVisibility(View.GONE);
            }
        });

        // Set up Mapbox
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);


        // Set up RecyclerView
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        suggestionsAdapter = new SuggestionsAdapter(new ArrayList<>(), item -> {
            isSelectingSuggestion = true; // Đánh dấu đang chọn gợi ý
            searchEditText.setText(item.getPlaceName());

            moveMapToLocation(item.getLatitude(), item.getLongitude());

            suggestionsRecyclerView.setVisibility(View.GONE);
            searchEditText.clearFocus();

            // Ẩn bàn phím
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

            isSelectingSuggestion = false; // Bỏ đánh dấu sau khi chọn
        });

        suggestionsRecyclerView.setAdapter(suggestionsAdapter);

        // TextWatcher for search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (isSelectingSuggestion) return; // Nếu đang chọn gợi ý thì không gọi search()

                String query = editable.toString();
                if (!query.isEmpty()) {
                    search(query);
                } else {
                    suggestionsRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PROVINCE && resultCode == RESULT_OK && data != null) {
            String selectedProvinceName = data.getStringExtra("selectedProvince");

            Log.d("ChooseLocationActivity", "Received province: " + selectedProvinceName); // Kiểm tra dữ liệu nhận

            updateSelectedProvince(selectedProvinceName);
        }
    }

    private void updateSelectedProvince(String provinceName) {
        List<Province> provinceList = ProvinceList.getProvinces();
        for (Province p : provinceList) {
            if (p.getName().equals(provinceName)) {
                province = p; // Cập nhật tỉnh mới
                tvProvinceName.setText(province.getName());
                moveMapToLocation(province.getLat(), province.getLon()); // Di chuyển bản đồ
                return;
            }
        }
        Toast.makeText(this, "Không tìm thấy tỉnh", Toast.LENGTH_SHORT).show();
    }

    private boolean checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Please allow location access to continue", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location location = task.getResult();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        moveMapToLocation(latitude, longitude);
                        getProvinceFromLocation(latitude, longitude);
                    } else {
                        // Nếu không lấy được vị trí, đặt tỉnh mặc định
                        province = new Province("Hồ Chí Minh", 10.762622, 106.660172);
                        tvProvinceName.setText(province.getName());
                        Toast.makeText(ChooseLocationActivity.this, "Không lấy được vị trí, sử dụng tỉnh mặc định", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void moveMapToLocation(double latitude, double longitude) {
        mapView.getMapboxMap().setCamera(new CameraOptions.Builder()
                .center(Point.fromLngLat(longitude, latitude))
                .zoom(14.0)  // Điều chỉnh mức zoom phù hợp
                .build());
    }

    private void getProvinceFromLocation(double latitude, double longitude) {
        List<Province> provinceList = ProvinceList.getProvinces();
        double minDistance = Double.MAX_VALUE;
        Province closestProvince = null;

        for (Province province : provinceList) {
            double distance = Functions.calculateDistance(latitude, longitude, province.getLat(), province.getLon());
            if (distance < minDistance) {
                minDistance = distance;
                closestProvince = province;
            }
        }

        if (closestProvince != null) {
            province = closestProvince; // Save the province
            tvProvinceName.setText(province.getName());
        } else {
            tvProvinceName.setText("Province not found");
        }
    }

    private void search(String query) {
        new Thread(() -> {
            try {
                String url;
                if (province != null) {
                    url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + query + ".json?access_token=" + MAPBOX_ACCESS_TOKEN + "&language=vi" + "&proximity="+province.getLon()+","+province.getLat()+"&country=VN&fuzzyMatch=true";
                } else {
                    url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + query + ".json?access_token=" + MAPBOX_ACCESS_TOKEN + "&language=vi"+"&country=VN&fuzzyMatch=true";
                }

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("API Response", responseBody);

                    JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonResponse.has("features")) {
                        runOnUiThread(() -> {
                            List<SuggestionItem> suggestions = new ArrayList<>();
                            JsonArray features = jsonResponse.getAsJsonArray("features");
                            for (JsonElement feature : features) {
                                JsonObject featureObj = feature.getAsJsonObject();
                                String placeName = featureObj.get("place_name").getAsString();
                                JsonArray coordinates = featureObj.getAsJsonObject("geometry").getAsJsonArray("coordinates");
                                double longitude = coordinates.get(0).getAsDouble();
                                double latitude = coordinates.get(1).getAsDouble();

                                suggestions.add(new SuggestionItem(placeName, latitude, longitude));
                            }
                            suggestionsAdapter.updateData(suggestions);
                            suggestionsRecyclerView.setVisibility(View.VISIBLE);
                        });
                    }
                } else {
                    Log.e("API Error", "Request failed: " + response.code());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ChooseLocationActivity.this, "Search error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
