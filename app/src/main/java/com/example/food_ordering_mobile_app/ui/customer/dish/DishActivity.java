package com.example.food_ordering_mobile_app.ui.customer.dish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.SideDishAdapter;
import com.example.food_ordering_mobile_app.models.dish.Topping;

import java.util.ArrayList;
import java.util.List;

public class DishActivity extends AppCompatActivity {
    private RecyclerView sideDishRecyclerView;
    private SideDishAdapter dishAdapter;
    private List<Topping> sideDishList;
    TextView tvQuantity;
    LinearLayout quantityContainer;
    ImageButton btnIncrease, btnDecrease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dish);

        sideDishRecyclerView = findViewById(R.id.sideDishRecyclerView);
        sideDishRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sideDishList = new ArrayList<>();
        sideDishList.add(new Topping("Cơm them", 10000));
        sideDishList.add(new Topping("Nhiều thịt bò", 15000));

        dishAdapter = new SideDishAdapter(this, sideDishList);
        sideDishRecyclerView.setAdapter(dishAdapter);

        // Change quantity
        quantityContainer = findViewById(R.id.quantityContainer);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);

        btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void openNoteModel(View view) {
        showNoteDialog();
    }

    private void showNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_dialog_note, null);
        builder.setView(dialogView);

        EditText edtNote = dialogView.findViewById(R.id.edtNote);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        ImageButton btnClose = dialogView.findViewById(R.id.btnClose); // Thêm dòng này

        AlertDialog dialog = builder.create();
        dialog.show(); // Hiển thị dialog

        btnConfirm.setOnClickListener(v -> {
            String note = edtNote.getText().toString();
            // Xử lý lưu ý được nhập
            dialog.dismiss();
        });

        btnClose.setOnClickListener(v -> dialog.dismiss()); // Đóng dialog khi nhấn nút
    }

}