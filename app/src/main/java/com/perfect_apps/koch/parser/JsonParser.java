package com.perfect_apps.koch.parser;

import com.perfect_apps.koch.BuildConfig;
import com.perfect_apps.koch.models.Cities;
import com.perfect_apps.koch.models.ClientInfo;
import com.perfect_apps.koch.models.ConversationItem;
import com.perfect_apps.koch.models.Countries;
import com.perfect_apps.koch.models.InboxItem;
import com.perfect_apps.koch.models.OrderRequest;
import com.perfect_apps.koch.models.ProviderInfo;
import com.perfect_apps.koch.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mostafa_anter on 10/27/16.
 */

public class JsonParser {
    public static List<Countries> parseCountriesFeed(String feed) {
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

    public static List<Cities> parseCitiesFeed(String feed) {
        try {
            JSONObject jsonRootObject = new JSONObject(feed);
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

    public static List<ProviderInfo> parseNearProviders(String feed) {
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
                // edit here
                String country_name = provider_info.optString("country_name");
                String city_name = provider_info.optString("city_name");
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


                brandList.add(new ProviderInfo(user_id, userName, email, mobile, is_active,
                        desc, country_id, city_id, country_name, city_name,
                        working_hours, service_1, service_2, service_3, service_4, other_services,
                        delivery, facebook_url, twitter_url, picassa_url, image_full_path, addressName,
                        lat, lng));
            }
            return brandList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static ProviderInfo parseProviderInfo(String feed) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(feed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject item = jsonObject.optJSONObject("item");

        String id = item.optString("id");
        String name = item.optString("name");
        String email = item.optString("email");
        String mobile = item.optString("mobile");/*dummy*/
        String desc = item.optString("desc");
        String is_active = item.optString("is_available");
        String image_full_path = item.optString("image_full_path");

        JSONArray jsonArray = item.optJSONArray("locations");
        JSONObject locationInfo = jsonArray.optJSONObject(jsonArray.length() - 1);
        String addressName = locationInfo.optString("name");
        String lat = locationInfo.optString("lat");
        String lng = locationInfo.optString("lng");

        JSONObject provider_info = item.optJSONObject("provider_info");

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
        JSONObject country = provider_info.optJSONObject("country");
        JSONObject city = provider_info.optJSONObject("city");

        String countryId = country.optString("id");
        String cityId = city.optString("id");
        String countryName = country.optString("name");
        String cityName = city.optString("name");

        return new ProviderInfo(id, name, email, mobile, is_active, desc, countryId, cityId, countryName, cityName,
                working_hours, service_1, service_2, service_3, service_4, other_services, delivery, facebook_url,
                twitter_url, picassa_url, image_full_path, addressName, lat, lng);
    }

    public static ClientInfo parseClientInfo(String feed) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(feed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject item = jsonObject.optJSONObject("item");

        String id = item.optString("id");
        String name = item.optString("name");
        String email = item.optString("email");
        String mobile = item.optString("mobile");/*dummy*/
        String desc = item.optString("desc");
        String is_active = item.optString("is_active");
        String image_full_path = item.optString("image_full_path");

        JSONArray jsonArray = item.optJSONArray("locations");
        JSONObject locationInfo = jsonArray.optJSONObject(jsonArray.length() - 1);
        String addressName = locationInfo.optString("name");
        String lat = locationInfo.optString("lat");
        String lng = locationInfo.optString("lng");

        return new ClientInfo(id, name, email , mobile,image_full_path, addressName, lat, lng);
    }

    public static List<InboxItem> parseMyMessages(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("data");
            List<InboxItem> teacherItems = new ArrayList<>();
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String message = jsonObject.optString("message");
                JSONObject createAt = jsonObject.optJSONObject("created_at");
                String created_at = createAt.optString("date");
                String new_count = jsonObject.optString("new_count");
                JSONObject sender = jsonObject.optJSONObject("sender");
                String name = sender.optString("name");
                String group_id  = sender.optString("group_id");
                String user_id = sender.optString("id");
                String image_full_path = sender.optString("image_full_path");
                teacherItems.add(new InboxItem(name, id, user_id, message,
                        Utils.manipulateDateFormat(created_at),
                        image_full_path , new_count, group_id));
            }
            return teacherItems;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<ConversationItem> parseConversation(String feed){
        try {
            JSONObject  jsonRootObject = new JSONObject(feed);
            JSONArray jsonMoviesArray = jsonRootObject.optJSONArray("data");
            List<ConversationItem> teacherItems = new ArrayList<>();
            for (int i = 0; i < jsonMoviesArray.length(); i++) {
                JSONObject jsonObject = jsonMoviesArray.getJSONObject(i);
                String message = jsonObject.optString("message");
                JSONObject createAt = jsonObject.optJSONObject("created_at");
                String created_at = Utils.manipulateDateFormat(createAt.optString("date"));


                JSONObject sender = jsonObject.optJSONObject("sender");
                String email = sender.optString("email");
                String image_full_path = sender.optString("image_full_path");
                int group_id = sender.optInt("group_id");

                JSONArray message_users  = jsonObject.optJSONArray("message_users");
                JSONObject message_user = message_users.getJSONObject(0);
                String user_id = message_user.optString("user_id");
                String user_to_id = message_user.optString("user_to_id");
                String is_seen = message_user.optString("is_seen");

                boolean show =false;
                if (is_seen.equalsIgnoreCase("1")){
                    show = true;
                }

                teacherItems.add(new ConversationItem(image_full_path, message, show, created_at, group_id, email, user_id, user_to_id));
            }
            Collections.reverse(teacherItems);
            return teacherItems;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<OrderRequest> parseOrderRequest(String feed) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(feed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       JSONArray jsonArray = jsonObject.optJSONArray("data");
        List<OrderRequest> orderRequestList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length() ; i++){
            JSONObject data = jsonArray.optJSONObject(i);
            String title = data.optString("title");
            String detail = data.optString("details");
            String cost = data.optString("cost");
            String row_hash = data.optString("row_hash");
            String status = data.optString("status");
            String updated_at = Utils.manipulateDateFormat(data.optString("updated_at"));
            JSONObject providerObj = data.optJSONObject("user");
            String providerName = providerObj.optString("name");
            String providerImage = "http://services-apps.net/koch/assets/uploads/users/" + providerObj.optString("image");
            JSONObject clientObj = data.optJSONObject("client_user");

            String clientName = clientObj.optString("name");
            String clientImage = "http://services-apps.net/koch/assets/uploads/users/" + clientObj.optString("image");

            orderRequestList.add(new OrderRequest(title, detail,cost, row_hash, status, updated_at, providerName, providerImage, clientName,
                    clientImage));
        }

        return orderRequestList;
    }
}
