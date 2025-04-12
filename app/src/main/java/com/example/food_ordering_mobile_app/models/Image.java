package com.example.food_ordering_mobile_app.models;

import java.io.Serializable;

public class Image implements Serializable {
    private String filePath;
    private String url;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserAvatar{" +
                "filePath='" + filePath + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
