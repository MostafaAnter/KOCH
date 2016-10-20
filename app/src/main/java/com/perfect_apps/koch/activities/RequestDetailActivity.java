package com.perfect_apps.koch.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.perfect_apps.koch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestDetailActivity extends LocalizationActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
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

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2) Button button2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        ButterKnife.bind(this);

       setToolbar();
        changeFontOfText();

    }

    private void changeFontOfText(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
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

}
