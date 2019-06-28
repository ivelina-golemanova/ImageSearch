package com.upnetix.imagesearch.imagesearch;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upnetix.imagesearch.databinding.ItemImageBinding;
import com.upnetix.imagesearch.service.base.ICallback;
import com.upnetix.imagesearch.service.imagedownload.DownloadedImage;
import com.upnetix.imagesearch.service.imagedownload.IDownloadService;
import com.upnetix.imagesearch.service.imagesearch.Photo;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<Photo> data;
    private IDownloadService downloadService;
    private RecyclerView recyclerView;

    ImageAdapter(List<Photo> data, IDownloadService downloadService) {
        this.data = data;
        this.downloadService = downloadService;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageBinding binding = ItemImageBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        Photo photo = data.get(position);
        downloadService.downloadImage(photo, position, new ICallback<DownloadedImage>() {
            @Override
            public void onSuccess(DownloadedImage model) {

                if (recyclerView == null) {
                    return;
                }
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(model.getPosition());
                if (viewHolder != null) {
                    ((ImageViewHolder) viewHolder).updatePhoto(model.getBitmap());
                }
            }

            @Override
            public void onError(String error) {
                //show a placeholder
            }
        });
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
