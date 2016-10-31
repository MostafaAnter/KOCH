package com.perfect_apps.koch.store;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mostafa_anter on 9/26/16.
 */

public class KochPrefStore {
    private static final String PREFKEY = "KochPreferencesStore";
    private SharedPreferences kochPreferences;

    public KochPrefStore(Context context){
        kochPreferences = context.getSharedPreferences(PREFKEY, Context.MODE_PRIVATE);
    }

    public void clearPreference(){
        SharedPreferences.Editor editor = kochPreferences.edit();
        editor.clear().apply();
    }

    public void addPreference(String key, String value){
        SharedPreferences.Editor editor = kochPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void addPreference(String key, int value){
        SharedPreferences.Editor editor = kochPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void removePreference(String key){
        SharedPreferences.Editor editor = kochPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public String getPreferenceValue(String key){
        return kochPreferences.getString(key, "");
    }

    public int getIntPreferenceValue(String key){
        return kochPreferences.getInt(key, 0);
    }
}
