package com.perfect_apps.koch.parser;

import com.perfect_apps.koch.models.Cities;
import com.perfect_apps.koch.models.Countries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mostafa_anter on 10/27/16.
 */

public class JsonParser {
    public static List<Countries> parseCountriesFeed(String feed){
        try {
            JSONObject jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("countries");
            List<Countries> brandList = new ArrayList<>();
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String name = jsonObject.optString("name");
                brandList.add(new Countries(id, name));
            }
            return brandList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<Cities> parseCitiesFeed(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("cities");
            List<Cities> brandList = new ArrayList<>();
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String name = jsonObject.optString("name");
                brandList.add(new Cities(id, name));
            }
            return brandList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
