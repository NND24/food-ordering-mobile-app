package com.example.food_ordering_mobile_app.models.order;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.Topping;

import java.util.List;

public class OrderItem {
    private OrderDish dish;
    private Integer quantity;
    private List<OrderTopping> toppings;

    public OrderDish getDish() {
        return dish;
    }

    public void setDish(OrderDish dish) {
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
