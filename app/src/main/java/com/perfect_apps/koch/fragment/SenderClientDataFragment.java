package com.perfect_apps.koch.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.perfect_apps.koch.BuildConfig;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.models.ClientInfo;
import com.perfect_apps.koch.parser.JsonParser;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.MapHelper;
import com.perfect_apps.koch.utils.SweetDialogHelper;
import com.perfect_apps.koch.utils.Utils;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by mostafa_anter on 10/3/16.
 */

public class SenderClientDataFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener{

    @BindView(R.id.map)
    MapView mapView;


    @BindView(R.id.text2) TextView textView2;
    @BindView(R.id.text4) TextView textView4;
    @BindView(R.id.text5) TextView textView5;
    @BindView(R.id.imageView1)ImageView imageView1;
    @BindView(R.id.block)LinearLayout linearLayoutBlock;
    @BindView(R.id.call)LinearLayout linearLayoutCall;

    @BindView(R.id.button1) Button button1;

    private GoogleMap mMap;
    private static final int GPS_ERRORDIALOG_REQUEST = 9001;


    // for manage visibleHintFunc
    private boolean visibleHintGone = false;
    private boolean onCreateGone = false;

    // for client data info
    private ClientInfo clientInfo;

    public SenderClientDataFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clint_detail_data_tab, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // for map
        if (servicesOK()) {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        changeFontOfText();

        onCreateGone = true;
        if (visibleHintGone) {
           // some work
            getClientData();
        }

        linearLayoutBlock.setOnClickListener(this);
        linearLayoutCall.setOnClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            visibleHintGone = true;
        }
        if (isVisibleToUser && onCreateGone) {
           // do some thing

            getClientData();

            visibleHintGone = false;
            onCreateGone = false;
        }
    }

    private void getClientData(){
        if (Utils.isOnline(getActivity())) {
            // Set up a progress dialog
            final SweetDialogHelper sdh = new SweetDialogHelper(getActivity());
            sdh.showMaterialProgress(getString(R.string.wait));

            // Tag used to cancel the request
            String tag_string_req = "string_req";
            String url = Constants.getClientInfo;

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    response = StringEscapeUtils.unescapeJava(response);
                    clientInfo = JsonParser.parseClientInfo(response);
                    bindData();
                    sdh.dismissDialog();
                    Log.d("response", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    sdh.dismissDialog();
                    // show error message
                    sdh.showErrorMessage(getString(R.string.error), getString(R.string.try_again));
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", getArguments().getString("user_id"));
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

    private void bindData(){

        textView2.setText(clientInfo.getName());
        if (clientInfo.getImage_full_path() != null && !clientInfo.getImage_full_path().trim().isEmpty())
            Glide.with(getActivity())
                    .load(clientInfo.getImage_full_path())
                    .placeholder(R.color.gray_btn_bg_color)
                    .centerCrop()
                    .crossFade()
                    .thumbnail(0.1f)
                    .into(imageView1);
        textView4.setText(clientInfo.getMobile());
        textView5.setText(clientInfo.getEmail());

        if (clientInfo.getAddressLat() != null && !clientInfo.getAddressLat().equalsIgnoreCase("null")
                && !clientInfo.getAddressLat().trim().isEmpty() && clientInfo.getAddressLng() != null
                && !clientInfo.getAddressLng().equalsIgnoreCase("null")
                && !clientInfo.getAddressLng().trim().isEmpty()) {
            MapHelper.setUpMarker(mMap, new LatLng(Double.valueOf(clientInfo.getAddressLat()),
                    Double.valueOf(clientInfo.getAddressLng())), R.drawable.map_user_marker);
            button1.setText(clientInfo.getAddressName());
        } else {
            button1.setVisibility(View.GONE);
        }

    }

    private void changeFontOfText(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        textView2.setTypeface(font);
        textView4.setTypeface(font);
        textView5.setTypeface(font);
        button1.setTypeface(font);

    }

    // setup map
    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, getActivity(), GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(getActivity(), "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.block:
                if (getArguments() != null)
                    block();
                break;
            case R.id.call:
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + clientInfo.getMobile()));
                startActivity(callIntent);
                break;
        }
    }

    private void block() {
        if (Utils.isOnline(getActivity())) {

            // Tag used to cancel the request
            String tag_string_req = "string_req";
            String url = BuildConfig.API_BASE_URL + "block/block";

            // Set up a progress dialog
            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(getString(R.string.wait));
            pDialog.setCancelable(false);
            pDialog.show();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("push_token_response", response);

                    pDialog.dismissWithAnimation();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(getString(R.string.done))
                            .setContentText(getString(R.string.your_blocked_this_user))
                            .show();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    new SweetDialogHelper(getActivity())
                            .showErrorMessage(getString(R.string.error), getString(R.string.you_are_blocked_this_user));
                    pDialog.dismissWithAnimation();
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("blocked_id", getArguments().getString("user_id"));
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
}
