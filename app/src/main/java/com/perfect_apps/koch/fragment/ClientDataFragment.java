package com.perfect_apps.koch.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.activities.ClientHomeActivity;
import com.perfect_apps.koch.activities.ProviderDetailActivity;
import com.perfect_apps.koch.activities.SignUpActivity;
import com.perfect_apps.koch.activities.SignUpClientActivity;
import com.perfect_apps.koch.activities.SplashActivity;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.models.ClientInfo;
import com.perfect_apps.koch.models.ProviderInfo;
import com.perfect_apps.koch.parser.JsonParser;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.MapHelper;
import com.perfect_apps.koch.utils.MapStateManager;
import com.perfect_apps.koch.utils.SweetDialogHelper;
import com.perfect_apps.koch.utils.Utils;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by mostafa_anter on 10/3/16.
 */

public class ClientDataFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback, View.OnClickListener{

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.text1) TextView textView1;
    @BindView(R.id.text2) TextView textView2;
    @BindView(R.id.text3) TextView textView3;
    @BindView(R.id.text4) TextView textView4;
    @BindView(R.id.text5) TextView textView5;
    @BindView(R.id.button1) Button button1;

    @BindView(R.id.imageView1)ImageView imageView1;
    @BindView(R.id.exit)LinearLayout linearLayoutExit;
    @BindView(R.id.editProfile)LinearLayout linearLayoutEdit;


    private GoogleMap mMap;
    private static final int GPS_ERRORDIALOG_REQUEST = 9001;

    // for client data info
    private ClientInfo clientInfo;

    // for fetch last location
    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    // for draw markers
    private List<Marker> markers;

    public ClientDataFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setup markers
        this.markers = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clint_data_tab, container, false);
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
        button1.setOnClickListener(this);
        linearLayoutEdit.setOnClickListener(this);
        linearLayoutExit.setOnClickListener(this);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){

            getClientData();

            // Check if has GPS
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }


            // Create an instance of GoogleAPIClient.
            if (Utils.isOnline(getActivity())) {
                if (mGoogleApiClient == null) {
                    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .build();
                }
            }

            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }

        }
    }

    private void changeFontOfText(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normal.ttf");
        textView1.setTypeface(font);
        textView2.setTypeface(font);
        textView3.setTypeface(font);
        textView4.setTypeface(font);
        textView5.setTypeface(font);
        button1.setTypeface(font);

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

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        button1.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void getClientData(){
        if (Utils.isOnline(getActivity())) {
            // Set up a progress dialog
            final SweetDialogHelper sdh = new SweetDialogHelper(getActivity());
            sdh.showMaterialProgress(getString(R.string.wait));

            // Tag used to cancel the request
            String tag_string_req = "string_req";
            String url = Constants.getClientInfo;

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    response = StringEscapeUtils.unescapeJava(response);
                    clientInfo = JsonParser.parseClientInfo(response);
                    bindData();
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
                    params.put("user_id", new KochPrefStore(getActivity()).getPreferenceValue(Constants.userId));
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

    private void bindData(){

        textView2.setText(clientInfo.getName());
        if (clientInfo.getImage_full_path() != null && !clientInfo.getImage_full_path().trim().isEmpty())
            Glide.with(getActivity())
                    .load(clientInfo.getImage_full_path())
                    .placeholder(R.color.gray_btn_bg_color)
                    .centerCrop()
                    .crossFade()
                    .thumbnail(0.1f)
                    .into(imageView1);
        textView4.setText(clientInfo.getMobile());
        textView5.setText(clientInfo.getEmail());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                if (mLastLocation != null)
                viewAllProviders(String.valueOf(mLastLocation.getLatitude()),
                        String.valueOf(mLastLocation.getLongitude()));
                break;
            case R.id.exit:
                new KochPrefStore(getActivity()).clearPreference();
                startActivity(new Intent(getActivity(), SplashActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                break;
            case R.id.editProfile:
                Intent intent = new Intent(getActivity(), SignUpClientActivity.class);
                intent.putExtra("flag", "edit");
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        updateCurrentLocationData();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void buildAlertMessageNoGps() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.open_gps))
                .setContentText(getString(R.string.why_open_gps))
                .setConfirmText(getString(R.string.yes_open_gps))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    private void updateCurrentLocationData() {
        if (mLastLocation != null && mMap != null) {
            // save user location
            new KochPrefStore(getActivity()).addPreference(Constants.userLastLocationLat, String.valueOf(mLastLocation.getLatitude()));
            new KochPrefStore(getActivity()).addPreference(Constants.userLastLocationLng, String.valueOf(mLastLocation.getLongitude()));
            // draw user marker
            Marker marker = MapHelper.setUpMarkerAndReturnMarker(mMap, new LatLng(mLastLocation.getLatitude(),
                    mLastLocation.getLongitude()), R.drawable.map_user_marker);
            markers.add(marker);
            try {
                getAddressInfo(new LatLng(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // upload user location to server

        } else {
            String lat = new KochPrefStore(getActivity()).getPreferenceValue(Constants.userLastLocationLat);
            String lng = new KochPrefStore(getActivity()).getPreferenceValue(Constants.userLastLocationLng);
            if (!lat.trim().isEmpty() && !lng.trim().isEmpty()) {
                // draw user marker
                MapHelper.setUpMarker(mMap, new LatLng(Double.valueOf(lat),
                        Double.valueOf(lng)), R.drawable.map_user_marker);
                try {
                    getAddressInfo(new LatLng(Double.valueOf(lat),
                            Double.valueOf(lng)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            new UpdateCurrentLocTask().execute();
        }
    }

    private class UpdateCurrentLocTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateCurrentLocationData();

        }
    }

    private void getAddressInfo(LatLng latLng) throws IOException {

//        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//        List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//
//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//
//        StringBuilder sb = new StringBuilder();
//
//        if (address != null)
//            sb.append(address);
//        if (city != null)
//            sb.append(", " + city);
//        if (state != null)
//            sb.append(", " + state);
//        if (country != null)
//            sb.append(", " + country);
//        if (knownName != null)
//            sb.append(", " + knownName);
//
//        textView1.setText(sb);
//
//        Log.e("address info", sb.toString());

        button1.setVisibility(View.VISIBLE);
        // uploadLocationToServer(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), sb.toString());
    }

    private void viewAllProviders(final String lat, final String lng) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = Constants.viewAllProviders + "?lat=" + lat + "&lng=" + lng + "&distance=5";
        final SweetDialogHelper sdh = new SweetDialogHelper(getActivity());
        sdh.showMaterialProgress(getString(R.string.wait));
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = StringEscapeUtils.unescapeJava(response);
                Log.d("view all providers", response);
                sdh.dismissDialog();
                drawListOfProviders(JsonParser.parseNearProviders(response));

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error view providers", error.toString());
                sdh.dismissDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void drawListOfProviders(List<ProviderInfo> mList){
        for (ProviderInfo provider:
                mList) {
            // draw user marker
            drawMarker(provider);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        mMap.animateCamera(cu);
    }

    private void drawMarker(final ProviderInfo providerInfo){
        // Add a new marker to the map
        Marker marker = mMap.addMarker(new MarkerOptions()
                .title(providerInfo.getUsername())
                .snippet(providerInfo.getDesc() + "\n" + providerInfo.getService_1() + "\n" + providerInfo.getService_2() +
                        "\n" + providerInfo.getService_3() + "\n" + providerInfo.getService_4() + "\n" + providerInfo.getOther_services())
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_market_marker)))
                .position(new LatLng(Double.valueOf(providerInfo.getAddresslat()),
                        Double.valueOf(providerInfo.getAddresslng()))).draggable(true));
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getActivity());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getActivity());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getActivity());
                snippet.setTextColor(Color.RED);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        markers.add(marker);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(getActivity(), providerInfo.getUsername(), Toast.LENGTH_SHORT).show();
                marker.setPosition(new LatLng(Double.valueOf(providerInfo.getAddresslat()), Double.valueOf(providerInfo.getAddresslng())));
                startActivity(new Intent(getActivity(), ProviderDetailActivity.class));
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
    }
}
