package com.upnetix.imagesearch.service.imagedownload;

import android.graphics.Bitmap;

public class DownloadedImage {

    private Bitmap bitmap;
    private int position;

    public DownloadedImage(Bitmap bitmap, int position) {
        this.bitmap = bitmap;
        this.position = position;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
