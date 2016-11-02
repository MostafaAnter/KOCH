package com.perfect_apps.koch.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.activities.RequestDetailActivity;
import com.perfect_apps.koch.activities.SplashActivity;
import com.perfect_apps.koch.models.OrderRequest;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mostafa_anter on 10/14/16.
 */

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<OrderRequest> mDataSet;
    private  Context mContext;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public  class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)CircleImageView avatar;
        @BindView(R.id.progressBar)ProgressBar progressBar;
        @BindView(R.id.timestamp)TextView timestamp;
        @BindView(R.id.name)TextView name;
        @BindView(R.id.container)
        LinearLayout container;

        public CircleImageView getAvatar() {
            return avatar;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public TextView getTimestamp() {
            return timestamp;
        }

        public TextView getName() {
            return name;
        }

        public LinearLayout getContainer() {
            return container;
        }

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    mContext.startActivity(new Intent(mContext, RequestDetailActivity.class));
                }
            });

        }


    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public OrderItemsAdapter(Context mContext, List<OrderRequest> dataSet) {
        this.mContext = mContext;
        mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_row_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element

        if (Integer.valueOf(mDataSet.get(position).getStatus()) == 0){
            viewHolder.getContainer().setBackgroundResource(R.color.gray_btn_bg_color);
        }


        // with that element
        Typeface fontBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/bold.ttf");
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal.ttf");

        // set time stamp
        viewHolder.getTimestamp().setText(mDataSet.get(position).getUpdated_at());
        viewHolder.getTimestamp().setTypeface(font);

        if (new KochPrefStore(mContext).getPreferenceValue(Constants.userGroupId).equalsIgnoreCase("3")) {
            // set user name
            viewHolder.getName().setText(mDataSet.get(position).getProviderName());
            viewHolder.getName().setTypeface(fontBold);

            // populate mainImage
            Glide.with(mContext)
                    .load(mDataSet.get(position).getProviderImage())
                    .placeholder(R.color.gray_btn_bg_color)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            viewHolder.getProgressBar().setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .crossFade()
                    .dontAnimate()
                    .thumbnail(0.2f)
                    .into(viewHolder.getAvatar());
        } else {

            // set user name
            viewHolder.getName().setText(mDataSet.get(position).getClientName());
            viewHolder.getName().setTypeface(fontBold);

            // populate mainImage
            Glide.with(mContext)
                    .load(mDataSet.get(position).getClientImage())
                    .placeholder(R.color.gray_btn_bg_color)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            viewHolder.getProgressBar().setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .crossFade()
                    .dontAnimate()
                    .thumbnail(0.2f)
                    .into(viewHolder.getAvatar());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}