package com.perfect_apps.koch.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.perfect_apps.koch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mostafa_anter on 11/1/16.
 */

public class SendRequestFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.avatar)CircleImageView circleImageView;
    @BindView(R.id.name)
    TextView textViewName;
    @BindView(R.id.date)TextView textViewDate;
    @BindView(R.id.timestamp)TextView textViewTime;

    @BindView(R.id.text1)TextView textView1;
    @BindView(R.id.editText1)EditText editText1;
    @BindView(R.id.editText2)EditText editText2;
    @BindView(R.id.editText3)EditText editText3;

    @BindView(R.id.button1)Button button1;
    @BindView(R.id.button2)Button button2;

    public SendRequestFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_request_frame, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        changeTextFont();
    }

    private void changeTextFont() {
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");
        textViewName.setTypeface(fontBold);
        textView1.setTypeface(fontBold);
        editText1.setTypeface(font);
        editText2.setTypeface(font);
        editText3.setTypeface(font);

        button1.setTypeface(font);
        button2.setTypeface(font);
    }

    @Override
    public void onClick(View v) {

    }
}
