package com.upnetix.imagesearch.service.imagedownload;

import com.upnetix.imagesearch.service.base.ICallback;
import com.upnetix.imagesearch.service.base.IService;
import com.upnetix.imagesearch.service.imagesearch.Photo;

public interface IDownloadService extends IService {

    void downloadImage(Photo photo, int position, ICallback<DownloadedImage> callback);
}
