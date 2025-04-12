package com.example.food_ordering_mobile_app.models.user;

import com.example.food_ordering_mobile_app.models.Image;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BaseUser implements Serializable {
    @SerializedName("_id")
    protected String id;
    protected String name;
    protected String email;
    protected String phonenumber;
    protected String password;
    protected String gender;
    protected List<String> role;
    protected Image avatar;
    @SerializedName("token")
    protected String accessToken;
    protected boolean isGoogleLogin;

    public BaseUser() {
    }

    public BaseUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public BaseUser(String name, String phonenumber, String gender) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.gender = gender;
    }

    public BaseUser(String name, String email, String phonenumber, String gender, String password) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.gender = gender;
        this.password = password;
    }

    // Getters and setters...


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isGoogleLogin() {
        return isGoogleLogin;
    }

    public void setGoogleLogin(boolean googleLogin) {
        isGoogleLogin = googleLogin;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", password='" + (password != null ? "******" : "null") + '\'' +
                ", gender='" + gender + '\'' +
                ", role=" + role +
                ", avatar=" + avatar +
                ", accessToken='" + (accessToken != null ? "****" : "null") + '\'' +
                ", isGoogleLogin=" + isGoogleLogin +
                '}';
    }

    // Getters and setters ở đây nếu cần tái sử dụng
}
