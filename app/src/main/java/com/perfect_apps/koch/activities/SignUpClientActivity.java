package com.perfect_apps.koch.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.SweetDialogHelper;
import com.perfect_apps.koch.utils.Utils;
import com.perfect_apps.koch.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.iwf.photopicker.PhotoPicker;

public class SignUpClientActivity extends LocalizationActivity implements View.OnClickListener{

    @BindView(R.id.text1) TextView textView1;
    @BindView(R.id.text2) TextView textView2;
    @BindView(R.id.text3) TextView textView3;
    @BindView(R.id.text4) TextView textView4;
    @BindView(R.id.text5) TextView textView5;
    @BindView(R.id.text6) TextView textView6;
    @BindView(R.id.text7) TextView textView7;

    @BindView(R.id.editText1) EditText editText1;
    @BindView(R.id.editText2) EditText editText2;
    @BindView(R.id.editText3) EditText editText3;
    @BindView(R.id.editText4) EditText editText4;
    @BindView(R.id.editText5) EditText editText5;

    @BindView(R.id.button1) Button button1;
    @BindView(R.id.imageView1)ImageView imageView1;
    @BindView(R.id.pickPhoto)LinearLayout pickPhoto;


    private String name;
    private String mobile;
    private String email;
    private String password;
    private String password_confirmation;
    //private String desc;
    private Uri image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_client);
        ButterKnife.bind(this);
        setToolbar();
        changeTextFont();
        pickPhoto.setOnClickListener(this);
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

    private void changeTextFont(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        //Typeface fontBold = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        textView1.setTypeface(font);
        textView2.setTypeface(font);
        textView3.setTypeface(font);
        textView4.setTypeface(font);
        textView5.setTypeface(font);
        textView6.setTypeface(font);
        textView7.setTypeface(font);

        editText1.setTypeface(font);
        editText2.setTypeface(font);
        editText3.setTypeface(font);
        editText4.setTypeface(font);
        editText5.setTypeface(font);

        button1.setTypeface(font);

    }


    // for pick photo
    public void pickPhoto() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Uri uri = Uri.fromFile(new File(photos.get(0)));
                image = uri;
                setSelectedPhotoInsideCircleShap(uri);
            }
        }
    }

    private void setSelectedPhotoInsideCircleShap(Uri uri){
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                .error(R.drawable.__picker_ic_broken_image_black_48dp)
                .into(imageView1);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pickPhoto:
                pickPhoto();
                break;
            case R.id.button1:
                register();
                break;
        }
    }

    private boolean registerConditionsIsOk(){

        try {
            name = URLEncoder.encode(editText1.getText().toString().trim(), "UTF-8");
            mobile = URLEncoder.encode(editText2.getText().toString().trim(), "UTF-8");
            email = editText3.getText().toString().trim();
            password = editText4.getText().toString().trim();
            password_confirmation = editText5.getText().toString().trim();
            //desc = URLEncoder.encode(editText4.getText().toString().trim(), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (name == null || name.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.enter_your_name));
            return false;
        }
        if ( mobile == null ||  mobile.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.enter_phone_number));
            return false;
        }
        if (email == null || email.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.enter_email));
            return false;
        }
        // first check mail format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.enter_email));
            return false;
        }
        if (password == null || password.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.password));
            return false;
        }
//        if (desc == null || desc.trim().isEmpty()){
//            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.short_description));
//            return false;
//        }

        if (!password.equalsIgnoreCase(password_confirmation)){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.password_not_equal));
            return false;
        }
        return true;

    }

    private void register(){

        if (registerConditionsIsOk()){

            if (Utils.isOnline(this)) {

                // make request
                final SweetDialogHelper sdh = new SweetDialogHelper(this);
                sdh.showMaterialProgress(getString(R.string.wait));
                String tag_string_req = "string_req";
                String url = Constants.registerClientURL;
                // begin of request
                VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        sdh.dismissDialog();
                        String resultResponse = new String(response.data);
                        try {
                            JSONObject result = new JSONObject(resultResponse);
                            Log.d("response", resultResponse);
                            finish();
//                            Intent intent = new Intent(RegisterTeacherMembershipActivity.this, LoginTeacherActivity.class);
//                            intent.putExtra("email", email);
//                            intent.putExtra("password", password);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sdh.dismissDialog();
                        String errorServerMessage = "";
                        if (error.networkResponse.data != null) {
                            errorServerMessage = new String(error.networkResponse.data);
                            try {
                                JSONObject errorMessageObject = new JSONObject(errorServerMessage);
                                Log.e("server error", errorMessageObject.toString());
                                JSONObject jsonObjectError = errorMessageObject.optJSONObject("errors");
                                errorServerMessage = jsonObjectError.toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // show error message
                        sdh.showErrorMessage(getString(R.string.error), errorServerMessage);

                        NetworkResponse networkResponse = error.networkResponse;
                        String errorMessage = "Unknown error";
                        if (networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                errorMessage = "Request timeout";
                            } else if (error.getClass().equals(NoConnectionError.class)) {
                                errorMessage = "Failed to connect server";
                            }
                        } else {
                            String result = new String(networkResponse.data);
                            try {
                                JSONObject response = new JSONObject(result);
                                String status = response.getString("status");
                                String message = response.getString("message");

                                Log.e("Error Status", status);
                                Log.e("Error Message", message);

                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("Error", errorMessage);
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        if(name != null && !name.trim().isEmpty())
                            params.put("name", name);
                        if(email != null && !email.trim().isEmpty())
                            params.put("email", email);
                        if(mobile != null && !mobile.trim().isEmpty())
                            params.put("mobile", mobile);
                        if(password != null&& !password.trim().isEmpty())
                            params.put("password", password);
                        if(password_confirmation != null && !password_confirmation.trim().isEmpty())
                            params.put("password_confirmation", password_confirmation);

                        return params;
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        // file name could found file base or direct access from real path
                        // for now just get bitmap data from ImageView

                        if (image != null)
                            params.put("image", new DataPart("file_avatar.jpg", Utils.getFileDataFromDrawable(SignUpClientActivity.this,
                                    image), "image/jpeg"));

                        return params;
                    }
                };

                int socketTimeout = 30000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                multipartRequest.setRetryPolicy(policy);

                AppController.getInstance().addToRequestQueue(multipartRequest);
                // last of request



            }else {
                // show error message
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ناسف...")
                        .setContentText("هناك مشكله بشبكة الانترنت حاول مره اخرى")
                        .show();
            }
        }

    }

}
