package com.perfect_apps.koch.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.perfect_apps.koch.BuildConfig;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.activities.ConversationActivity;
import com.perfect_apps.koch.activities.SplashActivity;
import com.perfect_apps.koch.adapters.ConversationAdapter;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.interfaces.OnLoadMoreListener;
import com.perfect_apps.koch.models.ConversationItem;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.Utils;

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
 * Created by mostafa_anter on 10/31/16.
 */

public class SenderDirectChatFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "SenderDirectChatFragmen";

    private static int mStackLevel = 0;
    private static String email;
    private static String password;
    private static String to;
    private static String messageText;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.send_button)ImageView sendButton;
    @BindView(R.id.send_request)ImageView sendRequest;
    @BindView(R.id.messageInput)EditText editText1;

    // user id
    private String userId;

    private int pageCount = 1;
    // add listener for loading more view
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_button:
                doNewMessage();
                break;
            case R.id.send_request:
                mStackLevel++;
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
                if (prev1 != null) {
                    ft1.remove(prev1);
                }
                ft1.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment1 = SendRequestDialog.newInstance(mStackLevel);
                Bundle bundle1 = new Bundle();
                bundle1.putString("user_id", userId);
                newFragment1.setArguments(bundle1);
                newFragment1.show(ft1, "dialog");
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

    public SenderDirectChatFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize mDataSet
        mDataSet = new ArrayList<>();
        // TODO: 11/2/16  initialize userId here
        userId = Constants.sharedUserId;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sender_direct_conversation, container, false);
        ButterKnife.bind(this, view);
        setRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sendButton.setOnClickListener(this);
        sendRequest.setOnClickListener(this);

        if (new KochPrefStore(getActivity()).getPreferenceValue(Constants.userGroupId).equalsIgnoreCase("3")){
            sendRequest.setVisibility(View.GONE);
        }
    }

    private void setRecyclerView(){
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mAdapter = new ConversationAdapter(getActivity(), mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        //       final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                totalItemCount = linearLayoutManager.getItemCount();
//                lastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
//
//                if (lastVisibleItem == 0
//                        && !mAdapter.isLoading
//                        && totalItemCount > visibleThreshold) {
//                    if (mAdapter.mOnLoadMoreListener != null) {
//                        mAdapter.mOnLoadMoreListener.onLoadMore();
//                    }
//                    mAdapter.isLoading = true;
//                }
//            }
//        });
//
//        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                Log.e(TAG, "Load More");
//                pageCount++;
//                mDataSet.add(0, null);
//                mAdapter.notifyItemInserted(0);
//
//                // loadMoreData
//                getConversationMessages();
//            }
//        });
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
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setStackFromEnd(true);
                mLayoutManager = linearLayoutManager;
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private void doNewMessage() {

        if (Utils.isOnline(getActivity())) {
            if (checkDataForNewMessage()) {
                // Set up a progress dialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
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
                        String myEmail = new KochPrefStore(getActivity()).getPreferenceValue(Constants.userEmail);
                        String imageUrl = new KochPrefStore(getActivity()).getPreferenceValue(Constants.userAvatarUrl);
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
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("خطأ")
                    .setContentText("تحقق من الأتصال بألأنترنت")
                    .show();
        }
    }

    private boolean checkDataForNewMessage() {
        email = new KochPrefStore(getActivity()).getPreferenceValue(Constants.userEmail);
        password = new KochPrefStore(getActivity()).getPreferenceValue(Constants.userPassword);
        to = userId;
        messageText = editText1.getText().toString().trim();

        if (email != null && !email.trim().isEmpty()
                && password != null && !password.trim().isEmpty()
                && to != null && !to.trim().isEmpty()
                && messageText != null && !messageText.trim().isEmpty()) {

            return true;

        } else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("هذه طريقه غير صحيحه حاول مره اخرى")
                    .show();
            return false;
        }

    }


}
