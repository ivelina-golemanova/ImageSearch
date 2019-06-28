package com.upnetix.imagesearch.base;

import android.app.Application;

import com.upnetix.imagesearch.service.base.ServiceLocator;
import com.upnetix.imagesearch.service.imagedownload.DownloadServiceImpl;
import com.upnetix.imagesearch.service.imagedownload.IDownloadService;
import com.upnetix.imagesearch.service.imagesearch.IImageSearchService;
import com.upnetix.imagesearch.service.imagesearch.ImageSearchServiceImpl;

public class ImageSearchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ServiceLocator.bindCustomServiceImplementation(IImageSearchService.class, ImageSearchServiceImpl.class);
        ServiceLocator.bindCustomServiceImplementation(IDownloadService.class, DownloadServiceImpl.class);
    }
}
