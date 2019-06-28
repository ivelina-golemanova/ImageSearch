package com.upnetix.imagesearch.listeners;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    public static final int PRELOAD_ITEMS = 20;
    private GridLayoutManager layoutManager;
    private int itemsOnPage;

    protected abstract void loadMoreItems();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

    public PaginationScrollListener(GridLayoutManager layoutManager, int itemsPerPage) {
        this.layoutManager = layoutManager;
        this.itemsOnPage = itemsPerPage;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - PRELOAD_ITEMS
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }
    }

    public void changeItemsOnPage(int itemsOnPage) {
        this.itemsOnPage = itemsOnPage;
    }

    public void addItemsOnPage(int itemsOnPage) {
        this.itemsOnPage += itemsOnPage;
    }
}
