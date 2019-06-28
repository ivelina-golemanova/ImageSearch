package com.upnetix.imagesearch.service.imagesearch;

import com.upnetix.imagesearch.service.base.ICallback;
import com.upnetix.imagesearch.service.base.IService;

public interface IImageSearchService extends IService {

    void searchImages(String searchWord, int page, ICallback<SearchResult> result);
}
