package com.perfect_apps.koch.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.activities.ProviderDetailActivity;
import com.perfect_apps.koch.activities.SignUpClientActivity;
import com.perfect_apps.koch.activities.SplashActivity;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.models.ClientInfo;
import com.perfect_apps.koch.models.ProviderInfo;
import com.perfect_apps.koch.parser.JsonParser;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.MapHelper;
import com.perfect_apps.koch.utils.SweetDialogHelper;
import com.perfect_apps.koch.utils.Utils;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
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

public class ClientDataFragment extends Fragment implements View.OnClickListener{


    @BindView(R.id.text1) TextView textView1;
    @BindView(R.id.text2) TextView textView2;
    @BindView(R.id.text3) TextView textView3;
    @BindView(R.id.text4) TextView textView4;
    @BindView(R.id.text5) TextView textView5;

    @BindView(R.id.imageView1)ImageView imageView1;
    @BindView(R.id.exit)LinearLayout linearLayoutExit;
    @BindView(R.id.editProfile)LinearLayout linearLayoutEdit;



    // for client data info
    private ClientInfo clientInfo;



    // for manage visibleHintFunc
    private boolean visibleHintGone = false;
    private boolean onCreateGone = false;

    public ClientDataFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clint_data_tab, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changeFontOfText();
        linearLayoutEdit.setOnClickListener(this);
        linearLayoutExit.setOnClickListener(this);

        onCreateGone =true;

        if (visibleHintGone){
            getClientData();
        }


    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            visibleHintGone = true;
        if (isVisibleToUser && onCreateGone){
            getClientData();
        }
    }

    private void changeFontOfText(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        textView1.setTypeface(font);
        textView2.setTypeface(font);
        textView3.setTypeface(font);
        textView4.setTypeface(font);
        textView5.setTypeface(font);

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
                    params.put("user_id", new KochPrefStore(getActivity()).getPreferenceValue(Constants.userId));
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit:
                new KochPrefStore(getActivity()).clearPreference();
                startActivity(new Intent(getActivity(), SplashActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                break;
            case R.id.editProfile:
                Intent intent = new Intent(getActivity(), SignUpClientActivity.class);
                intent.putExtra("flag", "edit");
                startActivity(intent);
                break;
        }

    }
}
