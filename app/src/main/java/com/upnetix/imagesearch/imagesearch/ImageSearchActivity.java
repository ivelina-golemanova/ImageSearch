package com.upnetix.imagesearch.imagesearch;

import com.upnetix.imagesearch.R;
import com.upnetix.imagesearch.base.BaseActivity;
import com.upnetix.imagesearch.databinding.ActivityImageSearchBinding;

public class ImageSearchActivity extends BaseActivity<ActivityImageSearchBinding, ImageSearchViewModel> {

    @Override
    protected Class<ImageSearchViewModel> getViewModelClass() {
        return ImageSearchViewModel.class;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_search;
    }
}
