package com.upnetix.imagesearch;

import androidx.lifecycle.Observer;

import com.upnetix.imagesearch.imagesearch.ImageSearchViewModel;
import com.upnetix.imagesearch.service.base.ICallback;
import com.upnetix.imagesearch.service.imagesearch.IImageSearchService;
import com.upnetix.imagesearch.service.imagesearch.ImageSearchServiceImpl;
import com.upnetix.imagesearch.service.imagesearch.SearchResult;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ImageSearchViewModelUnitTest {

    private ImageSearchViewModel viewModel;
    private IImageSearchService imageSearchServiceMock;

    @Before
    public void setup() {
        imageSearchServiceMock = mock(ImageSearchServiceImpl.class);
        viewModel = new ImageSearchViewModel();
        viewModel.setService(imageSearchServiceMock);
    }

    @Test
    public void searchForImages_searchWordIsEmpty() {
        String searchWord = "";

        //mocked observer
        Observer<SearchResult> searchResultObserver = mock(Observer.class);
        viewModel.getSearchResult().observeForever(searchResultObserver);

        viewModel.searchForImages(searchWord);
        verify(searchResultObserver).onChanged(null);

        viewModel.getSearchResult().removeObserver(searchResultObserver);
    }

    @Test
    public void searchForImages_searchWordIsNull() {
        String searchWord = null;

        viewModel.searchForImages(searchWord);

        verify(imageSearchServiceMock, never()).searchImages(eq(searchWord), anyInt(), any(ICallback.class));
    }

    @Test
    public void searchForImages_searchWordIsCorrect() {
        String searchWord = "cat";

        viewModel.searchForImages(searchWord);

        verify(imageSearchServiceMock).searchImages(eq(searchWord), anyInt(), any(ICallback.class));
    }
}
