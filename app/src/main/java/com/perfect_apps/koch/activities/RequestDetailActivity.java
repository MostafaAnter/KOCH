package com.perfect_apps.koch.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.perfect_apps.koch.BuildConfig;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.models.OrderRequest;
import com.perfect_apps.koch.parser.JsonParser;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.SweetDialogHelper;
import com.perfect_apps.koch.utils.Utils;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestDetailActivity extends LocalizationActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text1)
    TextView textView1;
    @BindView(R.id.text2)
    TextView textView2;

    @BindView(R.id.text00)
    TextView textView00;
    @BindView(R.id.text0)
    TextView textView0;

    @BindView(R.id.text4)
    TextView textView4;
    @BindView(R.id.text5)
    TextView textView5;
    @BindView(R.id.text6)
    TextView textView6;
    @BindView(R.id.text7)
    TextView textView7;
    @BindView(R.id.text8)
    TextView textView8;
    @BindView(R.id.text9)
    TextView textView9;
    @BindView(R.id.text10)
    TextView textView10;

    @BindView(R.id.editText1)EditText editText;

    @BindView(R.id.user_avatar)
    CircleImageView avatar;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;

    private OrderRequest orderRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        ButterKnife.bind(this);

        setToolbar();
        changeFontOfText();
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        orderRequest = getIntent().getParcelableExtra("orderRequest");
        if (!new KochPrefStore(this).getPreferenceValue(Constants.userGroupId).equalsIgnoreCase("3")) {
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
        }

        bindData();
    }

    private void changeFontOfText() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        textView1.setTypeface(font);
        textView2.setTypeface(font);
        textView00.setTypeface(font);
        textView0.setTypeface(font);
        textView4.setTypeface(font);
        editText.setTypeface(font);
        textView5.setTypeface(font);
        textView6.setTypeface(font);
        textView7.setTypeface(font);
        textView8.setTypeface(font);
        textView9.setTypeface(font);
        textView10.setTypeface(font);
        button1.setTypeface(font);
        button2.setTypeface(font);

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

        ImageView backIc = (ImageView) toolbar.findViewById(R.id.back);
        ImageView profileIc = (ImageView) toolbar.findViewById(R.id.profile);
        ImageView messagesIc = (ImageView) toolbar.findViewById(R.id.messages);

        backIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestDetailActivity.this, ProviderProfileActivity.class));
            }
        });

        messagesIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestDetailActivity.this, ProviderProfileActivity.class);
                intent.putExtra("messageTab", true);
                startActivity(intent);


            }
        });
    }

    private void bindData() {
        textView4.setText(orderRequest.getUpdated_at());
        textView6.setText(orderRequest.getTitle());
        textView0.setText(orderRequest.getRow_hash());
        textView8.setText(orderRequest.getDetail());
        textView10.setText(orderRequest.getCost());
        if (new KochPrefStore(this).getPreferenceValue(Constants.userGroupId).equalsIgnoreCase("3")) {
            textView2.setText(orderRequest.getProviderName());
            Glide.with(this)
                    .load(orderRequest.getProviderImage())
                    .placeholder(R.color.gray_btn_bg_color)
                    .centerCrop()
                    .crossFade()
                    .thumbnail(0.1f)
                    .into(avatar);

        } else {
            textView2.setText(orderRequest.getClientName());
            Glide.with(this)
                    .load(orderRequest.getClientImage())
                    .placeholder(R.color.gray_btn_bg_color)
                    .centerCrop()
                    .crossFade()
                    .thumbnail(0.1f)
                    .into(avatar);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                changeRequestState("1");
                break;
            case R.id.button2:
                changeRequestState("2");
                break;
        }
    }

    private void changeRequestState(String status) {
        String url = BuildConfig.API_BASE_URL + "status_request/client?email="
                + new KochPrefStore(this).getPreferenceValue(Constants.userEmail)
                + "&password=" + new KochPrefStore(this).getPreferenceValue(Constants.userPassword) + "&row_hash="
                + orderRequest.getRow_hash() + "&status=" + status + "&notes=" + editText.getText().toString();

        if (Utils.isOnline(this)) {
            // Tag used to cancel the request
            String tag_string_req = "string_req";
            // Set up a progress dialog
            final SweetDialogHelper sdh = new SweetDialogHelper(this);
            sdh.showMaterialProgress(getString(R.string.wait));
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    sdh.dismissDialog();
                    response = StringEscapeUtils.unescapeJava(response);
                    new SweetAlertDialog(RequestDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(getString(R.string.done))
                            .setContentText(getString(R.string.done))
                            .show();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    sdh.dismissDialog();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

    }
}
