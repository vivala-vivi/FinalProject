package com.example.vtradeversion3;

import android.widget.ImageView;

public class NewsData {
    String title;
    String description;
    String url;
    String urlToImage;
    ImageView imageView;

    public NewsData(String title, String description, String url, String urlToImage, ImageView imageView) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.imageView = imageView;
    }
}
