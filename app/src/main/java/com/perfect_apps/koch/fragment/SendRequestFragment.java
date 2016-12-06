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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.SweetDialogHelper;
import com.perfect_apps.koch.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
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

    private String title, details, cost;


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

        textViewDate.setText(Utils.returnDate());
        textViewTime.setText(Utils.returnTime());
        textViewName.setText(getArguments().getString("user_name", ""));

        Glide.with(getActivity())
                .load(getArguments().getString("user_avatar", ""))
                .placeholder(R.color.gray_btn_bg_color)
                .centerCrop()
                .crossFade()
                .thumbnail(0.1f)
                .into(circleImageView);
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
        switch (v.getId()){
            case R.id.button1:
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.are_sure))
                        .setContentText(getString(R.string.send_request))
                        .setConfirmText(getString(R.string.yess))
                        .setCancelText(getString(R.string.noo))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                sendRequest();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;
            case R.id.button2:

                break;
        }
    }

    private void sendRequest(){
        if (Utils.isOnline(getActivity())) {
            if (attempData()) {
                // Set up a progress dialog
                final SweetDialogHelper sdh = new SweetDialogHelper(getActivity());
                sdh.showMaterialProgress(getString(R.string.wait));

                // Tag used to cancel the request
                String tag_string_req = "string_req";
                String url = "http://services-apps.net/koch/api/set_request/provider";

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        sdh.dismissDialog();

                        JSONObject rootObject = null;
                        String row_hash = "";
                        try {
                            rootObject = new JSONObject(response);
                            JSONObject jsonObject = rootObject.optJSONObject("item");
                            row_hash = jsonObject.optString("row_hash");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("تم")
                                .setContentText("قمت بأرسال فاتوره برقم"+ " " + row_hash)
                                .show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sdh.dismissDialog();
                        // show error message
                        sdh.showErrorMessage(getString(R.string.error), getString(R.string.wrong_mail_or_password));
                    }
                }) {


                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", new KochPrefStore(getActivity()).getPreferenceValue(Constants.userEmail));
                        params.put("password", new KochPrefStore(getActivity()).getPreferenceValue(Constants.userPassword));
                        params.put("title", title);
                        params.put("details", details);
                        params.put("cost", cost);
                        params.put("client_id", Constants.sharedUserId);
                        return params;

                    }
                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        } else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("ناسف...")
                    .setContentText("هناك مشكله بشبكة الانترنت حاول مره اخرى")
                    .show();
        }
    }

    private boolean attempData(){
        title = editText1.getText().toString().trim();
        details = editText2.getText().toString().trim();
        cost = editText3.getText().toString().trim();

        if (title == null || title.trim().isEmpty()) {
            new SweetDialogHelper(getActivity()).showErrorMessage(getString(R.string.error), getString(R.string.title_of_request));
            return false;
        }
        if (details == null || details.trim().isEmpty()) {
            new SweetDialogHelper(getActivity()).showErrorMessage(getString(R.string.error), getString(R.string.details_of_request));
            return false;
        }
        if (cost == null || cost.trim().isEmpty()) {
            new SweetDialogHelper(getActivity()).showErrorMessage(getString(R.string.error), getString(R.string.cost_of_request));
            return false;
        }

        return true;

    }
}
