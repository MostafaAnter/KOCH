package com.perfect_apps.koch.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perfect_apps.koch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mostafa_anter on 10/31/16.
 */

public class SenderDataFragment extends Fragment{

    @BindView(R.id.text1)
    TextView textView1 ;
    @BindView(R.id.text2) TextView textView2 ;
    @BindView(R.id.text3) TextView textView3 ;
    @BindView(R.id.text4) TextView textView4 ;
    @BindView(R.id.text5) TextView textView5 ;
    @BindView(R.id.text6) TextView textView6 ;
    @BindView(R.id.text7) TextView textView7 ;
    @BindView(R.id.text8) TextView textView8 ;
    @BindView(R.id.text9) TextView textView9 ;
    @BindView(R.id.text10) TextView textView10 ;
    @BindView(R.id.text11) TextView textView11 ;
    @BindView(R.id.text12) TextView textView12 ;
    @BindView(R.id.text13) TextView textView13 ;
    @BindView(R.id.text14) TextView textView14 ;
    @BindView(R.id.text15) TextView textView15 ;
    @BindView(R.id.text16) TextView textView16 ;
    @BindView(R.id.text17) TextView textView17 ;
    @BindView(R.id.text18) TextView textView18 ;

    public SenderDataFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sender_data_tab, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void changeTextFont() {
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");


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
        textView13.setTypeface(font);
        textView14.setTypeface(fontBold);
        textView15.setTypeface(font);
        textView16.setTypeface(fontBold);
        textView17.setTypeface(font);
        textView18.setTypeface(fontBold);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeTextFont();
    }

}
