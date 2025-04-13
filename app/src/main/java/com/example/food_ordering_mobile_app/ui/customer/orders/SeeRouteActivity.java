package com.example.food_ordering_mobile_app.ui.customer.orders;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.location.ProvinceList;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.network.SocketManager;
import com.example.food_ordering_mobile_app.ui.customer.account.location.ChooseLocationActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.OrderViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SeeRouteActivity extends AppCompatActivity implements MapEventsReceiver {
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private OrderViewModel orderViewModel;
    private String orderId;
    private Marker customerMarker, storeMarker, shipperMarker;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Double storeLatitude, storeLongitude, customerLatitude, customerLongitude;
    private Polyline storePolyline;
    private Polyline customerPolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_route);

        mapView = findViewById(R.id.mapView);

        orderId = getIntent().getStringExtra("orderId");

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);

        MapEventsOverlay eventsOverlay = new MapEventsOverlay(this);
        mapView.getOverlays().add(eventsOverlay);

        if (checkLocationPermission()) {
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }

        setupOrderDetail();

        SocketManager.joinOrder(orderId);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // 5 giây
        locationRequest.setFastestInterval(3000); // tối thiểu 3 giây
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    addShipperMarker(latitude, longitude);

                    updateRouteToStore(latitude, longitude);
                    updateRouteToCustomer(latitude, longitude);

                    try {
                        JSONObject dataObject = new JSONObject();
                        dataObject.put("latitude", latitude);
                        dataObject.put("longitude", longitude);

                        JSONObject locationObject = new JSONObject();
                        locationObject.put("id", orderId);
                        locationObject.put("data", dataObject);

                        SocketManager.sendLocation(locationObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketManager.leaveOrder(orderId);
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        moveMapToLocation(p.getLatitude(), p.getLongitude());
        mapView.invalidate();
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        moveMapToLocation(p.getLatitude(), p.getLongitude());
        mapView.invalidate();
        return true;
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

    private void setupOrderDetail() {
        orderViewModel.getOrderDetailResponse().observe(this, new Observer<Resource<ApiResponse<Order>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Order>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        Order order = resource.getData().getData();
                        addCustomerMarker(order.getShipLocation().getCoordinates()[1], order.getShipLocation().getCoordinates()[0]);
                        addStoreMarker(order.getStore().getAddress().getLat(), order.getStore().getAddress().getLon());

                        break;
                    case ERROR:
                        break;
                }
            }
        });

        orderViewModel.getOrderDetail(orderId);
    }

    private Drawable resizeDrawable(int drawableId, int width, int height) {
        Drawable image = getResources().getDrawable(drawableId);
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return new BitmapDrawable(getResources(), resized);
    }

    private void addCustomerMarker(double latitude, double longitude) {
        GeoPoint fixedPoint = new GeoPoint(latitude, longitude);
        customerLatitude = latitude;
        customerLongitude = longitude;

        // Tạo marker cố định nếu chưa có
        if (customerMarker == null) {
            customerMarker = new Marker(mapView);
            customerMarker.setPosition(fixedPoint);
            customerMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            customerMarker.setTitle("Người đặt");
            customerMarker.setIcon(resizeDrawable(R.drawable.ic_home_marker, 24, 24));
            customerMarker.setDraggable(false);
            mapView.getOverlays().add(customerMarker);
            moveMapToLocation(latitude, longitude);
        }

        mapView.invalidate();
    }

    private void addStoreMarker(double latitude, double longitude) {
        GeoPoint fixedPoint = new GeoPoint(latitude, longitude);
        storeLatitude = latitude;
        storeLongitude = longitude;

        if (storeMarker == null) {
            storeMarker = new Marker(mapView);
            storeMarker.setPosition(fixedPoint);
            storeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            storeMarker.setTitle("Cửa hàng");
            storeMarker.setIcon(resizeDrawable(R.drawable.ic_store_marker, 24, 24));
            storeMarker.setDraggable(false);
            mapView.getOverlays().add(storeMarker);
        }

        mapView.invalidate();
    }

    private void addShipperMarker(double latitude, double longitude) {
        GeoPoint fixedPoint = new GeoPoint(latitude, longitude);

        if (shipperMarker == null) {
            shipperMarker = new Marker(mapView);
            shipperMarker.setPosition(fixedPoint);
            shipperMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            shipperMarker.setTitle("Shipper");
            shipperMarker.setIcon(resizeDrawable(R.drawable.ic_shipper_marker, 24, 24));
            shipperMarker.setDraggable(false);
            mapView.getOverlays().add(shipperMarker);
            moveMapToLocation(latitude, longitude);
        }

        mapView.invalidate();
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location location = task.getResult();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        moveMapToLocation(latitude, longitude);
                        addShipperMarker(latitude, longitude);
                    }
                });
    }

    private void updateRouteToStore(double shipperLat, double shipperLon) {
        if (storeLatitude == null || storeLongitude == null) return;

        String url = "https://router.project-osrm.org/route/v1/driving/" +
                shipperLon + "," + shipperLat + ";" + storeLongitude + "," + storeLatitude +
                "?overview=full&geometries=geojson";

        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL apiUrl = new URL(url);
                connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("User-Agent", "MyApp/1.0");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    reader.close();

                    JSONObject json = new JSONObject(responseBuilder.toString());
                    JSONArray routes = json.getJSONArray("routes");

                    if (routes.length() > 0) {
                        JSONObject geometry = routes.getJSONObject(0).getJSONObject("geometry");
                        JSONArray coordinates = geometry.getJSONArray("coordinates");

                        List<GeoPoint> geoPoints = new ArrayList<>();
                        for (int i = 0; i < coordinates.length(); i++) {
                            JSONArray coord = coordinates.getJSONArray(i);
                            double lon = coord.getDouble(0);
                            double lat = coord.getDouble(1);
                            geoPoints.add(new GeoPoint(lat, lon));
                        }

                        runOnUiThread(() -> {
                            if (storePolyline != null) {
                                mapView.getOverlays().remove(storePolyline);
                            }

                            storePolyline = new Polyline();
                            storePolyline.setPoints(geoPoints);
                            storePolyline.setColor(Color.BLUE);
                            storePolyline.setWidth(5f);
                            mapView.getOverlays().add(storePolyline);
                            mapView.invalidate();
                        });
                    }

                } else {
                    Log.e("ROUTE_ERROR", "OSRM server returned code: " + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi lấy route từ OSRM", Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) connection.disconnect();
            }
        }).start();
    }

    private void updateRouteToCustomer(double shipperLat, double shipperLon) {
        if (customerLatitude == null || customerLongitude == null) return;

        String url = "https://router.project-osrm.org/route/v1/driving/" +
                shipperLon + "," + shipperLat + ";" + customerLongitude + "," + customerLatitude +
                "?overview=full&geometries=geojson";

        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL apiUrl = new URL(url);
                connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("User-Agent", "MyApp/1.0");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    reader.close();

                    JSONObject json = new JSONObject(responseBuilder.toString());
                    JSONArray routes = json.getJSONArray("routes");

                    if (routes.length() > 0) {
                        JSONObject geometry = routes.getJSONObject(0).getJSONObject("geometry");
                        JSONArray coordinates = geometry.getJSONArray("coordinates");

                        List<GeoPoint> geoPoints = new ArrayList<>();
                        for (int i = 0; i < coordinates.length(); i++) {
                            JSONArray coord = coordinates.getJSONArray(i);
                            double lon = coord.getDouble(0);
                            double lat = coord.getDouble(1);
                            geoPoints.add(new GeoPoint(lat, lon));
                        }

                        runOnUiThread(() -> {
                            if (storePolyline != null) {
                                mapView.getOverlays().remove(storePolyline);
                            }

                            storePolyline = new Polyline();
                            storePolyline.setPoints(geoPoints);
                            storePolyline.setColor(Color.RED);
                            storePolyline.setWidth(5f);
                            mapView.getOverlays().add(storePolyline);
                            mapView.invalidate();
                        });
                    }

                } else {
                    Log.e("ROUTE_ERROR", "OSRM server returned code: " + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi lấy route từ OSRM", Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) connection.disconnect();
            }
        }).start();
    }

    private void moveMapToLocation(double latitude, double longitude) {
        GeoPoint newLocation = new GeoPoint(latitude, longitude);
        mapView.getController().animateTo(newLocation);
        mapView.invalidate();
    }
}