package com.example.food_ordering_mobile_app.ui.customer.rating;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.Image;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewmodels.OrderViewModel;
import com.example.food_ordering_mobile_app.viewmodels.RatingViewModel;
import com.example.food_ordering_mobile_app.viewmodels.UploadViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRatingActivity extends AppCompatActivity {
    private UploadViewModel uploadViewModel;
    private RatingViewModel ratingViewModel;
    private OrderViewModel orderViewModel;
    private String storeId, orderId;
    private static final int PICK_IMAGES_REQUEST = 1;
    private List<Uri> selectedImageUris = new ArrayList<>();
    private RatingBar ratingBar;
    private EditText editText;
    private LinearLayout imagePreviewContainer, btnChooseImage;
    private Button submitButton;
    private List<OrderItem> orderItemList;
    private TextView tvStoreName;
    private ImageView ivStoreAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_rating);

        Intent intent = getIntent();
        storeId = intent.getStringExtra("storeId");
        orderId = intent.getStringExtra("orderId");

        ratingBar = findViewById(R.id.ratingBar);
        editText = findViewById(R.id.editText);
        imagePreviewContainer = findViewById(R.id.imagePreviewContainer);
        submitButton = findViewById(R.id.footer).findViewById(R.id.btnAddRating);
        tvStoreName = findViewById(R.id.tvStoreName);
        ivStoreAvatar = findViewById(R.id.ivStoreAvatar);
        btnChooseImage = findViewById(R.id.btnChooseImage);

        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        ratingViewModel = new ViewModelProvider(this).get(RatingViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        btnChooseImage.setOnClickListener(view -> openImagePicker());

        setupOrderDetail();

        submitButton.setOnClickListener(view -> uploadImagesAndSubmitRating());

        ratingViewModel.getAddStoreRatingResponse().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        // Hiển thị loading nếu cần
                        break;
                    case SUCCESS:
                        Intent intent = new Intent(AddRatingActivity.this, RatingActivity.class);
                        intent.putExtra("storeId", storeId);
                        startActivity(intent);

                        Toast.makeText(AddRatingActivity.this, "Đánh giá đã được gửi!", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:

                        break;
                }
            }
        });
    }

    private void setupOrderDetail() {
        orderItemList = new ArrayList<>();
        orderViewModel.getOrderDetail(orderId);
        orderViewModel.getOrderDetailResponse().observe(this, new Observer<Resource<ApiResponse<Order>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Order>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        orderItemList.clear();
                        orderItemList.addAll(resource.getData().getData().getItems());
                        Store store = resource.getData().getData().getStore();
                        tvStoreName.setText(store.getName());

                        String storeAvatarUrl = store.getAvatar() != null ? store.getAvatar().getUrl() : null;
                        Glide.with(ivStoreAvatar)
                                .asBitmap()
                                .load(storeAvatarUrl)
                                .override(95, 95)
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
                        break;
                    case ERROR:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                // Nếu chọn nhiều ảnh
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    if (!selectedImageUris.contains(uri)) {
                        selectedImageUris.add(uri); // Thêm ảnh mới vào danh sách
                        addImagePreview(uri); // Thêm ảnh vào giao diện
                    }
                }
            } else if (data.getData() != null) {
                // Nếu chọn 1 ảnh
                Uri uri = data.getData();
                if (!selectedImageUris.contains(uri)) {
                    selectedImageUris.add(uri); // Thêm ảnh mới vào danh sách
                    addImagePreview(uri); // Thêm ảnh vào giao diện
                }
            }
        }
    }

    private void addImagePreview(Uri uri) {
        // Tạo layout cha chứa ảnh và nút xoá
        FrameLayout container = new FrameLayout(this);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                200,
                200
        );
        containerParams.setMargins(16, 0, 16, 0); // margin giữa các ảnh
        container.setLayoutParams(containerParams);

        // Tạo ảnh
        ImageView imageView = new ImageView(this);
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageURI(uri);

        Glide.with(this)
                .load(uri)
                .transform(new RoundedCorners(30))
                .into(imageView);

        // Tạo nút xoá
        ImageView deleteButton = new ImageView(this);
        FrameLayout.LayoutParams deleteParams = new FrameLayout.LayoutParams(
                60,
                60
        );
        deleteParams.setMargins(0, 0, 5, 5);  // Thêm margin cách viền 5dp
        deleteParams.gravity = android.view.Gravity.END | android.view.Gravity.TOP;

        // Đặt kích thước cho nút xóa
        deleteParams.width = getResources().getDimensionPixelSize(R.dimen.delete_button_size);  // Kích thước tùy chỉnh (thường là 24dp)
        deleteParams.height = getResources().getDimensionPixelSize(R.dimen.delete_button_size);  // Kích thước tùy chỉnh (thường là 24dp)

        deleteButton.setLayoutParams(deleteParams);
        deleteButton.setImageResource(R.drawable.ic_close_white); // Đảm bảo có icon trong drawable
        deleteButton.setBackgroundResource(R.drawable.circle_orange); // Tạo hình tròn cho nút xoá
        deleteButton.setPadding(10, 10, 10, 10); // Có thể giảm padding để icon nhỏ hơn

        deleteButton.setOnClickListener(v -> {
            selectedImageUris.remove(uri);
            imagePreviewContainer.removeView(container);
        });

        // Gắn ảnh và nút xoá vào FrameLayout
        container.addView(imageView);
        container.addView(deleteButton);

        // Thêm vào container chính
        imagePreviewContainer.addView(container);
    }

    private boolean isValidRatingAndComment(float rating, String comment) {
        if (rating == 0) {
            Toast.makeText(this, "Vui lòng chọn số sao đánh giá", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (comment.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung đánh giá", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void uploadImagesAndSubmitRating() {
        float rating = ratingBar.getRating();
        String comment = editText.getText().toString();

        if (!isValidRatingAndComment(rating, comment)) {
            return; // Dừng nếu không hợp lệ
        }

        List<String> dishIds = new ArrayList<>();
        for (OrderItem item : orderItemList) {
            if (item.getDish() != null) {
                dishIds.add(item.getDish().getId());
            }
        }

        if (selectedImageUris.isEmpty()) {
            Log.d("RatingDebug", "No images selected.");
            // Không có ảnh, gửi đánh giá bình thường
            Map<String, Object> data = new HashMap<>();
            data.put("dishes", dishIds);
            data.put("ratingValue", rating);
            data.put("comment", comment);
            ratingViewModel.addStoreRating(storeId, data);
            Toast.makeText(this, "Đánh giá đã được gửi!", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("RatingDebug", "Images selected: ");
            // Có ảnh, tải ảnh và gửi đánh giá kèm ảnh
            uploadViewModel.uploadImages(selectedImageUris, this);
            uploadViewModel.getUploadImagesResponse().observe(this, new Observer<Resource<List<Image>>>() {
                @Override
                public void onChanged(Resource<List<Image>> resource) {
                    switch (resource.getStatus()) {
                        case SUCCESS:
                            List<Image> uploadedImageUrls = resource.getData();
                            Log.d("RatingDebug", "Uploaded Image URLs: " + uploadedImageUrls);
                            submitRating(uploadedImageUrls);
                            break;
                        case ERROR:
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void submitRating(List<Image> imageUrls) {
        float rating = ratingBar.getRating();
        String comment = editText.getText().toString();

        if (rating == 0) {
            Toast.makeText(this, "Vui lòng chọn số sao đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }

        if (comment.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUrls == null || imageUrls.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn lại hình ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> dishIds = new ArrayList<>();
        for (OrderItem item : orderItemList) {
            if (item.getDish() != null) {
                dishIds.add(item.getDish().getId());
            }
        }

        Log.d("RatingDebug", "Image URLs: "  + imageUrls);

        Map<String, Object> data = new HashMap<>();
        data.put("dishes", dishIds);
        data.put("ratingValue", rating);
        data.put("comment", comment);
        data.put("images", imageUrls);
        ratingViewModel.addStoreRating(storeId, data);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGES_REQUEST);
    }

    public void goBack(View view) {
        finish();
    }
}