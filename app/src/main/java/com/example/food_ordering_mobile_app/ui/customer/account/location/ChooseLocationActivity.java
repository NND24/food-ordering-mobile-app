package com.example.food_ordering_mobile_app.ui.customer.account.location;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;



import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.location.Location;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ChooseLocationActivity extends AppCompatActivity implements MapEventsReceiver {
    private MapView mapView;
    private EditText searchEditText;
    private RecyclerView suggestionsRecyclerView;
    private SuggestionsAdapter suggestionsAdapter;
    private LinearLayout btnChooseProvince;
    private TextView tvProvinceName;
    private Button btnSaveLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1Ijoibm5kMjQiLCJhIjoiY204eTluZzl6MDhtcjJqc2FsMzN3Z3ozaCJ9.wigbyhwGl22d2pdzdntD_g"; // Replace with your token
    private Province province;
    private boolean isSelectingSuggestion = false;
    private static final int REQUEST_CODE_PROVINCE = 100;
    private Marker fixedMarker;
    private Marker draggableMarker;
    private double lat = 200, lon = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_choose);

        mapView = findViewById(R.id.mapView);
        searchEditText = findViewById(R.id.searchEditText);
        suggestionsRecyclerView = findViewById(R.id.suggestionsRecyclerView);
        btnChooseProvince = findViewById(R.id.btnChooseProvince);
        tvProvinceName = findViewById(R.id.tvProvinceName);
        btnSaveLocation = findViewById(R.id.btnSaveLocation);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));
        mapView.setTileSource(TileSourceFactory.MAPNIK); // Chọn kiểu bản đồ
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);

        MapEventsOverlay eventsOverlay = new MapEventsOverlay(this);
        mapView.getOverlays().add(eventsOverlay);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0.0);
        lon = intent.getDoubleExtra("lon", 0.0);

        if (checkLocationPermission()) {
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }

        btnChooseProvince.setOnClickListener(v -> {
            if (province != null) {
                Intent newIntent = new Intent(ChooseLocationActivity.this, ChooseProvinceActivity.class);
                newIntent.putExtra("currentProvince", province.getName());
                startActivityForResult(newIntent, REQUEST_CODE_PROVINCE);
            } else {
                Toast.makeText(ChooseLocationActivity.this, "No province selected", Toast.LENGTH_SHORT).show();
            }
        });

        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                suggestionsRecyclerView.setVisibility(View.GONE);
            }
        });

        // Set up RecyclerView
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        suggestionsAdapter = new SuggestionsAdapter(new ArrayList<>(), location -> {
            isSelectingSuggestion = true; // Đánh dấu đang chọn gợi ý
            searchEditText.setText(location.getPlaceName());

            GeoPoint newPosition = new GeoPoint(location.getLatitude(), location.getLongitude());
            if (draggableMarker != null) {
                // Di chuyển marker đến vị trí mới
                draggableMarker.setPosition(newPosition);
            } else {
                // Nếu chưa có marker, tạo mới marker
                addMarker(location.getLatitude(), location.getLongitude());
            }
            moveMapToLocation(location.getLatitude(), location.getLongitude());

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

        btnSaveLocation.setOnClickListener(v -> {
            if (draggableMarker != null) {
                double lat = draggableMarker.getPosition().getLatitude();
                double lon = draggableMarker.getPosition().getLongitude();

                // Hiển thị thông tin
                getLocationDetailsOnline(lat, lon);
            } else {
                Toast.makeText(this, "Chưa có marker!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PROVINCE && resultCode == RESULT_OK && data != null) {
            String selectedProvinceName = data.getStringExtra("selectedProvince");
            updateSelectedProvince(selectedProvinceName);
        }
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        if (draggableMarker != null) {
            // Di chuyển marker đến vị trí mới
            draggableMarker.setPosition(p);
        } else {
            // Nếu chưa có marker, tạo mới marker
            addMarker(p.getLatitude(), p.getLongitude());
        }

        // Di chuyển bản đồ đến vị trí người dùng nhấn
        moveMapToLocation(p.getLatitude(), p.getLongitude());
        mapView.invalidate();
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        if (draggableMarker != null) {
            // Di chuyển marker đến vị trí mới
            draggableMarker.setPosition(p);
        } else {
            // Nếu chưa có marker, tạo mới marker
            addMarker(p.getLatitude(), p.getLongitude());
        }

        // Di chuyển bản đồ đến vị trí người dùng nhấn
        moveMapToLocation(p.getLatitude(), p.getLongitude());
        mapView.invalidate();
        return true;
    }

    private void getLocationDetailsOnline(double lat, double lon) {
        String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + lat + "&lon=" + lon;

        new Thread(() -> {
            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                String address = jsonResponse.optString("display_name", "Không tìm thấy địa chỉ");

                runOnUiThread(() -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("latitude", lat);
                    resultIntent.putExtra("longitude", lon);
                    resultIntent.putExtra("address", address);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Đóng Activity
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi lấy dữ liệu từ API", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void updateSelectedProvince(String provinceName) {
        List<Province> provinceList = ProvinceList.getProvinces();
        for (Province p : provinceList) {
            if (p.getName().equals(provinceName)) {
                province = p; // Cập nhật tỉnh mới
                tvProvinceName.setText(province.getName());
                GeoPoint newPosition = new GeoPoint(p.getLat(), p.getLon());
                if (draggableMarker != null) {
                    // Di chuyển marker đến vị trí mới
                    draggableMarker.setPosition(newPosition);
                } else {
                    // Nếu chưa có marker, tạo mới marker
                    addMarker(p.getLat(), p.getLon());
                }
                moveMapToLocation(p.getLat(), p.getLon()); // Di chuyển bản đồ
                mapView.invalidate();
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
                        addHomeMarker(latitude, longitude);
                        if(lat == 200) {
                            addMarker(latitude, longitude);
                            getProvinceFromLocation(latitude, longitude);
                        } else {
                            addMarker(lat, lon);
                            getProvinceFromLocation(lat, lon);
                        }
                    } else {
                        // Nếu không lấy được vị trí, đặt tỉnh mặc định
                        province = ProvinceList.getProvinces().get(1);
                        tvProvinceName.setText(province.getName());
                        moveMapToLocation(province.getLat(), province.getLon());
                        addMarker(province.getLat(),  province.getLon());
                        Toast.makeText(ChooseLocationActivity.this, "Không lấy được vị trí, sử dụng tỉnh mặc định", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addHomeMarker(double latitude, double longitude) {
        GeoPoint fixedPoint = new GeoPoint(latitude, longitude);

        // Tạo marker cố định nếu chưa có
        if (fixedMarker == null) {
            fixedMarker = new Marker(mapView);
            fixedMarker.setPosition(fixedPoint);
            fixedMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            fixedMarker.setTitle("Vị trí hiện tại");
            fixedMarker.setIcon(getResources().getDrawable(R.drawable.ic_home)); // Icon cố định
            fixedMarker.setDraggable(false); // Marker cố định không thể kéo
            mapView.getOverlays().add(fixedMarker); // Thêm marker vào bản đồ
        }

        mapView.invalidate();
    }

    private void addMarker(double latitude, double longitude) {
        GeoPoint fixedPoint = new GeoPoint(latitude, longitude);
        // Tạo marker có thể kéo nếu chưa có
        if (draggableMarker == null) {
            draggableMarker = new Marker(mapView);
            draggableMarker.setPosition(fixedPoint); // Đặt vị trí ban đầu cho marker di động
            draggableMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            draggableMarker.setIcon(getResources().getDrawable(R.drawable.ic_location_active)); // Icon di động
            draggableMarker.setDraggable(true); // Cho phép kéo marker
            mapView.getOverlays().add(draggableMarker); // Thêm marker vào bản đồ
        }

        mapView.invalidate(); // Cập nhật lại bản đồ
    }

    private void moveMapToLocation(double latitude, double longitude) {
        GeoPoint newLocation = new GeoPoint(latitude, longitude);
        mapView.getController().animateTo(newLocation); // Thay đổi từ setCenter() thành animateTo()
        mapView.invalidate(); // Cập nhật lại giao diện bản đồ
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
                Thread.sleep(1000);
                String url;
                if (province != null) {
                    url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(query, "UTF-8") +
                            "&format=json&addressdetails=1&countrycodes=VN&limit=5&viewbox=" +
                            (province.getLon() - 0.5) + "," + (province.getLat() + 0.5) + "," +
                            (province.getLon() + 0.5) + "," + (province.getLat() - 0.5) +
                            "&bounded=1";
                } else {
                    url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(query, "UTF-8") +
                            "&format=json&addressdetails=1&countrycodes=VN&limit=5";
                }

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "YourAppName") // Thêm User-Agent để tránh bị block
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("API Response", responseBody);

                    JsonArray jsonResponse = new Gson().fromJson(responseBody, JsonArray.class);
                    if (jsonResponse.size() > 0) {
                        runOnUiThread(() -> {
                            List<SuggestionItem> suggestions = new ArrayList<>();
                            for (JsonElement element : jsonResponse) {
                                JsonObject place = element.getAsJsonObject();
                                String placeName = place.get("display_name").getAsString();
                                double latitude = place.get("lat").getAsDouble();
                                double longitude = place.get("lon").getAsDouble();

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
                Log.e("API Error", "Search error: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(ChooseLocationActivity.this, "Search error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }


    public void goBack(View view) {
        onBackPressed();
    }
}
