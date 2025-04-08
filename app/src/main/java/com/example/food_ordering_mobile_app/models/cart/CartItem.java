package com.example.food_ordering_mobile_app.models.cart;

import com.example.food_ordering_mobile_app.models.dish.Topping;

import java.io.Serializable;
import java.util.List;

public class CartItem implements Serializable {
    private CartDish dish;
    private List<Topping> toppings;
    private Integer quantity;

    public CartDish getDish() {
        return dish;
    }

    public void setDish(CartDish dish) {
        this.dish = dish;
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
