package com.upnetix.imagesearch.service.imagedownload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.upnetix.imagesearch.service.base.ICallback;
import com.upnetix.imagesearch.service.base.Result;
import com.upnetix.imagesearch.service.imagesearch.Photo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DownloadServiceImpl implements IDownloadService {

    private static final String url = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";
    private Map<Integer, AsyncTask> tasks = new HashMap<>();

    @Override
    public void downloadImage(Photo photo, int position, ICallback<DownloadedImage> callback) {
        String fullUrl = String.format(url, photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret());

        AsyncTask downloadTask = new DownloadImageTask(callback, position).execute(fullUrl);
        ;
        tasks.put(position, downloadTask);
    }

    @Override
    public void stopDownloadTask(int position) {
        AsyncTask task = tasks.get(position);
        if (task != null) {
            task.cancel(true);
        }
        tasks.remove(position);
    }

    private static class DownloadImageTask extends AsyncTask<String, Integer, Result<DownloadedImage>> {

        private ICallback<DownloadedImage> callback;
        private int position;

        DownloadImageTask(ICallback<DownloadedImage> callback, int position) {
            this.callback = callback;
            this.position = position;
        }

        @Override
        protected Result<DownloadedImage> doInBackground(String... strings) {

            Result<DownloadedImage> result = null;
            if (!isCancelled()) {
                InputStream stream = null;
                try {
                    String urlString = strings[0];
                    URL url = new URL(urlString);
                    stream = (InputStream) url.getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    DownloadedImage downloadedImage = new DownloadedImage(bitmap, position);
                    result = new Result<>(downloadedImage);
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
        protected void onPostExecute(Result<DownloadedImage> result) {
            super.onPostExecute(result);
            if (result != null && callback != null) {
                if (result.getError() != null) {
                    callback.onError(result.getError());
                } else if (result.getModel().getBitmap() != null) {
                    callback.onSuccess(result.getModel());
                }
            }
        }
    }
}
