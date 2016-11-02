package com.perfect_apps.koch.fragment;

import android.app.Dialog;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.models.ProviderInfo;
import com.perfect_apps.koch.parser.JsonParser;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.MapHelper;
import com.perfect_apps.koch.utils.SweetDialogHelper;
import com.perfect_apps.koch.utils.Utils;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by mostafa_anter on 10/31/16.
 */

public class SenderLocationFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.map)
    MapView mapView;

    @BindView(R.id.text1)
    TextView textView1;


    private GoogleMap mMap;
    private static final int GPS_ERRORDIALOG_REQUEST = 9001;

    public SenderLocationFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sender_location, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // for map
        if (servicesOK()) {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        changeFontOfText();
    }

    private void changeFontOfText(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        textView1.setTypeface(font);

    }

    // setup map
    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, getActivity(), GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(getActivity(), "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (!Constants.sharedUserlat.trim().isEmpty()) {
            MapHelper.setUpMarker(mMap, new LatLng(Double.valueOf(Constants.sharedUserlat),
                    Double.valueOf(Constants.sharedUserlng)), R.drawable.map_user_marker);
            try {
                getAddressInfo(new LatLng(Double.valueOf(Constants.sharedUserlat),
                        Double.valueOf(Constants.sharedUserlng)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getProviderDate();
        }


    }

    private void getAddressInfo(LatLng latLng) throws IOException {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        StringBuilder sb = new StringBuilder();

        if (address != null)
            sb.append(address);
        if (city != null)
            sb.append(", " + city);
        if (state != null)
            sb.append(", " + state);
        if (country != null)
            sb.append(", " + country);
        if (knownName != null)
            sb.append(", " + knownName);

        textView1.setText(sb);

        Log.e("address info", sb.toString());


    }

    private void getProviderDate(){
        if (Utils.isOnline(getActivity())) {
            // Set up a progress dialog
            final SweetDialogHelper sdh = new SweetDialogHelper(getActivity());
            sdh.showMaterialProgress(getString(R.string.wait));

            // Tag used to cancel the request
            String tag_string_req = "string_req";
            String url = Constants.getProviderInfo;

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    response = StringEscapeUtils.unescapeJava(response);
                    ProviderInfo providerInfo = JsonParser.parseProviderInfo(response);


                    MapHelper.setUpMarker(mMap, new LatLng(Double.valueOf(providerInfo.getAddresslat()),
                            Double.valueOf(providerInfo.getAddresslng())), R.drawable.map_user_marker);
                    try {
                        getAddressInfo(new LatLng(Double.valueOf(providerInfo.getAddresslat()),
                                Double.valueOf(providerInfo.getAddresslng())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    sdh.dismissDialog();
                    Log.d("response", response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    sdh.dismissDialog();
                    // show error message
                    sdh.showErrorMessage(getString(R.string.error), getString(R.string.try_again));
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", Constants.sharedUserId);
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
