package com.zoho.mohammadrajabi.socialnetwork.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private int visibleThreshold;
    private int firstVisibleItemPosition, totalItemCount, visibleItemCount;
    private int currentPage = 1;
    private boolean loading = true;

    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener.scrollListener scrollListener;

    public EndlessRecyclerOnScrollListener(int visibleThreshold,LinearLayoutManager linearLayoutManager, EndlessRecyclerOnScrollListener.scrollListener scrollListener) {
        this.visibleThreshold = visibleThreshold;
        this.linearLayoutManager = linearLayoutManager;
        this.scrollListener = scrollListener;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = linearLayoutManager.getItemCount();

        if(loading) {
            if(totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if(!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
            currentPage++;

            scrollListener.onLoadMore(currentPage);

            loading = true;
        }
    }

    public interface scrollListener{
         void onLoadMore(int currentPage);
    }
}
