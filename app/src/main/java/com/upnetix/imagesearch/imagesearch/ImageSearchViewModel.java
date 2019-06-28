package com.upnetix.imagesearch.imagesearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.upnetix.imagesearch.base.BaseViewModel;
import com.upnetix.imagesearch.service.base.ICallback;
import com.upnetix.imagesearch.service.imagesearch.IImageSearchService;
import com.upnetix.imagesearch.service.imagesearch.Photo;
import com.upnetix.imagesearch.service.imagesearch.SearchResult;

import java.util.List;

public class ImageSearchViewModel extends BaseViewModel {

    private static final int FIRST_PAGE = 1;

    private IImageSearchService imageService;

    private int currentPage;
    private int totalPages;
    private boolean isPageLoading;
    private String currentSearchWord;

    private MutableLiveData<SearchResult> searchResult;
    private MutableLiveData<List<Photo>> newPhotoData;

    public LiveData<SearchResult> getSearchResult() {
        if (searchResult == null) {
            searchResult = new MutableLiveData<>();
        }
        return searchResult;
    }

    public LiveData<List<Photo>> getNewPhotoData() {
        if (newPhotoData == null) {
            newPhotoData = new MutableLiveData<>();
        }
        return newPhotoData;
    }

    public void setService(IImageSearchService service) {
        imageService = service;
    }

    public void searchForImages(final String searchWord) {
        if (searchWord == null || (currentSearchWord != null && currentSearchWord.equals(searchWord))) {
            return;
        }

        currentSearchWord = searchWord;
        if (currentSearchWord.isEmpty()) {
            searchResult.setValue(null);
            return;
        }

        imageService.searchImages(searchWord, FIRST_PAGE, new ICallback<SearchResult>() {
            @Override
            public void onSuccess(SearchResult model) {
                if (haveFullData(model)) {
                    searchResult.setValue(model);

                    currentPage = model.getPage();
                    totalPages = model.getPages();
                }
            }

            @Override
            public void onError(String error) {
                //display some error to the user
            }
        });
    }

    public void getNextPagerResult() {
        isPageLoading = true;
        currentPage++;
        imageService.searchImages(currentSearchWord, currentPage, new ICallback<SearchResult>() {
            @Override
            public void onSuccess(SearchResult model) {
                isPageLoading = false;
                if (haveFullData(model)) {
                    newPhotoData.setValue(model.getPhotos());
                }
            }

            @Override
            public void onError(String error) {
                //display some error to the user
            }
        });
    }

    public boolean isImagesLastPage() {
        return currentPage == totalPages;
    }

    public boolean isPageLoading() {
        return isPageLoading;
    }

    private boolean haveFullData(SearchResult model) {
        return model != null
                && model.getPhotos() != null && model.getPhotos().size() > 0;
    }
}
