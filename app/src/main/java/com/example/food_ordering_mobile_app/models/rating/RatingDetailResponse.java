package com.example.food_ordering_mobile_app.models.rating;

import com.example.food_ordering_mobile_app.models.dish.DishImage;
import com.example.food_ordering_mobile_app.models.order.OrderStore;
import com.example.food_ordering_mobile_app.models.user.User;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.List;

public class RatingDetailResponse {
    @SerializedName("_id")
    private String id;
    private String user;
    private OrderStore store;
    private List<String> dishes;
    private Integer ratingValue;
    private String comment;
    private List<DishImage> images;
    private Timestamp updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public OrderStore getStore() {
        return store;
    }

    public void setStore(OrderStore store) {
        this.store = store;
    }

    public List<String> getDishes() {
        return dishes;
    }

    public void setDishes(List<String> dishes) {
        this.dishes = dishes;
    }

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<DishImage> getImages() {
        return images;
    }

    public void setImages(List<DishImage> images) {
        this.images = images;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
