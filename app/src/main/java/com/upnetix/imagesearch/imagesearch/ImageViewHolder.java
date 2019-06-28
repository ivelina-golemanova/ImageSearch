package com.upnetix.imagesearch.imagesearch;

import android.graphics.Bitmap;

import androidx.recyclerview.widget.RecyclerView;

import com.upnetix.imagesearch.R;
import com.upnetix.imagesearch.databinding.ItemImageBinding;
import com.upnetix.imagesearch.service.base.ICallback;
import com.upnetix.imagesearch.service.imagedownload.IImageDownloadService;
import com.upnetix.imagesearch.service.imagesearch.Photo;

class ImageViewHolder extends RecyclerView.ViewHolder {

    private ItemImageBinding binding;
    private IImageDownloadService downloadService;
    private int position;

    ImageViewHolder(ItemImageBinding binding, IImageDownloadService downloadService) {
        super(binding.getRoot());
        this.binding = binding;
        this.downloadService = downloadService;
    }

    void showPhoto(Photo photo, int position) {
        this.position = position;
        downloadService.downloadImage(photo, position, new ICallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap model) {
                updatePhoto(model);
            }

            @Override
            public void onError(String error) {
                //show a placeholder
            }
        });
    }

    void stopPhotoDownload() {
        downloadService.stopDownloadTask(position);

        //remove current image and add placeholder
        binding.ivImage.setImageBitmap(null);
        binding.ivImage.setImageResource(R.drawable.ic_launcher_background);
    }

    private void updatePhoto(Bitmap bitmap) {
        binding.ivImage.setImageBitmap(bitmap);
    }

}
