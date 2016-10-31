package com.perfect_apps.koch.parser;

import com.perfect_apps.koch.models.Cities;
import com.perfect_apps.koch.models.Countries;
import com.perfect_apps.koch.models.ProviderInfo;

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

    public static List<ProviderInfo> parseNearProviders(String feed){
        try {
            JSONObject jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("data");
            List<ProviderInfo> brandList = new ArrayList<>();
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);

                String user_id = jsonObject.optString("user_id");
                String addressName = jsonObject.optString("name");
                String lat = jsonObject.optString("lat");
                String lng = jsonObject.optString("lng");
                String image_full_path = jsonObject.optString("image_full_path");

                JSONObject userData = jsonObject.optJSONObject("user");
                String userName = userData.optString("name");
                String email = userData.optString("email");
                String mobile = userData.optString("mobile");
                String is_active = userData.optString("is_active");
                String desc = userData.optString("desc");

                JSONObject provider_info = userData.optJSONObject("provider_info");
                String country_id = provider_info.optString("country_id");
                String city_id = provider_info.optString("city_id");
                String working_hours = provider_info.optString("working_hours");
                String service_1 = provider_info.optString("service_1");
                String service_2 = provider_info.optString("service_2");
                String service_3 = provider_info.optString("service_3");
                String service_4 = provider_info.optString("service_4");
                String other_services = provider_info.optString("other_services");
                String delivery = provider_info.optString("delivery");
                String facebook_url = provider_info.optString("facebook_url");
                String twitter_url = provider_info.optString("twitter_url");
                String picassa_url = provider_info.optString("picassa_url");


                brandList.add(new ProviderInfo(user_id, userName, mobile, is_active, desc, country_id, city_id,
                        working_hours, service_1, service_2, service_3,service_4, other_services,
                        delivery, facebook_url, twitter_url, picassa_url, image_full_path, addressName,
                        lat, lng));
            }
            return brandList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}