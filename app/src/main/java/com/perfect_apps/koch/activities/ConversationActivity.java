package com.perfect_apps.koch.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.perfect_apps.koch.R;
import com.perfect_apps.koch.adapters.ConversationAdapter;
import com.perfect_apps.koch.interfaces.OnLoadMoreListener;
import com.perfect_apps.koch.models.ConversationItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationActivity extends AppCompatActivity {
    private static final String TAG = "ConversationActivity";

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @BindView(R.id.toolbar) Toolbar toolbar;

    private int pageCount = 1;
    // add listener for loading more view
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER;
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected ConversationAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<ConversationItem> mDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);
        setToolbar();
        setupRecyclerView();

        getConversationMessages();

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);

        /*
        * hide title
        * */
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        TextView tv = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv.setTypeface(font);
    }

    private void setupRecyclerView() {
        // initialize mDataSet
        mDataSet = new ArrayList<>();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mAdapter = new ConversationAdapter(this, mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (lastVisibleItem == 0
                        && !mAdapter.isLoading
                        && totalItemCount > visibleThreshold) {
                    if (mAdapter.mOnLoadMoreListener != null) {
                        mAdapter.mOnLoadMoreListener.onLoadMore();
                    }
                    mAdapter.isLoading = true;
                }
            }
        });

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e(TAG, "Load More");
                pageCount++;
                mDataSet.add(0, null);
                mAdapter.notifyItemInserted(0);

                // loadMoreData
                getConversationMessages();
            }
        });
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findLastCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setStackFromEnd(true);
                mLayoutManager = linearLayoutManager;
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private void getConversationMessages(){
        if(pageCount > 1 && mDataSet.size() != 0){
            mDataSet.remove(0);
            mAdapter.isLoading = false;
            mAdapter.notifyItemRemoved(0);
        }

        ConversationItem conversationItem = new ConversationItem(null, "hello", true, "mostafa", 0, null, null, null);
        for (int i = 0; i<20 ; i++){
            mDataSet.add(conversationItem);
            mAdapter.notifyDataSetChanged();
        }
    }

}
