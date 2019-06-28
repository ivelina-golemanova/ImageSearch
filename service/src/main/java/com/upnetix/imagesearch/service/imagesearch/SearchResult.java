package com.upnetix.imagesearch.service.imagesearch;

import com.upnetix.imagesearch.service.base.Result;

import java.util.List;

public class SearchResult {

    private List<Photo> photos;
    private int page;
    private int pages;
    private int perPage;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
}
