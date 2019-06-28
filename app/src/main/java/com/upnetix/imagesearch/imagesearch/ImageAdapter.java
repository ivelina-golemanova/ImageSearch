package com.upnetix.imagesearch.imagesearch;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upnetix.imagesearch.databinding.ItemImageBinding;
import com.upnetix.imagesearch.service.imagedownload.IImageDownloadService;
import com.upnetix.imagesearch.service.imagesearch.Photo;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<Photo> data;
    private IImageDownloadService downloadService;

    ImageAdapter(List<Photo> data, IImageDownloadService downloadService) {
        this.data = data;
        this.downloadService = downloadService;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageBinding binding = ItemImageBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ImageViewHolder(binding, downloadService);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        Photo photo = data.get(position);
        holder.showPhoto(photo, position);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ImageViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.stopPhotoDownload();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void updateData(List<Photo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    void addData(List<Photo> newData) {
        int startPosition = data.size() - 1;
        data.addAll(newData);
        notifyItemRangeInserted(startPosition, newData.size());
    }
}
