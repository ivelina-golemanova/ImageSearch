package com.upnetix.imagesearch.imagesearch;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.upnetix.imagesearch.R;
import com.upnetix.imagesearch.base.BaseActivity;
import com.upnetix.imagesearch.databinding.ActivityImageSearchBinding;
import com.upnetix.imagesearch.listeners.PaginationScrollListener;
import com.upnetix.imagesearch.service.base.ServiceLocator;
import com.upnetix.imagesearch.service.imagedownload.IDownloadService;
import com.upnetix.imagesearch.service.imagesearch.IImageSearchService;
import com.upnetix.imagesearch.service.imagesearch.Photo;
import com.upnetix.imagesearch.service.imagesearch.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class ImageSearchActivity extends BaseActivity<ActivityImageSearchBinding, ImageSearchViewModel> {

    private PaginationScrollListener paginationScrollListener;

    @Override
    protected Class<ImageSearchViewModel> getViewModelClass() {
        return ImageSearchViewModel.class;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_search;
    }

    @Override
    protected void onPrepare() {
        super.onPrepare();

        viewModel.setService(ServiceLocator.get(IImageSearchService.class));

        final ImageAdapter adapter = new ImageAdapter(new ArrayList<Photo>(), ServiceLocator.get(IDownloadService.class));
        binding.rvImages.setAdapter(adapter);
        viewModel.getSearchResult().observe(this, new Observer<SearchResult>() {
            @Override
            public void onChanged(SearchResult searchResult) {
                if (searchResult == null) {
                    adapter.updateData(new ArrayList<Photo>());
                    return;
                }
                adapter.updateData(searchResult.getPhotos());

                if (paginationScrollListener == null) {
                    paginationScrollListener =
                            CreateScrollListener((GridLayoutManager) binding.rvImages.getLayoutManager(), searchResult.getPhotos().size());
                } else {
                    paginationScrollListener.changeItemsOnPage(searchResult.getPhotos().size());
                }
                binding.rvImages.addOnScrollListener(paginationScrollListener);
            }
        });

        viewModel.getNewPhotoData().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                adapter.addData(photos);
                paginationScrollListener.addItemsOnPage(photos.size());
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchWord = binding.etSearch.getText().toString();
                viewModel.searchForImages(searchWord);

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private PaginationScrollListener CreateScrollListener(GridLayoutManager layoutManager, int itemsPerPage) {
        return new PaginationScrollListener(layoutManager, itemsPerPage) {

            @Override
            protected void loadMoreItems() {
                Log.d("TEST", "loadMoreItems");
                viewModel.getNextPagerResult();
            }

            @Override
            public boolean isLastPage() {
                return viewModel.isImagesLastPage();
            }

            @Override
            public boolean isLoading() {
                return viewModel.isPageLoading();
            }
        };
    }
}
