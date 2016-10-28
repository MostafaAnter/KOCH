package com.perfect_apps.koch.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.SweetDialogHelper;
import com.perfect_apps.koch.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignInActivity extends LocalizationActivity {

    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;

    @BindView(R.id.username_input)
    EditText userNameInput;
    @BindView(R.id.password_input)
    EditText passwordInput;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.text1)
    TextView textView1;
    @BindView(R.id.text2)
    TextView textView2;
    @BindView(R.id.text3)
    TextView textView3;
    @BindView(R.id.text4)
    TextView textView4;

    private int signUpPageFlage = 0;

    private String email;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        setToolbar();
        changeTextFont();
        setRadioButtons();
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

    private void changeTextFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        //Typeface fontBold = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");

        radioButton1.setTypeface(font);
        radioButton2.setTypeface(font);

        userNameInput.setTypeface(font);
        passwordInput.setTypeface(font);

        loginButton.setTypeface(font);
        textView1.setTypeface(font);
        textView2.setTypeface(font);
        textView3.setTypeface(font);
        textView4.setTypeface(font);

    }

    private void setRadioButtons() {

        if (radioButton1.isChecked()) {
            signUpPageFlage = 1;

        } else if (radioButton2.isChecked()) {
            signUpPageFlage = 2;
        }

        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    signUpPageFlage = 1;
                }
            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    signUpPageFlage = 2;
                }
            }
        });
    }


    public void signUp(View view) {
        if (signUpPageFlage == 1) {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        } else if (signUpPageFlage == 2) {
            startActivity(new Intent(SignInActivity.this, SignUpClientActivity.class));
        }
    }

    public void signIn(View view) {
        if (signUpPageFlage == 1) {
            providerLogin();
        } else if (signUpPageFlage == 2) {
            clientLogin();
        }
    }

    private void providerLogin() {
        if (Utils.isOnline(this)) {
            if (attempData()) {
                // Set up a progress dialog
                final SweetDialogHelper sdh = new SweetDialogHelper(this);
                sdh.showMaterialProgress(getString(R.string.wait));

                // Tag used to cancel the request
                String tag_string_req = "string_req";
                String url = Constants.providerLoginUrl;

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        sdh.dismissDialog();

                        parseFeed(response);

                        Log.d("response", response);

                        startActivity(new Intent(SignInActivity.this, ProviderHomeActivity.class));
                        finish();
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
                        params.put("email", email);
                        params.put("password", password);
                        return params;

                    }
                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        } else {
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("ناسف...")
                    .setContentText("هناك مشكله بشبكة الانترنت حاول مره اخرى")
                    .show();
        }
    }

    private void clientLogin() {
        if (Utils.isOnline(this)) {
            if (attempData()) {
                // Set up a progress dialog
                final SweetDialogHelper sdh = new SweetDialogHelper(this);
                sdh.showMaterialProgress(getString(R.string.wait));

                // Tag used to cancel the request
                String tag_string_req = "string_req";
                String url = Constants.clientLoginUrl;

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        sdh.dismissDialog();
                        parseFeed(response);
                        Log.d("response", response);
                        startActivity(new Intent(SignInActivity.this, ClientHomeActivity.class));
                        finish();
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
                        params.put("email", email);
                        params.put("password", password);
                        return params;

                    }
                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        } else {
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("ناسف...")
                    .setContentText("هناك مشكله بشبكة الانترنت حاول مره اخرى")
                    .show();
        }
    }

    private boolean attempData(){
        email = userNameInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();

        // first check mail format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("البريد الالكترونى غير صالح")
                    .show();
            return false;
        }


        if (email != null && !email.trim().isEmpty()
                && password != null && !password.trim().isEmpty()){

            return true;

        }else {
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("نأسف !")
                    .setContentText("قم بإكمال تسجيل البيانات")
                    .show();
            return false;
        }


    }

    private void parseFeed(String feed){
        try {
            JSONObject jsonObject =  new JSONObject(feed);
            JSONObject itemObject = jsonObject.optJSONObject("item");
            String id = itemObject.optString("id");
            String name = itemObject.optString("name");
            String email = itemObject.optString("email");
            String image = itemObject.optString("image_full_path");
            String user_password = itemObject.optString("user_password");
            JSONObject groupObject = itemObject.optJSONObject("group");
            String groupId = groupObject.optString("id");

            new KochPrefStore(SignInActivity.this).addPreference(Constants.userId, id);
            new KochPrefStore(SignInActivity.this).addPreference(Constants.userEmail, email);
            new KochPrefStore(SignInActivity.this).addPreference(Constants.userAvatarUrl, image);
            new KochPrefStore(SignInActivity.this).addPreference(Constants.userName, name);
            new KochPrefStore(SignInActivity.this).addPreference(Constants.userPassword, user_password);
            new KochPrefStore(SignInActivity.this).addPreference(Constants.userGroupId, groupId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
