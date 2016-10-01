package com.perfect_apps.koch.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.perfect_apps.koch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends LocalizationActivity {

    @BindView(R.id.radioButton1) RadioButton radioButton1;
    @BindView(R.id.radioButton2) RadioButton radioButton2;

    @BindView(R.id.username_input) EditText userNameInput;
    @BindView(R.id.password_input) EditText passwordInput;

    @BindView(R.id.loginButton) Button loginButton;

    @BindView(R.id.text1) TextView textView1;
    @BindView(R.id.text2) TextView textView2;
    @BindView(R.id.text3) TextView textView3;
    @BindView(R.id.text4) TextView textView4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage("ar");
        setContentView(R.layout.activity_sign_in);
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


    public void signUp(View view) {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }
}
