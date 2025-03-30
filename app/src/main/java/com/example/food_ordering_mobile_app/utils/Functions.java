package com.example.food_ordering_mobile_app.utils;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.CategoryWithStores;
import com.example.food_ordering_mobile_app.models.store.Store;

import java.util.*;

public class Functions {
    public static List<CategoryWithStores> groupStoresByCategory(List<Store> stores) {
        Map<String, CategoryWithStores> groupedStores = new HashMap<>();

        for (Store store : stores) {
            for (FoodType category : store.getStoreCategory()) {
                // Nếu danh mục chưa tồn tại trong map, tạo mới
                groupedStores.putIfAbsent(category.getId(), new CategoryWithStores());
                CategoryWithStores cs = groupedStores.get(category.getId());

                // Gán thông tin danh mục nếu chưa có
                if (cs.getCategory() == null) {
                    cs.setCategory(category);
                    cs.setStores(new ArrayList<>()); // Khởi tạo danh sách stores
                }

                // Thêm cửa hàng vào danh sách
                cs.getStores().add(store);
            }
        }

        // Chuyển đổi Map thành List<CategoryWithStores>
        return new ArrayList<>(groupedStores.values());
    }
}
