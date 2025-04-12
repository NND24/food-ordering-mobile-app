package com.example.food_ordering_mobile_app.models.user;

import java.io.Serializable;

public class User extends BaseUser implements Serializable {
    public User() {
    }

    public User(String email, String password) {
        super(email, password);
    }

    public User(String name, String phonenumber, String gender) {
        super(name, phonenumber, gender);
    }

    public User(String name, String email, String phonenumber, String gender, String password) {
        super(name, email, phonenumber, gender, password);
    }
}
