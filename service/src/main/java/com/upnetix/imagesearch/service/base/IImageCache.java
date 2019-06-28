package com.upnetix.imagesearch.service.base;

import android.graphics.Bitmap;

public interface IImageCache {

    void addBitmapToMemoryCache(String key, Bitmap bitmap);

    Bitmap getBitmapFromMemoryCache(String key);
}
