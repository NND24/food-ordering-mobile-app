package com.example.food_ordering_mobile_app.models.order;

import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.example.food_ordering_mobile_app.models.store.PaperWork;
import com.example.food_ordering_mobile_app.models.store.StoreAddress;
import com.example.food_ordering_mobile_app.models.store.StoreImage;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderStore {
    @SerializedName("_id")
    private String id;
    private String name;
    private String owner;
    private String description;
    private StoreAddress address;
    private List<String> storeCategory;
    private StoreImage avatar;
    private StoreImage cover;
    private PaperWork paperWork;
    private Double avgRating;
    private Integer amountRating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StoreAddress getAddress() {
        return address;
    }

    public void setAddress(StoreAddress address) {
        this.address = address;
    }

    public List<String> getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(List<String> storeCategory) {
        this.storeCategory = storeCategory;
    }

    public StoreImage getAvatar() {
        return avatar;
    }

    public void setAvatar(StoreImage avatar) {
        this.avatar = avatar;
    }

    public StoreImage getCover() {
        return cover;
    }

    public void setCover(StoreImage cover) {
        this.cover = cover;
    }

    public PaperWork getPaperWork() {
        return paperWork;
    }

    public void setPaperWork(PaperWork paperWork) {
        this.paperWork = paperWork;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getAmountRating() {
        return amountRating;
    }

    public void setAmountRating(Integer amountRating) {
        this.amountRating = amountRating;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", description='" + description + '\'' +
                ", address=" + address +
                ", storeCategory=" + storeCategory +
                ", avatar=" + avatar +
                ", cover=" + cover +
                ", paperWork=" + paperWork +
                ", avgRating=" + avgRating +
                ", amountRating=" + amountRating +
                '}';
    }
}
