package com.example.food_ordering_mobile_app.models.rating;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishImage;
import com.example.food_ordering_mobile_app.models.user.User;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.List;

public class Rating {
    @SerializedName("_id")
    private String id;
    private User user;
    private String store;
    private List<DishRating> dishes;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public List<DishRating> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishRating> dishes) {
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
