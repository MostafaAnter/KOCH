package com.perfect_apps.koch.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class AboutActivity extends LocalizationActivity {

    public static final String TAG = "AboutFragment";

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setToolbar();

        progressBar.setVisibility(View.VISIBLE);

        if (Utils.isOnline(this)) {
            String tag_string_req = "string_req";

            String url;
            if (getLanguage().equalsIgnoreCase("en")){
                url = "http://services-apps.net/koch/api/pages/1?lng=en";
            }else {
                url = "http://services-apps.net/koch/api/pages/1?lng=ar";
            }
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());
                    progressBar.setVisibility(View.GONE);
                    parseResponse(response);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else {
            // show error message
            progressBar.setVisibility(View.GONE);
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please check your Network connection!")
                    .show();
        }

    }

    private void parseResponse(String feed) {

        try {
            JSONObject rootObject = new JSONObject(feed);
            JSONObject dataObject = rootObject.optJSONObject("data");

            title.setText(dataObject.optString("title"));
            content.setText(Html.fromHtml(dataObject.optString("page_description")).toString().replaceAll("</p>|</br>", ""));


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*
        * hide title
        * */
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
    }

}
