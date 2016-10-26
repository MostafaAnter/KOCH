package com.perfect_apps.koch.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.adapters.CitiesAdapter;
import com.perfect_apps.koch.adapters.CountriesAdapter;
import com.perfect_apps.koch.models.Cities;
import com.perfect_apps.koch.models.CitiesResponse;
import com.perfect_apps.koch.models.Countries;
import com.perfect_apps.koch.models.CountriesResponse;
import com.perfect_apps.koch.rest.ApiClient;
import com.perfect_apps.koch.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends LocalizationActivity {
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

    @BindView(R.id.checkbox1)
    CheckBox checkBox1;

    @BindView(R.id.button1)
    Button button1;

    @BindView(R.id.spinner1)
    Spinner spinner1;
    @BindView(R.id.spinner2)
    Spinner spinner2;



    private String countryId;
    private String cityId;


    // initiate inter face for use retrofit
    private ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setToolbar();
        changeTextFont();
        getCountries();

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

    }

    private void getCountries(){
        Call<CountriesResponse> call = apiService.getCountries();
        call.enqueue(new Callback<CountriesResponse>() {
            int tryTime = 0;
            @Override
            public void onResponse(Call<CountriesResponse> call, Response<CountriesResponse> response) {
                populateSpinner1(response.body().getCountries());
            }

            @Override
            public void onFailure(Call<CountriesResponse> call, Throwable t) {
                if (tryTime < 1){
                    getCountries();
                    tryTime++;
                }

            }
        });
    }

    private void getCities(String countryId){
        Call<CitiesResponse> call = apiService.getCities(countryId);
        call.enqueue(new Callback<CitiesResponse>() {
            @Override
            public void onResponse(Call<CitiesResponse> call, Response<CitiesResponse> response) {
                populateSpinner2(response.body().getCities());
            }

            @Override
            public void onFailure(Call<CitiesResponse> call, Throwable t) {

            }
        });

    }
}
