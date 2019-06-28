package com.upnetix.imagesearch.service.imagesearch;

import android.os.AsyncTask;

import com.upnetix.imagesearch.service.base.ICallback;
import com.upnetix.imagesearch.service.base.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ImageSearchServiceImpl implements IImageSearchService {

    private final static String url
            = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1&text=%s&page=%s";

    @Override
    public void searchImages(String searchWord, int page, ICallback<SearchResult> callback) {
        String fullUrl = String.format(url, searchWord, page);
        new ImageSearchTask(callback).execute(fullUrl);
    }

    private static class ImageSearchTask extends AsyncTask<String, Integer, Result<SearchResult>> {

        private ICallback<SearchResult> callback;

        ImageSearchTask(ICallback<SearchResult> callback) {
            this.callback = callback;
        }

        @Override
        protected Result<SearchResult> doInBackground(String... urls) {

            Result<SearchResult> result = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    String searchResponse = requestSearchResult(url);
                    SearchResult searchResult = parseSearchResponse(searchResponse);
                    if (searchResult != null) {
                        result = new Result<>(searchResult);
                    } else {
                        throw new IOException("No response received");
                    }
                } catch (Exception e) {
                    result = new Result<>(e.getMessage());
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Result<SearchResult> searchResult) {
            super.onPostExecute(searchResult);

            if (searchResult != null && callback != null) {
                if (searchResult.getError() != null && !searchResult.getError().isEmpty()) {
                    callback.onError(searchResult.getError());
                } else if (searchResult.getModel() != null) {
                    callback.onSuccess(searchResult.getModel());
                }
            }
        }

        private String requestSearchResult(URL url) throws IOException {

            InputStream stream = null;
            HttpsURLConnection connection = null;
            String result = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                stream = connection.getInputStream();
                if (stream != null) {
                    result = readStream(stream);
                }
            } finally {
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        private SearchResult parseSearchResponse(String searchResponse) throws JSONException {
            SearchResult searchResult = new SearchResult();

            JSONObject object = new JSONObject(searchResponse);

            JSONObject photosObject = object.getJSONObject("photos");
            searchResult.setPage(photosObject.getInt("page"));
            searchResult.setPages(photosObject.getInt("pages"));
            searchResult.setPerPage(photosObject.getInt("perpage"));

            //get the photos list
            List<Photo> photos = new ArrayList<>();
            JSONArray photosArray = photosObject.getJSONArray("photo");
            for (int i = 0; i < photosArray.length(); i++) {
                Photo photo = new Photo();
                JSONObject photoObject = photosArray.getJSONObject(i);
                photo.setId(photoObject.getString("id"));
                photo.setServer(photoObject.getString("server"));
                photo.setFarm(photoObject.getString("farm"));
                photo.setSecret(photoObject.getString("secret"));

                photos.add(photo);
            }
            searchResult.setPhotos(photos);

            return searchResult;
        }

        /**
         * Converts the contents of an InputStream to a String.
         */
        String readStream(InputStream stream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

    }
}
