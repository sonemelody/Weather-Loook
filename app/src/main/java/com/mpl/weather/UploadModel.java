package com.mpl.weather;

public class UploadModel {
    private String imageUrl;

    UploadModel(){

    }

    public UploadModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
