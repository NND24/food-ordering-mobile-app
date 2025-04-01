package com.example.food_ordering_mobile_app.models.location;

public class SuggestionItem {
    private String placeName;
    private double latitude;
    private double longitude;

    public SuggestionItem(String placeName, double latitude, double longitude) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
