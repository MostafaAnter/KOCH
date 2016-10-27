package com.perfect_apps.koch.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.adapters.CitiesAdapter;
import com.perfect_apps.koch.adapters.CountriesAdapter;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.models.Cities;
import com.perfect_apps.koch.models.Countries;
import com.perfect_apps.koch.parser.JsonParser;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.SweetDialogHelper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;

public class SignUpActivity extends LocalizationActivity implements View.OnClickListener{
    @BindView(R.id.text1) TextView textView1;
    @BindView(R.id.text2) TextView textView2;
    @BindView(R.id.text3) TextView textView3;
    @BindView(R.id.text4) TextView textView4;
    @BindView(R.id.text5) TextView textView5;
    @BindView(R.id.text6) TextView textView6;
    @BindView(R.id.text7) TextView textView7;
    @BindView(R.id.text8) TextView textView8;
    @BindView(R.id.text9) TextView textView9;
    @BindView(R.id.text10) TextView textView10;
    @BindView(R.id.text11) TextView textView11;
    @BindView(R.id.text12) TextView textView12;

    @BindView(R.id.editText1) EditText editText1;
    @BindView(R.id.editText2) EditText editText2;
    @BindView(R.id.editText3) EditText editText3;
    @BindView(R.id.editText4) EditText editText4;
    @BindView(R.id.editText5) EditText editText5;
    @BindView(R.id.editText6) EditText editText6;
    @BindView(R.id.editText7) EditText editText7;
    @BindView(R.id.editText8) EditText editText8;
    @BindView(R.id.editText9) EditText editText9;
    @BindView(R.id.editText10) EditText editText10;
    @BindView(R.id.editText11) EditText editText11;
    @BindView(R.id.editText12) EditText editText12;
    @BindView(R.id.editText13) EditText editText13;
    @BindView(R.id.editText14) EditText editText14;
    @BindView(R.id.editText15) EditText editText15;
    @BindView(R.id.imageView1)ImageView imageView1;
    @BindView(R.id.pickPhoto)LinearLayout pickPhoto;

    @BindView(R.id.checkbox1)
    CheckBox checkBox1;

    @BindView(R.id.button1)
    Button button1;

    @BindView(R.id.spinner1)
    Spinner spinner1;
    @BindView(R.id.spinner2)
    Spinner spinner2;


    // parameter i'll post
    private String name;
    private String mobile;
    private String email;
    private String password;
    private String password_confirmation;
    private String desc;
    private String countryId;
    private String cityId;
    private String working_hours;
    private String service_1;
    private String service_2;
    private String service_3;
    private String service_4;
    private String other_services;
    private String delivery = "0";

    //Not required

    private String facebook_url;
    private String twitter_url;
    private String picassa_url;
    private Uri image;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setToolbar();
        changeTextFont();
        getCountries();

        pickPhoto.setOnClickListener(this);
        button1.setOnClickListener(this);

        populateSpinner1(new ArrayList<Countries>());
        populateSpinner2(new ArrayList<Cities>());
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
        textView8.setTypeface(font);
        textView9.setTypeface(font);
        textView10.setTypeface(font);
        textView11.setTypeface(font);
        textView12.setTypeface(font);

        editText1.setTypeface(font);
        editText2.setTypeface(font);
        editText3.setTypeface(font);
        editText4.setTypeface(font);
        editText5.setTypeface(font);
        editText6.setTypeface(font);
        editText7.setTypeface(font);
        editText8.setTypeface(font);
        editText9.setTypeface(font);
        editText10.setTypeface(font);
        editText11.setTypeface(font);
        editText12.setTypeface(font);
        editText13.setTypeface(font);
        editText14.setTypeface(font);
        editText15.setTypeface(font);

