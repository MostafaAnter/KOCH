package com.perfect_apps.koch.activities;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.perfect_apps.koch.BuildConfig;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.adapters.ConversationAdapter;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.fragment.SendRequestDialog;
import com.perfect_apps.koch.interfaces.OnLoadMoreListener;
import com.perfect_apps.koch.models.ConversationItem;
import com.perfect_apps.koch.parser.JsonParser;
import com.perfect_apps.koch.services.NotificationEvent;
import com.perfect_apps.koch.services.UpdateMessageCountEvent;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.Utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationActivity extends LocalizationActivity implements View.OnClickListener {
    private static final String TAG = "ConversationActivity";
    private static int mStackLevel = 0;

    private static String email;
    private static String password;
    private static String to;
    private static String messageText;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.messageInput)
    EditText editText1;
    @BindView(R.id.send_button)
    ImageView sendButton;

    private int pageCount = 1;
    // add listener for loading more view
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                doNewMessage();
                break;
        }
    }

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER;
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected ConversationAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<ConversationItem> mDataSet;


    // for receive data from bundle
    private Bundle mBundle = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);
        mBundle = this.getIntent().getExtras();
        setToolbar();
        setupRecyclerView();

        getConversationMessages();
        sendButton.setOnClickListener(this);

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
        LinearLayout linearLayout = (LinearLayout) toolbar.findViewById(R.id.send_request);
        if (mBundle != null) {
            tv.setText(mBundle.getString("user_name") != null ? mBundle.getString("user_name") : "");
            tv.setTypeface(font);

            ImageView backIc = (ImageView) toolbar.findViewById(R.id.back);
            backIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            CircleImageView circleImageView = (CircleImageView) toolbar.findViewById(R.id.user_avatar);
            Glide.with(this)
                    .load(mBundle.getString("user_avatar"))
                    .placeholder(R.color.gray_btn_bg_color)
                    .centerCrop()
                    .crossFade()
                    .thumbnail(0.1f)
                    .into(circleImageView);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStackLevel++;
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    Fragment prev1 = getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev1 != null) {
                        ft1.remove(prev1);
                    }
                    ft1.addToBackStack(null);

                    // Create and show the dialog.
                    DialogFragment newFragment1 = SendRequestDialog.newInstance(mStackLevel);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", mBundle.getString("user_id"));
                    bundle1.putString("user_name", mBundle.getString("user_name"));
                    bundle1.putString("user_avatar", mBundle.getString("user_avatar"));
                    newFragment1.setArguments(bundle1);
                    newFragment1.show(ft1, "dialog");
                }
            });
        }

        if (new KochPrefStore(ConversationActivity.this).getPreferenceValue(Constants.userGroupId).equalsIgnoreCase("3")){
            linearLayout.setVisibility(View.GONE);
        }

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
                getMoreConversationMessages();
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

    private void getMoreConversationMessages() {
        String url = "";

        url = BuildConfig.API_BASE_URL + "message/show/message?email="
                + new KochPrefStore(this).getPreferenceValue(Constants.userEmail)
                + "&password=" + new KochPrefStore(this).getPreferenceValue(Constants.userPassword)
                + "&message_id=" + mBundle.getString("message_id") + "&page=" + pageCount;


        if (Utils.isOnline(this)) {
            // Tag used to cancel the request
            String tag_string_req = "string_req";

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    response = StringEscapeUtils.unescapeJava(response);
                    if (pageCount > 1 && mDataSet.size() != 0) {
                        mDataSet.remove(0);
                        mAdapter.isLoading = false;
                        mAdapter.notifyItemRemoved(0);
                    }

                    List<ConversationItem> messagesList = JsonParser.parseConversation(response);
                    if (messagesList != null) {
                        for (ConversationItem item : messagesList) {
                            mDataSet.add(0, item);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.smoothScrollToPosition(12);

                        }
                    }

                    EventBus.getDefault().post(new UpdateMessageCountEvent("message"));

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            strReq.setShouldCache(false);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    private void getConversationMessages(){
        String url = "";

            url = BuildConfig.API_BASE_URL + "message/show/message?email="
                    + new KochPrefStore(this).getPreferenceValue(Constants.userEmail)
                    + "&password=" + new KochPrefStore(this).getPreferenceValue(Constants.userPassword)
                    + "&message_id=" + mBundle.getString("message_id") + "&page=" + 1;


        if (Utils.isOnline(this)) {
            // Tag used to cancel the request
            String tag_string_req = "string_req";

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    response = StringEscapeUtils.unescapeJava(response);
                    // TODO: 13/07/16
                    clearDataSet();
                    for (ConversationItem item :
                            JsonParser.parseConversation(response)) {
                        mDataSet.add(item);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);

                    }

                    EventBus.getDefault().post(new UpdateMessageCountEvent("message"));

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            strReq.setShouldCache(false);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    // remove all item from RecyclerView
    private void clearDataSet() {
        if (mDataSet != null) {
            mDataSet.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void doNewMessage() {

        if (Utils.isOnline(this)) {
            if (checkDataForNewMessage()) {
                // Set up a progress dialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText(getString(R.string.wait));
                pDialog.setCancelable(false);
                pDialog.show();

                // Tag used to cancel the request
                String url = BuildConfig.API_BASE_URL + "message/add/new";

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();
                        try {
                            response = URLDecoder.decode(response, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        // add item to list here
                        String myEmail = new KochPrefStore(ConversationActivity.this).getPreferenceValue(Constants.userEmail);
                        String imageUrl = new KochPrefStore(ConversationActivity.this).getPreferenceValue(Constants.userAvatarUrl);
                        ConversationItem item = new ConversationItem(imageUrl, editText1.getText().toString().trim(), false, null, -1, myEmail, null, null);
                        mDataSet.add(item);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                        //getConversationMessagesWhenOpen();
                        editText1.setText("");

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        // show error message
                        new SweetAlertDialog(ConversationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("خطأ")
                                .setContentText("حاول مره أخري")
                                .show();
                    }
                }) {


                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        params.put("to[]", to);
                        params.put("message", messageText);
                        return params;

                    }
                };
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq);
            }

        } else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("خطأ")
                    .setContentText("تحقق من الأتصال بألأنترنت")
                    .show();
        }
    }

    private boolean checkDataForNewMessage() {
        email = new KochPrefStore(this).getPreferenceValue(Constants.userEmail);
        password = new KochPrefStore(this).getPreferenceValue(Constants.userPassword);
        to = mBundle.getString("user_id");
        messageText = editText1.getText().toString().trim();

        if (email != null && !email.trim().isEmpty()
                && password != null && !password.trim().isEmpty()
                && to != null && !to.trim().isEmpty()
                && messageText != null && !messageText.trim().isEmpty()) {

            return true;

        } else {
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("هذه طريقه غير صحيحه حاول مره اخرى")
                    .show();
            return false;
        }

    }

    @Subscribe
    public void onMessageEvent(NotificationEvent event) {
        getConversationMessages();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
