package com.example.comupnaguilarchavez.Entities;

import com.google.gson.annotations.SerializedName;

public class ImageResponse {
    @SerializedName("url")
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
