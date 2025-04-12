package com.example.food_ordering_mobile_app.models.user;

import java.io.Serializable;

public class Shipper extends BaseUser implements Serializable {
    public Shipper(String email, String password) {
        super(email, password);
    }

    public Shipper(String name, String phonenumber, String gender) {
        super(name, phonenumber, gender);
    }

    public Shipper(String name, String email, String phonenumber, String gender, String password) {
        super(name, email, phonenumber, gender, password);
    }
}
