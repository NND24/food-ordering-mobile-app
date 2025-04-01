package com.example.food_ordering_mobile_app.models.dish;

import java.util.List;

public class ToppingGroupResponse {
    private String success;
    private List<ToppingGroup> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ToppingGroup> getData() {
        return data;
    }

    public void setData(List<ToppingGroup> data) {
        this.data = data;
    }
}
