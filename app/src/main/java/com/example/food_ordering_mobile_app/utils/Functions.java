package com.example.food_ordering_mobile_app.utils;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishGroupByCategory;
import com.example.food_ordering_mobile_app.models.dish.FoodCategory;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.StoreGroupByCategory;
import com.example.food_ordering_mobile_app.models.store.Store;

import java.util.*;

public class Functions {
    public static List<StoreGroupByCategory> groupStoresByCategory(List<Store> stores) {
        Map<String, StoreGroupByCategory> groupedStores = new HashMap<>();

        for (Store store : stores) {
            for (FoodType category : store.getStoreCategory()) {
                // Nếu danh mục chưa tồn tại trong map, tạo mới
                groupedStores.putIfAbsent(category.getId(), new StoreGroupByCategory());
                StoreGroupByCategory cs = groupedStores.get(category.getId());

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

    public static List<DishGroupByCategory> groupDishesByCategory(List<Dish> dishes) {
        Map<String, DishGroupByCategory> groupedDishes = new HashMap<>();

        for (Dish dish : dishes) {
            FoodCategory category = dish.getCategory(); // Lấy đối tượng category

            if (category != null) {
                groupedDishes.putIfAbsent(category.getId(), new DishGroupByCategory());
                DishGroupByCategory cs = groupedDishes.get(category.getId());

                if (cs.getCategory() == null) {
                    cs.setCategory(category);
                    cs.setDishes(new ArrayList<>());
                }

                cs.getDishes().add(dish);
            }
        }

        return new ArrayList<>(groupedDishes.values());
    }

    private static final double EARTH_RADIUS = 6371; // Bán kính trái đất tính bằng km

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Chuyển đổi độ sang radian
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Áp dụng công thức Haversine
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Đơn vị km
    }

    private String fixNonStandardJson(String rawJson) {
        return rawJson.replaceAll("new ObjectId\\(\"(.*?)\"\\)", "\"$1\"");
    }

}
