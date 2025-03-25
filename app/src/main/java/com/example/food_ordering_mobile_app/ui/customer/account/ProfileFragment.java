package com.example.food_ordering_mobile_app.ui.customer.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.User;
import com.example.food_ordering_mobile_app.ui.customer.notifications.NotificationActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewmodels.UploadViewModel;
import com.example.food_ordering_mobile_app.viewmodels.UserViewModel;
import android.Manifest;


import java.io.ByteArrayOutputStream;

public class ProfileFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserViewModel userViewModel;
    private UploadViewModel uploadViewModel;
    private Button btnUpdate;
    private EditText edtName, edtEmail, edtPhonenumber;
    private RadioGroup radioGroupGender;
    private LinearLayout layoutFemale, layoutMale, layoutOther;
    private RadioButton radioFemale, radioMale, radioOther;
    private ImageView ivAvatar;
    private TextView backBtn, tvShowName;
    private ImageButton goToNotificationBtn;
    private ImageView  btnUploadAvatar;
    private static final int CAMERA_REQUEST = 100;
    private static final int GALLERY_REQUEST = 200;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPhonenumber = view.findViewById(R.id.edtPhonenumber);
        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        layoutFemale = view.findViewById(R.id.layoutFemale);
        layoutMale = view.findViewById(R.id.layoutMale);
        layoutOther = view.findViewById(R.id.layoutOther);
        radioFemale = view.findViewById(R.id.radioFemale);
        radioMale = view.findViewById(R.id.radioMale);
        radioOther = view.findViewById(R.id.radioOther);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        btnUploadAvatar = view.findViewById(R.id.btnUploadAvatar);
        backBtn = view.findViewById(R.id.backBtn);
        tvShowName = view.findViewById(R.id.tvShowName);
        goToNotificationBtn = view.findViewById(R.id.goToNotificationBtn);

        layoutFemale.setOnClickListener(v -> radioGroupGender.check(R.id.radioFemale));
        layoutMale.setOnClickListener(v -> radioGroupGender.check(R.id.radioMale));
        layoutOther.setOnClickListener(v -> radioGroupGender.check(R.id.radioOther));

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        backBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            NavHostFragment.findNavController(ProfileFragment.this)
                    .navigate(R.id.action_profile_to_settings, bundle);
        });

        goToNotificationBtn.setOnClickListener(v -> goToActivity(NotificationActivity.class));

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);

        User savedUser = SharedPreferencesHelper.getInstance(requireContext()).getCurrentUser();
        if (savedUser != null) {
            edtName.setText(savedUser.getName());
            tvShowName.setText(savedUser.getName());
            edtEmail.setText(savedUser.getEmail());
            edtPhonenumber.setText(savedUser.getPhonenumber());


            if (savedUser.getGender() != null) {
                String gender = savedUser.getGender().toLowerCase();
                if (gender.equals("male")) {
                    radioGroupGender.check(R.id.radioMale);
                    radioGroupGender.check(radioMale.getId());
                } else if (gender.equals("female")) {
                    radioGroupGender.check(R.id.radioFemale);
                    radioGroupGender.check(radioFemale.getId());
                } else if (gender.equals("other")) {
                    radioGroupGender.check(R.id.radioOther);
                    radioGroupGender.check(radioOther.getId());
                }
            }

            // Lấy URL avatar từ savedUser
            String avatarUrl = savedUser.getAvatar() != null ? savedUser.getAvatar().getUrl() : null;

            Glide.with(requireContext())
                    .asBitmap()
                    .load(avatarUrl != null ? avatarUrl : R.drawable.default_avatar)
                    .apply(new RequestOptions().override(110, 110).centerCrop())
                    .into(new BitmapImageViewTarget(ivAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable roundedDrawable =
                                    RoundedBitmapDrawableFactory.create(requireContext().getResources(), resource);
                            roundedDrawable.setCornerRadius(999);
                            ivAvatar.setImageDrawable(roundedDrawable);
                        }
                    });
        }

        userViewModel.getUpdateUserResponse().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    swipeRefreshLayout.setRefreshing(true);
                    break;
                case SUCCESS:
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(requireContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                    userViewModel.getCurrentUser();
                    break;
                case ERROR:
                    swipeRefreshLayout.setRefreshing(false);
                    String errorMessage = resource.getMessage();
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

       btnUpdate.setOnClickListener(v -> handleUpdateUser());

        btnUploadAvatar.setOnClickListener(v -> showImagePickerDialog());

        uploadViewModel.getUploadAvatarResponse().observe(getViewLifecycleOwner(), new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), "Cập nhật ảnh đại diện thành công!", Toast.LENGTH_SHORT).show();
                        userViewModel.getCurrentUser();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

        return view;
    }

    private void refreshData() {
        if (userViewModel != null) {
            userViewModel.getCurrentUserResponse().removeObservers(getViewLifecycleOwner());
            userViewModel.getCurrentUser();
            userViewModel.getCurrentUserResponse().observe(getViewLifecycleOwner(), new Observer<Resource<User>>() {
                @Override
                public void onChanged(Resource<User> resource) {
                    switch (resource.getStatus()) {
                        case LOADING:
                            swipeRefreshLayout.setRefreshing(true);
                            break;
                        case SUCCESS:
                            swipeRefreshLayout.setRefreshing(false);
                            break;
                        case ERROR:
                            swipeRefreshLayout.setRefreshing(false);
                            Log.d("HomeFragment", "Error loading data: " + resource.getMessage());
                            break;
                    }
                }
            });
        }
    }

    private void handleUpdateUser() {
        String name = edtName.getText().toString().trim();
        String phonenumber = edtPhonenumber.getText().toString().trim();

        // Validate input
        if (name.isEmpty()) {
            edtName.setError("Vui lòng nhập họ tên");
            edtName.requestFocus();
            return;
        }
        if (phonenumber.isEmpty() || phonenumber.length() < 10) {
            edtPhonenumber.setError("Số điện thoại không hợp lệ");
            edtPhonenumber.requestFocus();
            return;
        }

        // Check gender selection
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        Log.d("ProfileFragment", "Selected Gender ID: " + selectedId);
        if (selectedId == -1) {
            Toast.makeText(requireContext(), "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = "";
        if (selectedId == R.id.radioMale) {
            gender = "male";
        } else if (selectedId == R.id.radioFemale) {
            gender = "female";
        } else if (selectedId == R.id.radioOther) {
            gender = "other";
        }

        User user = new User(name, phonenumber, gender);

        userViewModel.updateUser(user);
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn ảnh")
                .setItems(new CharSequence[]{"Chụp ảnh", "Chọn từ thư viện"}, (dialog, which) -> {
                    if (which == 0) {
                        openCamera();
                    } else {
                        openGallery();
                    }
                })
                .show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera(); // Gọi lại phương thức mở camera nếu đã được cấp quyền
            } else {
                Toast.makeText(requireContext(), "Bạn cần cấp quyền Camera để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            // Mở camera nếu đã có quyền
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA_PERMISSION);
        }
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "CapturedImage", null);
        return Uri.parse(path);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) { // Use Activity.RESULT_OK instead of RESULT_OK
            if (requestCode == CAMERA_REQUEST && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri imageUri = getImageUri(getContext(), photo);
                uploadViewModel.uploadAvatar(imageUri, getContext());
                ivAvatar.setImageBitmap(photo);
            } else if (requestCode == GALLERY_REQUEST && data != null) {
                Uri selectedImageUri = data.getData();
                uploadViewModel.uploadAvatar(selectedImageUri, getContext());
                ivAvatar.setImageURI(selectedImageUri);
            }
        }
    }

    private void goToActivity(Class<?> activityClass) {
        Intent intent = new Intent(requireContext(), activityClass);
        startActivity(intent);
    }
}
