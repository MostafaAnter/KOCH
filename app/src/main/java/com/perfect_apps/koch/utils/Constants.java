package com.perfect_apps.koch.utils;

import com.perfect_apps.koch.BuildConfig;

/**
 * Created by mostafa_anter on 9/26/16.
 */

public class Constants {
    /**
     * util strings
     */
    public static final String PREFERENCE_FIRST_TIME_OPEN_APP_STATE = "first_time_open_app";
    public static final String PREFERENCE_UPLOAD_LOCATIONS_TIMES = "upload_location_times";
    public static final String PREFERENCE_LANGUAGE = "language";

    /**
     * util urls
     */
    public static final String registerClientURL = BuildConfig.API_BASE_URL + "register/client";
    public static final String registerProviderURL = BuildConfig.API_BASE_URL + "register/provider";
    public static final String countriesListURL = BuildConfig.API_BASE_URL + "registration/lists/countries";
    public static final String citiesListURL = BuildConfig.API_BASE_URL + "registration/lists/cities?country=";
    public static final String providerLoginUrl = BuildConfig.API_BASE_URL + "login/provider";
    public static final String clientLoginUrl = BuildConfig.API_BASE_URL + "login/client";
    public static final String clientUploadLoc = BuildConfig.API_BASE_URL + "set_location/client";
    public static final String providerUploadLoc = BuildConfig.API_BASE_URL + "set_location/provider";
    public static final String viewAllProviders = BuildConfig.API_BASE_URL + "location/show/provider";


    /**
     * user preferences
     */
    public static final String userId = "userId";
    public static final String userName = "userName";
    public static final String userEmail = "userEmail";
    public static final String userPassword = "userPassword";
    public static final String userAvatarUrl = "userAvatar";
    public static final String userGroupId = "groupId";
    public static final String userLastLocationLat = "userLastLocationLat";
    public static final String userLastLocationLng = "userLastLocationLng";
}
