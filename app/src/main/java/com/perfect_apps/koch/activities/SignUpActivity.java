package com.perfect_apps.koch.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.perfect_apps.koch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setToolbar();
        changeTextFont();
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


    }

    public void onCheckboxClicked(View view) {

    }
}
