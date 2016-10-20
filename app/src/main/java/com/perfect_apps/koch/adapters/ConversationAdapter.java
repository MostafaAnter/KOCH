package com.perfect_apps.koch.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.perfect_apps.koch.R;
import com.perfect_apps.koch.interfaces.OnLoadMoreListener;
import com.perfect_apps.koch.models.ConversationItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;



/**
 * Created by mostafa on 15/04/16.
 */
public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public OnLoadMoreListener mOnLoadMoreListener;
    public boolean isLoading;


    private static final String TAG = "CustomAdapter";
    private static Context mContext;
    private List<ConversationItem> mDataSet;
    private int SELF = 100;

    /**
     * Initialize the constructor of the Adapter.
     *
     * @param mDataSet String[] containing the data to populate views to be used by RecyclerView.
     * @param mContext Context hold context
     */
    public ConversationAdapter(Context mContext, List<ConversationItem> mDataSet) {
        this.mDataSet = mDataSet;
        this.mContext = mContext;
    }

    /**
     * Provide a reference to the type of views (custom ViewHolder)
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.conversation_avatar)
        CircleImageView avatarImage;
        @BindView(R.id.showFlag)
        ImageView showFlag;

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public TextView getMessage() {
            return message;
        }

        public CircleImageView getAvatarImage() {
            return avatarImage;
        }

        public ImageView getShowFlag() {
            return showFlag;
        }

        public ItemViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
            ButterKnife.bind(this, v);
        }

    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSet.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else if (true/*mDataSet.get(position)*/) {
            return SELF;
        } else {

            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_yourself_item, viewGroup, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_item, viewGroup, false);
            return new LoadingViewHolder(view);
        } else if (viewType == SELF) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_self_item, viewGroup, false);
            return new ItemViewHolder(view);
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            Log.d(TAG, "Element " + position + " set.");
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;

            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            Typeface makOnWayFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal.ttf");

        }else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        isLoading = false;
    }

}
