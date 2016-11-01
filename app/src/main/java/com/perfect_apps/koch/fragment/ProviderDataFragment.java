package com.perfect_apps.koch.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.activities.ClientHomeActivity;
import com.perfect_apps.koch.activities.SignInActivity;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.parser.JsonParser;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.SweetDialogHelper;
import com.perfect_apps.koch.utils.Utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by mostafa_anter on 10/3/16.
 */

public class ProviderDataFragment extends Fragment{
    @BindView(R.id.text1) TextView textView1 ;
    @BindView(R.id.text2) TextView textView2 ;
    @BindView(R.id.text3) TextView textView3 ;
    @BindView(R.id.text4) TextView textView4 ;
    @BindView(R.id.text5) TextView textView5 ;
    @BindView(R.id.text6) TextView textView6 ;
    @BindView(R.id.text7) TextView textView7 ;
    @BindView(R.id.text8) TextView textView8 ;
    @BindView(R.id.text9) TextView textView9 ;
    @BindView(R.id.text10) TextView textView10 ;
    @BindView(R.id.text11) TextView textView11 ;
    @BindView(R.id.text12) TextView textView12 ;
    @BindView(R.id.text13) TextView textView13 ;
    @BindView(R.id.text14) TextView textView14 ;
    @BindView(R.id.text15) TextView textView15 ;
    @BindView(R.id.text16) TextView textView16 ;
    @BindView(R.id.text17) TextView textView17 ;
    @BindView(R.id.text18) TextView textView18 ;

    @BindView(R.id.ratingBar)RatingBar rb;

    public ProviderDataFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_data_tab, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void changeTextFont() {
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");


        textView1.setTypeface(font);
        textView2.setTypeface(font);
        textView3.setTypeface(font);
        textView4.setTypeface(font);
        textView5.setTypeface(font);
        textView6.setTypeface(font);
        textView7.setTypeface(font);
        textView8.setTypeface(font);
        textView9.setTypeface(font);
        textView10.setTypeface(font);
        textView11.setTypeface(font);
        textView12.setTypeface(font);
        textView13.setTypeface(font);
        textView14.setTypeface(fontBold);
        textView15.setTypeface(font);
        textView16.setTypeface(fontBold);
        textView17.setTypeface(font);
        textView18.setTypeface(fontBold);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeTextFont();
        getProviderDate();
        getRateInfo();
    }

    private void getProviderDate(){
        if (Utils.isOnline(getActivity())) {
                // Set up a progress dialog
                final SweetDialogHelper sdh = new SweetDialogHelper(getActivity());
                sdh.showMaterialProgress(getString(R.string.wait));

                // Tag used to cancel the request
                String tag_string_req = "string_req";
                String url = Constants.getProviderInfo;

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
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

    private void getRateInfo(){
        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = Constants.providerRateInfo + new KochPrefStore(getActivity()).getPreferenceValue(Constants.userId);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = StringEscapeUtils.unescapeJava(response);
                Log.d("view all providers", response);
                setRateBar(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error view providers", error.toString());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setRateBar(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = jsonObject.optJSONObject("rates");
            double rate1 = jsonObject1.optDouble("1");
            double rate2 = jsonObject1.optDouble("2");
            double rate3 = jsonObject1.optDouble("3");
            double rate4 = jsonObject1.optDouble("4");
            double rate5 = jsonObject1.optDouble("5");

            if ((rate1 + rate2 + rate3 + rate4 + rate5) != 0) {
                double rate = (rate1 + 2*rate2 + 3*rate3 + 4*rate4 + 5*rate5)/(rate1 + rate2 + rate3 + rate4 + rate5);
                float totalRate = (float) Math.round(rate * 10)/10;
                rb.setRating(totalRate);

            }else {
                rb.setRating(0);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
