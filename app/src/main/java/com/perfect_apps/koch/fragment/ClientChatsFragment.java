package com.perfect_apps.koch.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.perfect_apps.koch.BuildConfig;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.activities.ConversationActivity;
import com.perfect_apps.koch.activities.ProviderDetailActivity;
import com.perfect_apps.koch.adapters.InboxItemsAdapter;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.models.InboxItem;
import com.perfect_apps.koch.parser.JsonParser;
import com.perfect_apps.koch.services.NotificationEvent;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.DividerItemDecoration;
import com.perfect_apps.koch.utils.RecyclerItemClickListener;
import com.perfect_apps.koch.utils.Utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by mostafa_anter on 10/3/16.
 */

public class ClientChatsFragment extends Fragment {

    @BindView(R.id.noData)
    LinearLayout noDataView;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;

    private static final String TAG = "ProviderChatsFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;


    protected LayoutManagerType mCurrentLayoutManagerType;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected InboxItemsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<InboxItem> mDataset;

    // for dialog actions
    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();

    // for dialog actions
    private String message_id;
    private String user_id;
    private String group_id;

    // for create items one time;
    private int dialogItemsCount = 0;

    // for manage visibleHintFunc
    private boolean visibleHintGone = false;
    private boolean onCreateGone = false;

    public ClientChatsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clint_chats_tab, container, false);

        ButterKnife.bind(this, view);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new InboxItemsAdapter(getActivity(), mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);


        //noinspection ResourceAsColor
        mSwipeRefresh.setColorScheme(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        mSwipeRefresh.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24,
                        getResources().getDisplayMetrics()));

        // set item click listener
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        Bundle b = new Bundle();
                        b.putString("message_id", mDataset.get(position).getChat_id());
                        b.putString("user_id", mDataset.get(position).getUser_id());
                        b.putString("group_id", mDataset.get(position).getGroup_id());
                        b.putString("user_name", mDataset.get(position).getChats_name());
                        b.putString("user_avatar", mDataset.get(position).getChats_avatar());
                        Intent intent = new Intent(getActivity(), ConversationActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);


                    }
                })
        );

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {
                final View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    childView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if (dialogItemsCount < 1) {
                                dialogItemsCount++;
                                mMenuItems.add(new DialogMenuItem(getString(R.string.sender_info), R.drawable.ic_error_outline_black_24dp));
                                mMenuItems.add(new DialogMenuItem(getString(R.string.delete), R.drawable.__picker_ic_delete_black_24dp));
                            }
                            // show dialog :)
                            message_id = mDataset.get(rv.getChildAdapterPosition(childView)).getChat_id();
                            user_id = mDataset.get(rv.getChildAdapterPosition(childView)).getUser_id();
                            group_id = mDataset.get(rv.getChildAdapterPosition(childView)).getGroup_id();
                            normalListDialogCustomAttr();

                            return false;
                        }
                    });
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initiate mDataSet
        mDataset = new ArrayList<>();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                initiateRefresh();
            }
        });

        onCreateGone = true;
        if (visibleHintGone) {
            initiateRefresh();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            visibleHintGone = true;
        if (isVisibleToUser && onCreateGone) {
            initiateRefresh();
            if (mSwipeRefresh != null && !mSwipeRefresh.isRefreshing())
                mSwipeRefresh.setRefreshing(true);

            visibleHintGone = false;
            onCreateGone = false;
        }
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private void initiateRefresh() {
        requestMessages();
    }

    private void onRefreshComplete() {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }

        if (mDataset.size() > 0) {
            noDataView.setVisibility(View.GONE);
        } else {
            noDataView.setVisibility(View.VISIBLE);
        }
    }

    private void requestMessages() {

        String url = BuildConfig.API_BASE_URL + "message/show?email="
                + new KochPrefStore(getActivity()).getPreferenceValue(Constants.userEmail)
                + "&password=" + new KochPrefStore(getActivity()).getPreferenceValue(Constants.userPassword);

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null && !Utils.isOnline(getActivity())) {
            try {
                String data = new String(entry.data, "UTF-8");
                data = StringEscapeUtils.unescapeJava(data);
                clearDataSet();
                for (InboxItem item :
                        JsonParser.parseMyMessages(data)) {
                    mDataset.add(item);
                    mAdapter.notifyDataSetChanged();
                    onRefreshComplete();

                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            if (Utils.isOnline(getActivity())) {
                // Tag used to cancel the request
                String tag_string_req = "string_req";

                // Cached response doesn't exists. Make network call here
                if (!mSwipeRefresh.isRefreshing())
                    mSwipeRefresh.setRefreshing(true);

                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        response = StringEscapeUtils.unescapeJava(response);
                        clearDataSet();
                        for (InboxItem item :
                                JsonParser.parseMyMessages(response)) {
                            mDataset.add(item);
                            mAdapter.notifyDataSetChanged();

                        }
                        onRefreshComplete();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onRefreshComplete();

                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        }


    }

    /**
     * todo
     */
    private void normalListDialogCustomAttr() {
        final NormalListDialog dialog = new NormalListDialog(getActivity(), mMenuItems);
        dialog.title("خيارات أضافية")//
                .titleTextSize_SP(18)//
                .titleBgColor(Color.parseColor("#409ED7"))//
                .itemPressColor(Color.parseColor("#85D3EF"))//
                .itemTextColor(Color.parseColor("#303030"))//
                .itemTextSize(14)//
                .cornerRadius(0)//
                .widthScale(0.8f)//
                .show(R.style.myDialogAnim);

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                // mMenuItems.get(position).mOperName
                //Toast.makeText(getActivity(), mMenuItems.get(position).mOperName + position, Toast.LENGTH_SHORT).show();

                switch (position) {
                    case 0:
                        Bundle b = new Bundle();
                        b.putString(Constants.userId, user_id);
                        Intent intent = new Intent(getActivity(), ProviderDetailActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);
                        break;
                    case 1:
                        deleteConversation();
                        break;
                }

                dialog.dismiss();
            }
        });
    }


    private void deleteConversation() {
        if (Utils.isOnline(getActivity())) {


            // Tag used to cancel the request
            String tag_string_req = "string_req";
            String url = "http://services-apps.net/tutors/api/message/delete";

            // Set up a progress dialog
            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("أنتظر...");
            pDialog.setCancelable(false);
            pDialog.show();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("push_token_response", response);
                    pDialog.dismissWithAnimation();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("تم")
                            .setContentText("لقد قمت بحذف المحادثة")
                            .show();
                    initiateRefresh();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismissWithAnimation();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("message_id", message_id);
                    params.put("email", new KochPrefStore(getActivity()).getPreferenceValue(Constants.userEmail));
                    params.put("password", new KochPrefStore(getActivity()).getPreferenceValue(Constants.userPassword));
                    return params;

                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("ناسف...")
                    .setContentText("هناك مشكله بشبكة الانترنت حاول مره اخرى")
                    .show();
        }
    }

    // remove all item from RecyclerView
    private void clearDataSet() {
        if (mDataset != null) {
            mDataset.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotificationEvent event) {
        initiateRefresh();
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
