package com.example.food_ordering_mobile_app.models.foodType;

import com.example.food_ordering_mobile_app.models.Image;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodType implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private Image image;

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
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
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
