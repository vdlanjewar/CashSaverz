package com.essensys.cashsaverz.helper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
/**
 * Pagination
 * Created by Suleiman19 on 10/15/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */
public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;

    /**
     * Supporting only LinearLayoutManager for now.
     *
     * @param layoutManager
     */
    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

//        if (!isLoading() && !isLastPage()) {
//            if (!isLoading() ) {
//
//                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                    && firstVisibleItemPosition >= 0
//                    && totalItemCount >= getTotalPageCount()) {
//                loadMoreItems();
//            }
//        }
        View view = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        int diff = (view.getBottom() - (recyclerView.getHeight() + recyclerView.getScrollY()));
        // if diff is zero, then the bottom has been reached
        if (diff == 0) {
            loadMoreItems();
        }

    }


    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();
    //
    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}

