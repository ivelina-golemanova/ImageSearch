package com.upnetix.imagesearch.service.imagedownload;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.upnetix.imagesearch.service.base.IImageCache;

public class ImageMemoryCache implements IImageCache {

    private LruCache<String, Bitmap> memoryCache;

    public ImageMemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    @Override
    public Bitmap getBitmapFromMemoryCache(String key) {
        return memoryCache.get(key);
    }

}
