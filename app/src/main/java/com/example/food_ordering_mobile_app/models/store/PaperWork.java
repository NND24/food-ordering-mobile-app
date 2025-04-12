package com.example.food_ordering_mobile_app.models.store;

import com.example.food_ordering_mobile_app.models.Image;

import java.io.Serializable;
import java.util.List;

public class PaperWork implements Serializable {
    private Image IC_front;
    private Image IC_back;
    private Image businessLicense;
    private List<Image> storePicture;

    public Image getIC_front() {
        return IC_front;
    }

    public void setIC_front(Image IC_front) {
        this.IC_front = IC_front;
    }

    public Image getIC_back() {
        return IC_back;
    }

    public void setIC_back(Image IC_back) {
        this.IC_back = IC_back;
    }

    public Image getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(Image businessLicense) {
        this.businessLicense = businessLicense;
    }

    public List<Image> getStorePicture() {
        return storePicture;
    }

    public void setStorePicture(List<Image> storePicture) {
        this.storePicture = storePicture;
    }

    @Override
    public String toString() {
        return "PaperWork{" +
                "IC_front=" + IC_front +
                ", IC_back=" + IC_back +
                ", businessLicense=" + businessLicense +
                ", storePicture=" + storePicture +
                '}';
    }
}
