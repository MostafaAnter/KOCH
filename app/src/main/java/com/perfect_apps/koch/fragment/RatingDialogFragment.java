package com.perfect_apps.koch.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.perfect_apps.koch.BuildConfig;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by mostafa on 07/07/16.
 */
public class RatingDialogFragment extends DialogFragment implements View.OnClickListener {
    int mNum;


    @BindView(R.id.text1)
    TextView textView1;
    @BindView(R.id.closeDialog)
    ImageView close;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.button1)
    Button button1;

    // Container Activity must implement this interface
    public interface OnRateDone {
        public void onRateComplete();
    }

    OnRateDone callBack;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static RatingDialogFragment newInstance(int num) {
        RatingDialogFragment f = new RatingDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NO_TITLE, theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_rate, container, false);
        ButterKnife.bind(this, v);
        changeTextFont();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close.setOnClickListener(this);
        button1.setOnClickListener(this);

        callBack = (SenderDataFragment) getTargetFragment();

    }

    private void changeTextFont() {
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bold.ttf");
        textView1.setTypeface(fontBold);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeDialog:
                dismiss();
                break;
            case R.id.button1:
                doRate();
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


    private void doRate() {
        if (Utils.isOnline(getActivity())) {

            // Set up a progress dialog
            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("جارى تنفيذ طلبك...");
            pDialog.setCancelable(false);
            pDialog.show();

            // Tag used to cancel the request
            String tag_string_req = "string_req";
            String url = BuildConfig.API_BASE_URL + "do/rate";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    pDialog.dismissWithAnimation();
                    dismiss();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("تم")
                            .setContentText("لقد قمت بتقييم المعلم")
                            .show();
                    dismiss();

                    // TODO: 10/4/16 callback method
                    callBack.onRateComplete();


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismissWithAnimation();
                    dismiss();

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("نأسف")
                            .setContentText("يبدو انك قمت بتقييم هذا المعلم من قبل")
                            .show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", new KochPrefStore(getActivity()).getPreferenceValue(Constants.userEmail));
                    params.put("password", new KochPrefStore(getActivity()).getPreferenceValue(Constants.userPassword));
                    params.put("rate", ratingBar.getRating() + "");
                    params.put("provider_id", getArguments().getString("user_id"));
                    return params;

                }
            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("ناسف...")
                    .setContentText("هناك مشكله بشبكة الانترنت حاول مره اخرى")
                    .show();
        }
    }
}
