package com.example.food_ordering_mobile_app.models.store;

import com.example.food_ordering_mobile_app.models.Image;
import com.example.food_ordering_mobile_app.models.foodType.FoodType;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Store implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private String owner;
    private String description;
    private StoreAddress address;
    private List<FoodType> storeCategory;
    private Image avatar;
    private Image cover;
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

    public List<FoodType> getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(List<FoodType> storeCategory) {
        this.storeCategory = storeCategory;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public Image getCover() {
        return cover;
    }

    public void setCover(Image cover) {
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
