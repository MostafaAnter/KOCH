package com.perfect_apps.koch.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;
import com.perfect_apps.koch.BuildConfig;
import com.perfect_apps.koch.R;
import com.perfect_apps.koch.app.AppController;
import com.perfect_apps.koch.services.NotificationEvent;
import com.perfect_apps.koch.store.KochPrefStore;
import com.perfect_apps.koch.utils.Constants;
import com.perfect_apps.koch.utils.CustomTypefaceSpan;
import com.perfect_apps.koch.utils.MapHelper;
import com.perfect_apps.koch.utils.MapStateManager;
import com.perfect_apps.koch.utils.SweetDialogHelper;
import com.perfect_apps.koch.utils.Utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProviderHomeActivity extends LocalizationActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text1)
    TextView textView1;
    @BindView(R.id.text2)
    TextView textView2;

    private NavigationView navigationView;

    // for map
    private GoogleMap mMap;
    private static final int GPS_ERRORDIALOG_REQUEST = 9001;


    // for fetch last location
    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    // for message count
    LinearLayout messageCountView;
    TextView messageCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home);
        ButterKnife.bind(this);
        setToolbar();


        // for map
        if (servicesOK()) {
            initMap();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        changeFontOfNavigation();

        changeFontOfText();


        // Check if has GPS
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           // buildAlertMessageNoGps();
        }


        // Create an instance of GoogleAPIClient.
        if (Utils.isOnline(this)) {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
        }

        // push token
        pushToken();
    }

    private void changeFontOfText() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        textView1.setTypeface(font);
        textView2.setTypeface(font);
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

        messageCountView = (LinearLayout) toolbar.findViewById(R.id.messageCountView);
        messageCount = (TextView) toolbar.findViewById(R.id.messageCount);
        messageCountView.setVisibility(View.GONE);

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
                startActivity(new Intent(ProviderHomeActivity.this, ProviderProfileActivity.class));
            }
        });

        messagesIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderHomeActivity.this, ProviderProfileActivity.class);
                intent.putExtra("messageTab", true);
                startActivity(intent);


            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_app) {
            startActivity(new Intent(this, AboutActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_share_app) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "KOCH");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.perfect_apps.koch \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }

        } else if (id == R.id.nav_translate) {
            showSingleChoiceListLangaugeAlertDialog();

        } else if (id == R.id.nav_call_us) {
            startActivity(new Intent(this, ContactUsActivity.class));

        } else if (id == R.id.sign_out) {
           changeState("0");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeState(final String status){
        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = "http://services-apps.net/koch/api/set/status/provider";
        final SweetDialogHelper sweetDialogHelper = new SweetDialogHelper(this);
        sweetDialogHelper.showMaterialProgress(getString(R.string.loading));
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                sweetDialogHelper.dismissDialog();
                Log.d("change_state", response);
                new KochPrefStore(ProviderHomeActivity.this).clearPreference();
                startActivity(new Intent(ProviderHomeActivity.this, SplashActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                sweetDialogHelper.dismissDialog();
                Log.d("error upload client loc", error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", new KochPrefStore(ProviderHomeActivity.this).getPreferenceValue(Constants.userEmail));
                params.put("password", new KochPrefStore(ProviderHomeActivity.this).getPreferenceValue(Constants.userPassword));
                params.put("status", status);
                return params;

            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //change font of drawer
    private void changeFontOfNavigation() {
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/normal.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private String mCheckedItem;

    public void showSingleChoiceListLangaugeAlertDialog() {
        final String[] list = new String[]{getString(R.string.language_arabic), getString(R.string.language_en)};
        int checkedItemIndex;

        switch (getLanguage()) {
            case "en":
                checkedItemIndex = 1;
                break;
            default:
                checkedItemIndex = 0;

        }
        mCheckedItem = list[checkedItemIndex];

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.language))
                .setSingleChoiceItems(list,
                        checkedItemIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCheckedItem = list[which];
                                if (which == 0) {
                                    setLanguage("ar");
                                    changeFirstTimeOpenAppState(4);
                                    dialog.dismiss();
                                } else if (which == 1) {
                                    setLanguage("en");
                                    changeFirstTimeOpenAppState(5);
                                    dialog.dismiss();
                                }
                            }
                        })
                .show();
    }

    private void changeFirstTimeOpenAppState(int language) {
        new KochPrefStore(this).addPreference(Constants.PREFERENCE_FIRST_TIME_OPEN_APP_STATE, 1);
        new KochPrefStore(this).addPreference(Constants.PREFERENCE_LANGUAGE, language);
    }

    // setup map
    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initMap() {
        if (mMap == null) {
            SupportMapFragment mapFrag =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
        }
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        if (mGoogleApiClient != null)
        mGoogleApiClient.disconnect();
        super.onStop();
        if (mMap != null) {
            mMap.clear();
            MapStateManager mgr = new MapStateManager(this);
            mgr.saveMapState(mMap);
        }
    }

    @Override
    public void onResume() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onResume();
        MapStateManager mgr = new MapStateManager(this);
        CameraPosition position = mgr.getSavedCameraPosition();
        if (position != null && mMap != null) {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mMap.moveCamera(update);
            mMap.setMapType(mgr.getSavedMapType());
        }


        // get Message count
        getMessageCount(new KochPrefStore(this).getPreferenceValue(Constants.userEmail),
                new KochPrefStore(this).getPreferenceValue(Constants.userPassword));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void updateCurrentLocationData() {
        if (mLastLocation != null && mMap != null) {
            // save user location
            new KochPrefStore(this).addPreference(Constants.userLastLocationLat, String.valueOf(mLastLocation.getLatitude()));
            new KochPrefStore(this).addPreference(Constants.userLastLocationLng, String.valueOf(mLastLocation.getLongitude()));
            // draw user marker
            MapHelper.setUpMarker(mMap, new LatLng(mLastLocation.getLatitude(),
                    mLastLocation.getLongitude()), R.drawable.map_user_marker);
            try {
                getAddressInfo(new LatLng(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // upload user location to server

        } else {
            String lat = new KochPrefStore(this).getPreferenceValue(Constants.userLastLocationLat);
            String lng = new KochPrefStore(this).getPreferenceValue(Constants.userLastLocationLng);
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
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("connection", "suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("connection", "failed");

    }

    private void buildAlertMessageNoGps() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
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

    private void getAddressInfo(LatLng latLng) throws IOException {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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

        if (!(new KochPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_UPLOAD_LOCATIONS_TIMES) == 1))
        uploadLocationToServer(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), sb.toString());
    }

    private void uploadLocationToServer(final String lat,
                                        final String lng, final String address) {
        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = Constants.providerUploadLoc;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("upload client loc", response);
                new KochPrefStore(ProviderHomeActivity.this).addPreference(Constants.PREFERENCE_UPLOAD_LOCATIONS_TIMES, 1);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error upload client loc", error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", new KochPrefStore(ProviderHomeActivity.this).getPreferenceValue(Constants.userEmail));
                params.put("password", new KochPrefStore(ProviderHomeActivity.this).getPreferenceValue(Constants.userPassword));
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("name", address);
                return params;

            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void pushToken() {
        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = BuildConfig.API_BASE_URL + "token/add";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("push_token_response", response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token_id", FirebaseInstanceId.getInstance().getToken());
                params.put("email", new KochPrefStore(ProviderHomeActivity.this).getPreferenceValue(Constants.userEmail));
                params.put("password", new KochPrefStore(ProviderHomeActivity.this).getPreferenceValue(Constants.userPassword));
                return params;

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getMessageCount(String email, String password) {
        String url = BuildConfig.API_BASE_URL + "message/show/count?email=" + email + "&password=" + password;


        if (Utils.isOnline(this)) {
            // Tag used to cancel the request
            String tag_string_req = "string_req";

            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    response = StringEscapeUtils.unescapeJava(response);
                    // do some thing here
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String count = jsonObject.optString("count");
                        if (Integer.valueOf(count) > 0) {
                            messageCountView.setVisibility(View.VISIBLE);
                            messageCount.setText(count);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("teeest", response);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }


    @Subscribe
    public void onMessageEvent(NotificationEvent event) {
            getMessageCount(new KochPrefStore(this).getPreferenceValue(Constants.userEmail),
                    new KochPrefStore(this).getPreferenceValue(Constants.userPassword));
    }

    @Override
    protected void onPause() {
        super.onPause();
        messageCountView.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
}
