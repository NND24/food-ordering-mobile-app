package com.example.food_ordering_mobile_app.ui.customer.orders;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OrderSummaryAdapter;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.order.OrderTopping;
import com.example.food_ordering_mobile_app.network.SocketManager;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.OrderViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class TrackOrderActivity extends AppCompatActivity implements MapEventsReceiver {
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private OrderViewModel orderViewModel;
    private String orderId;
    private Marker customerMarker, storeMarker, shipperMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_track_order);

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

        setupOrderDetail();

        SocketManager.joinOrder(orderId);

        SocketManager.setOnLocationUpdatedListener(args  -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];
                double lat = data.getDouble("lat");
                double lon = data.getDouble("lon");

                Log.d("TrackOrderActivity", "Latitude: " + lat + ", Longitude: " + lon);
                addShipperMarker(lat, lon);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TrackOrderActivity", "Lỗi parse JSON: " + e.getMessage());
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketManager.leaveOrder(orderId);
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

        if (customerMarker == null) {
            customerMarker = new Marker(mapView);
            customerMarker.setPosition(fixedPoint);
            customerMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            customerMarker.setTitle("Nhà");
            customerMarker.setIcon(resizeDrawable(R.drawable.ic_home_marker, 24, 24));
            customerMarker.setDraggable(false);
            mapView.getOverlays().add(customerMarker);
            moveMapToLocation(latitude, longitude);
        }

        mapView.invalidate();
    }

    private void addStoreMarker(double latitude, double longitude) {
        GeoPoint fixedPoint = new GeoPoint(latitude, longitude);

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

    private void moveMapToLocation(double latitude, double longitude) {
        GeoPoint newLocation = new GeoPoint(latitude, longitude);
        mapView.getController().animateTo(newLocation);
        mapView.invalidate();
    }
}