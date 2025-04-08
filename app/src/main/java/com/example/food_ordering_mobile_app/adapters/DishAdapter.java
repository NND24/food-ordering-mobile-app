package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.cart.Cart;
import com.example.food_ordering_mobile_app.models.cart.CartItem;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishStore;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.ui.customer.dish.DishActivity;
import com.example.food_ordering_mobile_app.viewmodels.CartViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {
    private Context context;
    private List<DishStore> dishList;
    private OnDishClickListener onDishClickListener;
    Cart currentCart = null;
    private CartViewModel cartViewModel;
    private FragmentActivity activity;

    public interface OnDishClickListener {
        void onDishClick(DishStore dish);
    }

    public DishAdapter(FragmentActivity activity, Context context, List<DishStore> dishList, OnDishClickListener onDishClickListener) {
        this.activity = activity;
        this.context = context;
        this.dishList = dishList != null ? dishList : new ArrayList<>();
        this.onDishClickListener = onDishClickListener;
        this.cartViewModel = new ViewModelProvider(activity).get(CartViewModel.class);
    }

    public void setDishCurrentCart(Cart currentCart) {
        this.currentCart = currentCart;
        notifyDataSetChanged();
    }

    public void updateData(List<DishStore> newDishes) {
        if (newDishes != null) {
            dishList.clear();
            dishList.addAll(newDishes);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false);
        return new ViewHolder(view);
    }

    private void updateCart(DishStore dish, int quantity) {
        Map<String, Object> data = new HashMap<>();
        data.put("storeId", dish.getStore());
        data.put("dishId", dish.getId());
        data.put("quantity", quantity);
        cartViewModel.updateCart(data);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DishStore dish = dishList.get(position);

        holder.name.setText(dish.getName());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.price.setText(String.valueOf(formatter.format(dish.getPrice())));
        holder.description.setText(dish.getDescription());

        String dishImageUrl = dish.getImage() != null ? dish.getImage().getUrl() : null;
        Glide.with(context)
                .asBitmap()
                .load(dishImageUrl)
                .override(80, 80)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.dishAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedDrawable.setCornerRadius(6);
                        holder.dishAvatar.setImageDrawable(roundedDrawable);
                    }
                });

        CartItem matchedItem = null;
        if (currentCart != null && currentCart.getItems() != null) {
            for (CartItem item : currentCart.getItems()) {
                if (item.getDish().getId().equals(dish.getId())) {
                    matchedItem = item;
                    break;
                }
            }
        }

        if (matchedItem != null) {
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.quantityContainer.setVisibility(View.VISIBLE);
            holder.tvQuantity.setText(String.valueOf(matchedItem.getQuantity()));
        } else {
            holder.btnAddToCart.setVisibility(View.VISIBLE);
            holder.quantityContainer.setVisibility(View.GONE);
        }

        boolean[] isAddToCartClicked = {false};

        // Xử lý click cho btnAddToCart
        holder.btnAddToCart.setOnClickListener(v -> {
            if (dish.getToppingGroups().isEmpty()) {
                holder.btnAddToCart.setVisibility(View.GONE);
                holder.quantityContainer.setVisibility(View.VISIBLE);
                holder.tvQuantity.setText("1");
                updateCart(dish, 1);
            } else {
                Intent intent = new Intent(context, DishActivity.class);
                intent.putExtra("dishId", dish.getId());
                context.startActivity(intent);
            }
            isAddToCartClicked[0] = true;
        });

        // Xử lý tăng giảm số lượng (chỉ dùng nếu không có topping)
        if (dish.getToppingGroups().isEmpty()) {
            holder.btnIncrease.setOnClickListener(v -> {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                quantity++;
                holder.tvQuantity.setText(String.valueOf(quantity));
                updateCart(dish, quantity);
            });

            holder.btnDecrease.setOnClickListener(v -> {
                int quantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                if (quantity > 1) {
                    quantity--;
                    holder.tvQuantity.setText(String.valueOf(quantity));
                    updateCart(dish, quantity);
                } else {
                    holder.quantityContainer.setVisibility(View.GONE);
                    holder.btnAddToCart.setVisibility(View.VISIBLE);
                    updateCart(dish, 0);
                }
            });
        } else {
            holder.btnIncrease.setOnClickListener(v -> {
                Intent intent = new Intent(context, DishActivity.class);
                intent.putExtra("dishId", dish.getId());
                context.startActivity(intent);
                isAddToCartClicked[0] = true;
            });

            holder.btnDecrease.setOnClickListener(v -> {
                Intent intent = new Intent(context, DishActivity.class);
                intent.putExtra("dishId", dish.getId());
                context.startActivity(intent);
                isAddToCartClicked[0] = true;
            });
        }

        // Xử lý click toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            if (!isAddToCartClicked[0] && onDishClickListener != null) {
                onDishClickListener.onDishClick(dish);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price, tvQuantity;
        ImageView dishAvatar;
        LinearLayout quantityContainer;
        ImageButton btnIncrease, btnDecrease, btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
            dishAvatar = itemView.findViewById(R.id.dishAvatar);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            quantityContainer = itemView.findViewById(R.id.quantityContainer);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }
}