        checkBox1.setTypeface(font);
        button1.setTypeface(font);


    }

    private void populateSpinner1(List<Countries> mlist) {

        CountriesAdapter spinnerArrayAdapter = new CountriesAdapter(this, R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(spinnerArrayAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                Countries selectedItem = (Countries) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    countryId = selectedItem.getId();
                    cityId = null;
                    getCities(countryId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner2(List<Cities> mlist) {

        CitiesAdapter spinnerArrayAdapter = new CitiesAdapter(this, R.layout.spinner_item, mlist);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(spinnerArrayAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                //String selectedItemText = (String) parent.getItemAtPosition(position);
                Cities selectedItem = (Cities) parent.getItemAtPosition(position);
                if (position > 0) {
                    // doSome things
                    cityId = selectedItem.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void onCheckboxClicked(View view) {
        if (checkBox1.isChecked()){
            delivery = "1";
        }else {
            delivery = "0";
        }

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

    private void getCountries(){

        /**
        * this section for fetch country
        */
        String urlBrands = Constants.countriesListURL;
        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(urlBrands);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                // do some thing
                populateSpinner1(JsonParser.parseCountriesFeed(data));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    urlBrands, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    List<Countries> spinnerItemList = JsonParser.parseCountriesFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner1(spinnerItemList);
                    }
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("response", "Error: " + error.getMessage());
                }
            }){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            //cacheEntry. = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString, cacheEntry);
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }
            };

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }

    private void getCities(String countryId){

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Constants.citiesListURL + countryId);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                // do some thing
                populateSpinner2(JsonParser.parseCitiesFeed(data));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                    Constants.citiesListURL + countryId, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    List<Cities> spinnerItemList = JsonParser.parseCitiesFeed(response);
                    if (spinnerItemList != null) {
                        populateSpinner2(spinnerItemList);
                    }
                    Log.d("response", response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("response", "Error: " + error.getMessage());
                }
            }){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            //cacheEntry. = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString, cacheEntry);
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }
            };

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

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

    private void register(){

        if (registerConditionsIsOk()){

        }

    }

    private boolean registerConditionsIsOk(){

        try {
            name = URLEncoder.encode(editText1.getText().toString().trim(), "UTF-8");
            mobile = URLEncoder.encode(editText2.getText().toString().trim(), "UTF-8");
            email = editText3.getText().toString().trim();
            password = editText14.getText().toString().trim();
            password_confirmation = editText15.getText().toString().trim();
            desc = URLEncoder.encode(editText4.getText().toString().trim(), "UTF-8");
            working_hours = URLEncoder.encode(editText5.getText().toString().trim(), "UTF-8");
            service_1 = URLEncoder.encode(editText6.getText().toString().trim(), "UTF-8");
            service_2 = URLEncoder.encode(editText7.getText().toString().trim(), "UTF-8");
            service_3 = URLEncoder.encode(editText8.getText().toString().trim(), "UTF-8");
            service_4 = URLEncoder.encode(editText9.getText().toString().trim(), "UTF-8");
            other_services = URLEncoder.encode(editText10.getText().toString().trim(), "UTF-8");
            facebook_url = URLEncoder.encode(editText11.getText().toString().trim(), "UTF-8");
            twitter_url = URLEncoder.encode(editText12.getText().toString().trim(), "UTF-8");
            picassa_url = URLEncoder.encode(editText13.getText().toString().trim(), "UTF-8");

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
        if (password == null || password.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.password));
            return false;
        }
        if (desc == null || desc.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.short_description));
            return false;
        }
        if (countryId == null || countryId.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.country1));
            return false;
        }
        if (cityId == null || cityId.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.city));
            return false;
        }
        if (working_hours == null || working_hours.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.work_hour));
            return false;
        }
        if (service_1 == null || service_1.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.first_service));
            return false;
        }
        if (delivery == null || delivery.trim().isEmpty()){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.delivery_service));
            return false;
        }
        if (!password.equalsIgnoreCase(password_confirmation)){
            new SweetDialogHelper(this).showErrorMessage(getString(R.string.error), getString(R.string.password_not_equal));
            return false;
        }



        return true;

    }
}
