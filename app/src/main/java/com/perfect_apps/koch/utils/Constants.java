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
    public static final String PREFERENCE_LANGUAGE = "language";

    /**
     * util urls
     */
    public static final String registerClientURL = BuildConfig.API_BASE_URL + "register/client";
    public static final String registerProviderURL = BuildConfig.API_BASE_URL + "register/provider";
    public static final String countriesListURL = BuildConfig.API_BASE_URL + "registration/lists/countries";
    public static final String citiesListURL = BuildConfig.API_BASE_URL + "registration/lists/cities?country=";
}
