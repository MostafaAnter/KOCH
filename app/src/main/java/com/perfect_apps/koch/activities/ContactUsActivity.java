package com.perfect_apps.koch.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.perfect_apps.koch.BuildConfig;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ContactUsActivity extends LocalizationActivity implements View.OnClickListener {

    public static final String TAG = "ContactUs";

    private static String email;
    private static String name;
    private static String subject;
    private static String message;
    private static String phone;

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.text1)
    TextView textView1;
    @BindView(R.id.editText1)
    EditText editText1;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.editText3)
    EditText editText3;
    @BindView(R.id.editText4)
    EditText editText4;
    @BindView(R.id.editText5)
    EditText editText5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        setToolbar();
        changeFont();
        button1.setOnClickListener(this);
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

    private void changeFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");

        textView1.setTypeface(fontBold);
        editText1.setTypeface(font);
        editText2.setTypeface(font);
        editText3.setTypeface(font);
        editText4.setTypeface(font);
        editText5.setTypeface(font);
        button1.setTypeface(font);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                sendReport();
                break;
        }
    }


    private void sendReport() {
        if (Utils.isOnline(this)) {

            if (checkValidation()) {
                // Set up a progress dialog
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("أنتظر...");
                pDialog.setCancelable(false);
                pDialog.show();

                // Tag used to cancel the request
                String url = BuildConfig.API_BASE_URL + "/contact_us";

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

                        new SweetAlertDialog(ContactUsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("عمل رأئع!")
                                .setContentText("تم ارسال رسالتك بنجاح")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                     finish();

                                    }
                                })
                                .show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        // show error message
                        new SweetAlertDialog(ContactUsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("خطأ")
                                .setContentText("حاول مره أخري")
                                .show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();

                        params.put("full_name", name);
                        params.put("mobile_number", phone);
                        params.put("subject", subject);
                        params.put("message", message);
                        params.put("email", email);
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

    private boolean checkValidation() {
        try {
            name = URLEncoder.encode(editText1.getText().toString().trim(), "UTF-8");
            phone = URLEncoder.encode(editText3.getText().toString().trim(), "UTF-8");
            subject = URLEncoder.encode(editText4.getText().toString().trim(), "UTF-8");
            message = URLEncoder.encode(editText5.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        email = editText2.getText().toString().trim();


        // first check mail format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("البريد الالكترونى غير صالح")
                    .show();
            return false;
        }


        if (name != null && !name.trim().isEmpty()
                && subject != null && !subject.trim().isEmpty()
                && email != null && !email.trim().isEmpty()
                && phone != null && !phone.trim().isEmpty()
                && message != null && !message.trim().isEmpty()) {

            return true;

        } else {
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("قم بإكمال تسجيل البيانات")
                    .show();
            return false;
        }
    }

}
