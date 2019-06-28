package com.upnetix.imagesearch.service.imagedownload;

import android.graphics.Bitmap;

import com.upnetix.imagesearch.service.base.ICallback;
import com.upnetix.imagesearch.service.base.IImageCache;
import com.upnetix.imagesearch.service.base.IService;
import com.upnetix.imagesearch.service.imagesearch.Photo;

public interface IImageDownloadService extends IService {

    void downloadImage(Photo photo, int position, ICallback<Bitmap> callback);

    void stopDownloadTask(int position);

    void setCacheService(IImageCache imageCache);
}
