package com.example.food_ordering_mobile_app.ui.customer.cart;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.CartSummaryAdapter;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.cart.CartItem;
import com.example.food_ordering_mobile_app.models.dish.Topping;
import com.example.food_ordering_mobile_app.models.dish.ToppingGroup;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.ui.customer.account.location.EditCurrentLocationActivity;
import com.example.food_ordering_mobile_app.ui.customer.account.location.LocationActivity;
import com.example.food_ordering_mobile_app.ui.customer.orders.OrderDetailActivity;
import com.example.food_ordering_mobile_app.ui.customer.store.StoreActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartDetailActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private CartViewModel cartViewModel;
    private RecyclerView orderSummaryRecyclerView;
    private CartSummaryAdapter cartSummaryAdapter;
    private List<CartItem> cartItemList;
    private TextView tvStoreName, tvStoreDescription, tvProvisionalTotal, tvFee, tvTotalPrice, tvLocationName, tvDeliveryAddress;
    private ImageView ivStoreAvatar;
    private Button btnCompleteCart;
    private LinearLayout btnAddDetailAddress, location_container, btnChooseCurrentLocation, storeInfoContainer;
    private String storeId, locationName ="", deliveryAddress="", customerName, customerPhonenumber, detailAddress, note;
    private Double lat, lon;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_CODE_EDIT_LOCATION = 1001;
    User savedUser = SharedPreferencesHelper.getInstance(this).getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_detail);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        orderSummaryRecyclerView = findViewById(R.id.orderSummaryRecyclerView);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreDescription = findViewById(R.id.tvStoreDescription);
        ivStoreAvatar = findViewById(R.id.ivStoreAvatar);
        tvProvisionalTotal = findViewById(R.id.tvProvisionalTotal);
        tvFee = findViewById(R.id.tvFee);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCompleteCart = findViewById(R.id.btnCompleteCart);
        btnAddDetailAddress = findViewById(R.id.btnAddDetailAddress);
        tvLocationName = findViewById(R.id.tvLocationName);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        location_container = findViewById(R.id.location_container);
        btnChooseCurrentLocation = findViewById(R.id.btnChooseCurrentLocation);
        storeInfoContainer = findViewById(R.id.storeInfoContainer);

        storeId = getIntent().getStringExtra("storeId");

        if (savedUser != null) {
            customerName = savedUser.getName();
            customerPhonenumber = savedUser.getPhonenumber();
        }

        storeInfoContainer.setOnClickListener(v -> {
            finish();
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        setupCartDetail();

        if (checkLocationPermission()) {
            if (deliveryAddress.isEmpty() || lat == 200 || lon == 200) {
                getCurrentLocation();
            }
        } else {
            requestLocationPermission();
        }

        btnChooseCurrentLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, LocationActivity.class);
            intent.putExtra("storeId", storeId);
            intent.putExtra("isFromCartDetailActivity", true);
            startActivityForResult(intent, REQUEST_CODE_EDIT_LOCATION);
        });

        btnAddDetailAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditCurrentLocationActivity.class);
            intent.putExtra("storeId", storeId);
            intent.putExtra("locationName", locationName);
            intent.putExtra("deliveryAddress", deliveryAddress);
            intent.putExtra("customerName", customerName);
            intent.putExtra("customerPhonenumber", customerPhonenumber);
            intent.putExtra("detailAddress", detailAddress);
            intent.putExtra("note", note);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            startActivityForResult(intent, REQUEST_CODE_EDIT_LOCATION);
        });

        btnCompleteCart.setOnClickListener(v -> handleCompleteCart());

        cartViewModel.getCompleteCartResponse().observe(this, new Observer<Resource<ApiResponse<String>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<String>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(CartDetailActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CartDetailActivity.this, StoreActivity.class);
                        intent.putExtra("storeId", storeId);
                        startActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
    }

    private String getValidString(String value, String fallback) {
        return (value != null && !value.isEmpty()) ? value : fallback;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_LOCATION && resultCode == RESULT_OK && data != null) {
            storeId = data.getStringExtra("storeId");
            locationName = data.getStringExtra("locationName") != null ? data.getStringExtra("locationName") : "";
            deliveryAddress = data.getStringExtra("deliveryAddress") != null ? data.getStringExtra("deliveryAddress") : "";
            customerName = getValidString(data.getStringExtra("customerName"), savedUser.getName());
            customerPhonenumber = getValidString(data.getStringExtra("customerPhonenumber"), savedUser.getPhonenumber());
            detailAddress = data.getStringExtra("detailAddress") != null ? data.getStringExtra("detailAddress") : "";
            note = data.getStringExtra("note") != null ? data.getStringExtra("note") : "";
            lat = data.hasExtra("lat") ? data.getDoubleExtra("lat", 200) : 200;
            lon = data.hasExtra("lon") ? data.getDoubleExtra("lon", 200) : 200;

            tvLocationName.setText(locationName);
            tvDeliveryAddress.setText(deliveryAddress);
        }
    }

    private void setupCartDetail() {
        orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();
        cartSummaryAdapter = new CartSummaryAdapter(this, cartItemList);
        orderSummaryRecyclerView.setAdapter(cartSummaryAdapter);

        cartViewModel.getUserCartInStoreResponse().observe(this, new Observer<Resource<ApiResponse<Cart>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Cart>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        tvStoreName.setText(resource.getData().getData().getStore().getName());
                        tvStoreDescription.setText(resource.getData().getData().getStore().getDescription());

                        // Xóa danh sách cũ và cập nhật danh sách mới
                        cartItemList.clear();
                        cartItemList.addAll(resource.getData().getData().getItems());
                        cartSummaryAdapter.notifyDataSetChanged();

                        String storeAvatarUrl = resource.getData().getData().getStore().getAvatar() != null ? resource.getData().getData().getStore().getAvatar().getUrl() : null;
                        Glide.with(ivStoreAvatar)
                                .asBitmap()
                                .load(storeAvatarUrl)
                                .override(60, 60)
                                .centerCrop()
                                .into(new BitmapImageViewTarget(ivStoreAvatar) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable roundedDrawable =
                                                RoundedBitmapDrawableFactory.create(ivStoreAvatar.getResources(), resource);
                                        roundedDrawable.setCornerRadius(6);
                                        ivStoreAvatar.setImageDrawable(roundedDrawable);
                                    }
                                });

                        // Tính tổng giá tiền của giỏ hàng
                        double totalCartPrice = 0;
                        for (CartItem item : cartItemList) {
                            double dishPrice = item.getDish().getPrice();
                            double totalToppingPrice = 0;

                            for (Topping topping : item.getToppings()) {
                                totalToppingPrice += topping.getPrice();
                            }

                            // Tính giá tiền của mỗi món và cộng vào tổng giỏ hàng
                            totalCartPrice += (dishPrice + totalToppingPrice) * item.getQuantity();
                        }

                        // Hiển thị tổng giá tiền
                        tvProvisionalTotal.setText(String.format("%.0f", totalCartPrice));
                        tvFee.setText(String.format("%.0f", totalCartPrice));
                        tvTotalPrice.setText(String.format("%.0f", totalCartPrice));

                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        cartViewModel.getUserCartInStore(storeId);
    }

    private void handleCompleteCart() {
        // Kiểm tra các trường bắt buộc có giá trị hợp lệ
        if (storeId == null || storeId.isEmpty()) {
            Toast.makeText(this, "Store ID không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (deliveryAddress == null || deliveryAddress.isEmpty() || lat.equals("200")  || lon.equals("200")) {
            Toast.makeText(this, "Địa chỉ giao hàng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customerName == null || customerName.isEmpty()) {
            Toast.makeText(this, "Tên khách hàng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customerPhonenumber == null || customerPhonenumber.isEmpty()) {
            Toast.makeText(this, "Số điện thoại khách hàng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("storeId", storeId);
        data.put("paymentMethod", "cash");
        data.put("deliveryAddress", deliveryAddress);
        data.put("customerName", customerName);
        data.put("customerPhonenumber", customerPhonenumber);
        data.put("detailAddress", detailAddress);
        data.put("note", note);

        List<Double> location = new ArrayList<>();
        location.add(lon);
        location.add(lat);
        data.put("location", location);

        Log.d("CartDetailActivity", "handleCompleteCart: " + data.toString());

        // Gọi phương thức completeCart trong ViewModel
        cartViewModel.completeCart(data);
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

    private void getLocationDetailsOnline(double latitude, double longitude) {
        String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude;

        new Thread(() -> {
            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "MyApp/1.0 (contact@example.com)");

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
                    deliveryAddress = address;
                    locationName = "Địa chỉ hiện tại";
                    tvLocationName.setText(locationName);
                    tvDeliveryAddress.setText(deliveryAddress);
                    lat = latitude;
                    lon = longitude;

                    location_container.setVisibility(address.isEmpty() ? View.GONE : View.VISIBLE);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi lấy dữ liệu từ API", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location location = task.getResult();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        getLocationDetailsOnline(latitude, longitude);
                    } else {
                        Toast.makeText(CartDetailActivity.this, "Không lấy được vị trí, sử dụng tỉnh mặc định", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void refreshData() {
        cartViewModel.getUserCartInStore(storeId);
    }

    public void goBack(View view) {
        finish();
    }

    public void goToOrderDetail(View view) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        startActivity(intent);
    }
}