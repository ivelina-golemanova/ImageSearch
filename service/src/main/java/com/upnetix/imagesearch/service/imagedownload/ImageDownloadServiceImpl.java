package com.upnetix.imagesearch.service.imagedownload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.upnetix.imagesearch.service.base.ICallback;
import com.upnetix.imagesearch.service.base.IImageCache;
import com.upnetix.imagesearch.service.base.Result;
import com.upnetix.imagesearch.service.imagesearch.Photo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageDownloadServiceImpl implements IImageDownloadService {

    private static final String url = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";

    private IImageCache imageCache;
    private Map<Integer, AsyncTask> tasks = new HashMap<>();

    @Override
    public void downloadImage(Photo photo, int position, ICallback<Bitmap> callback) {
        String fullUrl = String.format(url, photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret());

        if (imageCache != null && imageCache.getBitmapFromMemoryCache(fullUrl) != null) {
            Bitmap bitmap = imageCache.getBitmapFromMemoryCache(fullUrl);
            callback.onSuccess(bitmap);
        } else {
            AsyncTask downloadTask = new DownloadImageTask(callback, imageCache).execute(fullUrl);
            tasks.put(position, downloadTask);
        }
    }

    @Override
    public void stopDownloadTask(int position) {
        AsyncTask task = tasks.get(position);
        if (task != null) {
            task.cancel(true);
        }
        tasks.remove(position);
    }

    @Override
    public void setCacheService(IImageCache imageCache) {
        this.imageCache = imageCache;
    }

    private static class DownloadImageTask extends AsyncTask<String, Integer, Result<Bitmap>> {

        private ICallback<Bitmap> callback;
        private IImageCache imageCache;

        DownloadImageTask(ICallback<Bitmap> callback, IImageCache imageCache) {
            this.callback = callback;
            this.imageCache = imageCache;
        }

        @Override
        protected Result<Bitmap> doInBackground(String... strings) {

            Result<Bitmap> result = null;
            if (!isCancelled()) {
                InputStream stream = null;
                try {
                    String urlString = strings[0];
                    URL url = new URL(urlString);
                    stream = (InputStream) url.getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);

                    if(bitmap != null) {
                        if(imageCache != null){
                            imageCache.addBitmapToMemoryCache(urlString, bitmap);
                        }
                        result = new Result<>(bitmap);
                    } else {
                        throw new Exception("Something went wrong with downloading the image");
                    }
                } catch (Exception e) {
                    result = new Result<>(e.getMessage());
                } finally {
                    try {
                        if (stream != null) {
                            stream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(Result<Bitmap> result) {
            super.onPostExecute(result);
            if (result != null && callback != null) {
                if (result.getError() != null) {
                    callback.onError(result.getError());
                } else if (result.getModel() != null) {
                    callback.onSuccess(result.getModel());
                }
            }
        }
    }
}
