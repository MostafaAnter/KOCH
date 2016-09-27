package com.perfect_apps.koch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends LocalizationActivity {

    @BindView(R.id.fakeView) View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set default language of activity
        setDefaultLanguage("en");

        if(checkFirstTimeOpenApp() == 0){
            setLanguage(Locale.getDefault().getLanguage());
        }else {
            if (new KochPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_LANGUAGE) == 4){
                setLanguage("ar");
            }else if (new KochPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_LANGUAGE) == 5) {
                setLanguage("en");
            }else {
                setLanguage(Locale.getDefault().getLanguage());
            }
        }

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Animation fade0 = AnimationUtils.loadAnimation(this, R.anim.fade_in_enter);

        view.startAnimation(fade0);
        fade0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // do some thing
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private int checkFirstTimeOpenApp(){
        return new KochPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_FIRST_TIME_OPEN_APP_STATE);
    }
}
