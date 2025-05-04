package com.example.food_ordering_mobile_app.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.location.Province;
import com.example.food_ordering_mobile_app.models.location.ProvinceList;
import com.example.food_ordering_mobile_app.models.notification.Notification;
import com.example.food_ordering_mobile_app.network.SocketManager;
import com.example.food_ordering_mobile_app.ui.customer.account.location.ChooseLocationActivity;
import com.example.food_ordering_mobile_app.ui.customer.account.location.ChooseProvinceActivity;
import com.example.food_ordering_mobile_app.ui.customer.notifications.NotificationActivity;
import com.example.food_ordering_mobile_app.utils.Functions;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;
import com.example.food_ordering_mobile_app.viewmodels.NotificationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomHeaderView extends LinearLayout {
    private TextView unreadNotificationCount, cartItemCount, tvProvinceName;
    private ImageButton btnNotification, btnCart;
    private RelativeLayout unreadNotificationBadge, cartItemCountBadge;
    private CartViewModel cartViewModel;
    private NotificationViewModel notificationViewModel;
    private LifecycleOwner lifecycleOwner;
    private boolean isNotificationObserverSet = false;
    private boolean isCartObserverSet = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private Province province;
    private static final int REQUEST_CODE_PROVINCE = 100;
    private LinearLayout btnChooseProvince;
    private double lat = 200, lon = 200;

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        setUpUserCart();
        setUpUserNotification();
    }

    public CustomHeaderView(Context context) {
        super(context);
        init(context);
    }

    public CustomHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_custom_header_view, this, true);

        btnNotification = findViewById(R.id.btnNotification);
        btnCart = findViewById(R.id.btnCart);
        cartItemCount = findViewById(R.id.cartItemCount);
        unreadNotificationBadge = findViewById(R.id.unreadNotificationBadge);
        unreadNotificationCount = findViewById(R.id.unreadNotificationCount);
        cartItemCountBadge = findViewById(R.id.cartItemCountBadge);
        btnChooseProvince = findViewById(R.id.btnChooseProvince);
        tvProvinceName = findViewById(R.id.tvProvinceName);

        cartViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(CartViewModel.class);
        notificationViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NotificationViewModel.class);

        // Khi nhấn vào Notification Button, chuyển đến NotificationActivity
        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationActivity.class);
            context.startActivity(intent);
        });

        // Khi nhấn vào Cart Button, chuyển đến CartActivity
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.food_ordering_mobile_app.ui.customer.cart.CartActivity.class);
            context.startActivity(intent);
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        SharedPreferencesHelper prefs = SharedPreferencesHelper.getInstance(getContext());
        String savedProvinceName = prefs.getProvinceName();

        if (savedProvinceName == null || savedProvinceName.isEmpty()) {
            if (checkLocationPermission()) {
                getCurrentLocation();
            } else {
                requestLocationPermission();
            }
        } else {
            loadSavedProvince(); // Đảm bảo tỉnh đã chọn trước đó được hiển thị
        }


        btnChooseProvince.setOnClickListener(v -> {
            List<Province> provinceList = ProvinceList.getProvinces();
            String[] provinceNames = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                provinceNames[i] = provinceList.get(i).getName();
            }

            new android.app.AlertDialog.Builder(getContext())
                    .setTitle("Chọn tỉnh")
                    .setItems(provinceNames, (dialog, which) -> {
                        province = provinceList.get(which);
                        tvProvinceName.setText(province.getName());

                        // 🔥 Lưu vào SharedPreferences
                        saveProvinceToPreferences(province);
                    })
                    .show();

        });

        loadSavedProvince();
    }

    private void loadSavedProvince() {
        SharedPreferencesHelper prefs = SharedPreferencesHelper.getInstance(getContext());
        String savedProvinceName = prefs.getProvinceName();
        double savedLat = prefs.getProvinceLat();
        double savedLon = prefs.getProvinceLon();

        if (savedProvinceName != null && !savedProvinceName.isEmpty()) {
            province = new Province(savedProvinceName, savedLat, savedLon); // nếu bạn có constructor phù hợp
            tvProvinceName.setText(savedProvinceName);
        }
    }


    private void updateSelectedProvince(String provinceName) {
        List<Province> provinceList = ProvinceList.getProvinces();
        for (Province p : provinceList) {
            if (p.getName().equals(provinceName)) {
                province = p; // Cập nhật tỉnh mới
                tvProvinceName.setText(province.getName());
                GeoPoint newPosition = new GeoPoint(p.getLat(), p.getLon());
                return;
            }
        }
        Toast.makeText(this.getContext(), "Không tìm thấy tỉnh", Toast.LENGTH_SHORT).show();
    }

    private boolean checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestLocationPermission() {
        Context context = getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, "Please allow location access to continue", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    private void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        getProvinceFromLocation(latitude, longitude);
                    } else {
                        // Nếu không lấy được vị trí
                        setDefaultProvince();
                    }
                })
                .addOnFailureListener(e -> {
                    // Nếu có lỗi
                    setDefaultProvince();
                });
    }

    private void getProvinceFromLocation(double latitude, double longitude) {
        List<Province> provinceList = ProvinceList.getProvinces();
        double minDistance = Double.MAX_VALUE;
        Province closestProvince = null;

        for (Province p : provinceList) {
            double distance = Functions.calculateDistance(latitude, longitude, p.getLat(), p.getLon());
            if (distance < minDistance) {
                minDistance = distance;
                closestProvince = p;
            }
        }

        if (closestProvince != null) {
            province = closestProvince;
            tvProvinceName.setText(province.getName());
            saveProvinceToPreferences(province);
        } else {
            setDefaultProvince();
        }
    }

    private void setDefaultProvince() {
        // TP. Hồ Chí Minh thường ở vị trí 0 trong danh sách hoặc bạn có thể tìm bằng tên
        for (Province p : ProvinceList.getProvinces()) {
            if (p.getName().equalsIgnoreCase("TP. Hồ Chí Minh")) {
                province = p;
                break;
            }
        }

        if (province == null) {
            province = ProvinceList.getProvinces().get(0);
        }

        tvProvinceName.setText(province.getName());
        saveProvinceToPreferences(province);
        Toast.makeText(getContext(), "Không lấy được vị trí, sử dụng tỉnh mặc định", Toast.LENGTH_SHORT).show();
    }

    private void saveProvinceToPreferences(Province province) {
        SharedPreferencesHelper.getInstance(getContext())
                .saveProvince(province.getName(), province.getLat(), province.getLon());
    }

    private void setUpUserCart() {
        if (isCartObserverSet) return;

        cartViewModel.getUserCart();
        cartViewModel.getUserCartResponse().observe(lifecycleOwner, new Observer<Resource<ApiResponse<List<Cart>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Cart>>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        updateCartCount(resource.getData().getData());
                        break;
                    case ERROR:
                        updateCartCount(new ArrayList<Cart>());
                        break;
                }
            }
        });
    }

    private void updateCartCount(List<Cart> carts) {
        int cartCount = 0;

        for (Cart cart : carts) {
            cartCount++;
        }

        if (cartCount > 0) {
            cartItemCountBadge.setVisibility(VISIBLE);
            cartItemCount.setText(String.valueOf(cartCount));
        } else {
            cartItemCountBadge.setVisibility(GONE);
        }
    }

    private void setUpUserNotification() {
        if (isNotificationObserverSet) return;  // Đảm bảo chỉ thiết lập observer 1 lần

        // Kết nối socket
        SocketManager.connectSocket(getContext(), notificationViewModel);

        // Đặt observer cho thông báo
        notificationViewModel.getNotificationsResponse().observe(lifecycleOwner , new Observer<Resource<List<Notification>>>() {
            @Override
            public void onChanged(Resource<List<Notification>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        updateUnreadNotificationCount(resource.getData());
                        break;
                    case ERROR:
                        updateUnreadNotificationCount(new ArrayList<Notification>());
                        break;
                }
            }
        });

        // Đánh dấu observer đã được thiết lập
        isNotificationObserverSet = true;
    }

    private void updateUnreadNotificationCount(List<Notification> notifications) {
        int unreadCount = 0;
        Set<String> seenNotificationIds = new HashSet<>(); // Set to track seen notifications

        for (Notification notification : notifications) {
            if ("unread".equals(notification.getStatus()) && !seenNotificationIds.contains(notification.getId())) {
                unreadCount++;
                seenNotificationIds.add(notification.getId()); // Mark this notification as seen
            }
        }

        // Kiểm tra xem số lượng đã thay đổi không
        if (unreadCount != Integer.parseInt(unreadNotificationCount.getText().toString())) {
            if (unreadCount > 0) {
                unreadNotificationBadge.setVisibility(VISIBLE);
                unreadNotificationCount.setText(String.valueOf(unreadCount));
            } else {
                unreadNotificationBadge.setVisibility(GONE);
            }
        }
    }

    // Hàm để set tên vào tvName từ bên ngoài
    public void setName(String name) {

    }
}
