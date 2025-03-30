package com.example.food_ordering_mobile_app.models.store;

import java.util.Arrays;

public class PaperWork {
    private StoreImage IC_front;
    private StoreImage IC_back;
    private StoreImage businessLicense;
    private StoreImage[] storePicture;

    public StoreImage getIC_front() {
        return IC_front;
    }

    public void setIC_front(StoreImage IC_front) {
        this.IC_front = IC_front;
    }

    public StoreImage getIC_back() {
        return IC_back;
    }

    public void setIC_back(StoreImage IC_back) {
        this.IC_back = IC_back;
    }

    public StoreImage getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(StoreImage businessLicense) {
        this.businessLicense = businessLicense;
    }

    public StoreImage[] getStorePicture() {
        return storePicture;
    }

    public void setStorePicture(StoreImage[] storePicture) {
        this.storePicture = storePicture;
    }

    @Override
    public String toString() {
        return "PaperWork{" +
                "IC_front=" + IC_front +
                ", IC_back=" + IC_back +
                ", businessLicense=" + businessLicense +
                ", storePicture=" + Arrays.toString(storePicture) +
                '}';
    }
}
