package com.perfect_apps.koch.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perfect_apps.koch.R;

/**
 * Created by mostafa_anter on 10/3/16.
 */

public class ProviderRequestFragment extends Fragment{
    public ProviderRequestFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_sent_request, container, false);
        return view;
    }
}