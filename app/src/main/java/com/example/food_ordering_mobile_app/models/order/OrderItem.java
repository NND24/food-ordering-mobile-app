package com.example.food_ordering_mobile_app.models.order;

import com.example.food_ordering_mobile_app.models.dish.Dish;

import java.io.Serializable;
import java.util.List;

public class OrderItem implements Serializable {
    private Dish dish;
    private Integer quantity;
    private List<OrderTopping> toppings;

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<OrderTopping> getToppings() {
        return toppings;
    }

    public void setToppings(List<OrderTopping> toppings) {
        this.toppings = toppings;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "dish=" + dish +
                ", quantity=" + quantity +
                ", toppings=" + toppings +
                '}';
    }
}
