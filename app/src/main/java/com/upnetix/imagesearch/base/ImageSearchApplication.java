package com.upnetix.imagesearch.base;

import android.app.Application;

import com.upnetix.imagesearch.service.base.ServiceLocator;
import com.upnetix.imagesearch.service.imagedownload.ImageDownloadServiceImpl;
import com.upnetix.imagesearch.service.imagedownload.IImageDownloadService;
import com.upnetix.imagesearch.service.imagesearch.IImageSearchService;
import com.upnetix.imagesearch.service.imagesearch.ImageSearchServiceImpl;

public class ImageSearchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ServiceLocator.bindCustomServiceImplementation(IImageSearchService.class, ImageSearchServiceImpl.class);
        ServiceLocator.bindCustomServiceImplementation(IImageDownloadService.class, ImageDownloadServiceImpl.class);
    }
}
