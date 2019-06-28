package com.upnetix.imagesearch.imagesearch;

import android.graphics.Bitmap;

import androidx.recyclerview.widget.RecyclerView;

import com.upnetix.imagesearch.databinding.ItemImageBinding;

public class ImageViewHolder extends RecyclerView.ViewHolder {

    private ItemImageBinding binding;

    public ImageViewHolder(ItemImageBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void updatePhoto(Bitmap bitmap) {
        binding.ivImage.setImageBitmap(bitmap);
    }

}
