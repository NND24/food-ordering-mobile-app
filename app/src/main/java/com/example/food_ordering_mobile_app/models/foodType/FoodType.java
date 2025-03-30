package com.example.food_ordering_mobile_app.models.foodType;

import com.google.gson.annotations.SerializedName;

public class FoodType {
    @SerializedName("_id")
    private String id;
    private String name;
    private FoodTypeImage image;

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
    public FoodTypeImage getImage() {
        return image;
    }

    public void setImage(FoodTypeImage image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "FoodType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image=" + image +
                '}';
    }
}
