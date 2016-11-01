package com.perfect_apps.koch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mostafa_anter on 10/31/16.
 */

public class ProviderInfo implements Parcelable {
    private String userId;
    private String username;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String mobile;
    private String isActive;
    private String desc;
    private String country_id;
    private String city_id;

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    private String country_name;
    private String city_name;
    private String working_hours;
    private String service_1;
    private String service_2;
    private String service_3;
    private String service_4;
    private String other_services;
    private String delivery;
    private String facebook_url;
    private String twitter_url;
    private String picassa_url;
    private String image_full_path;
    private String addressName;
    private String addresslat;
    private String addresslng;

    public ProviderInfo(){

    }

    public ProviderInfo(String userId, String username, String email,
                        String mobile, String isActive, String desc, String country_id,
                        String city_id, String country_name, String city_name,
                        String working_hours, String service_1, String service_2, String service_3, String service_4, String other_services, String delivery, String facebook_url, String twitter_url, String picassa_url, String image_full_path, String addressName, String addresslat, String addresslng) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.isActive = isActive;
        this.desc = desc;
        this.country_id = country_id;
        this.city_id = city_id;
        this.country_name = country_name;
        this.city_name = city_name;
        this.working_hours = working_hours;
        this.service_1 = service_1;
        this.service_2 = service_2;
        this.service_3 = service_3;
        this.service_4 = service_4;
        this.other_services = other_services;
        this.delivery = delivery;
        this.facebook_url = facebook_url;
        this.twitter_url = twitter_url;
        this.picassa_url = picassa_url;
        this.image_full_path = image_full_path;
        this.addressName = addressName;
        this.addresslat = addresslat;
        this.addresslng = addresslng;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(String working_hours) {
        this.working_hours = working_hours;
    }

    public String getService_1() {
        return service_1;
    }

    public void setService_1(String service_1) {
        this.service_1 = service_1;
    }

    public String getService_2() {
        return service_2;
    }

    public void setService_2(String service_2) {
        this.service_2 = service_2;
    }

    public String getService_3() {
        return service_3;
    }

    public void setService_3(String service_3) {
        this.service_3 = service_3;
    }

    public String getService_4() {
        return service_4;
    }

    public void setService_4(String service_4) {
        this.service_4 = service_4;
    }

    public String getOther_services() {
        return other_services;
    }

    public void setOther_services(String other_services) {
        this.other_services = other_services;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getFacebook_url() {
        return facebook_url;
    }

    public void setFacebook_url(String facebook_url) {
        this.facebook_url = facebook_url;
    }

    public String getTwitter_url() {
        return twitter_url;
    }

    public void setTwitter_url(String twitter_url) {
        this.twitter_url = twitter_url;
    }

    public String getPicassa_url() {
        return picassa_url;
    }

    public void setPicassa_url(String picassa_url) {
        this.picassa_url = picassa_url;
    }

    public String getImage_full_path() {
        return image_full_path;
    }

    public void setImage_full_path(String image_full_path) {
        this.image_full_path = image_full_path;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddresslat() {
        return addresslat;
    }

    public void setAddresslat(String addresslat) {
        this.addresslat = addresslat;
    }

    public String getAddresslng() {
        return addresslng;
    }

    public void setAddresslng(String addresslng) {
        this.addresslng = addresslng;
    }

    protected ProviderInfo(Parcel in) {
        userId = in.readString();
        username = in.readString();
        email = in.readString();
        mobile = in.readString();
        isActive = in.readString();
        desc = in.readString();
        country_id = in.readString();
        city_id = in.readString();
        country_name = in.readString();
        city_name = in.readString();
        working_hours = in.readString();
        service_1 = in.readString();
        service_2 = in.readString();
        service_3 = in.readString();
        service_4 = in.readString();
        other_services = in.readString();
        delivery = in.readString();
        facebook_url = in.readString();
        twitter_url = in.readString();
        picassa_url = in.readString();
        image_full_path = in.readString();
        addressName = in.readString();
        addresslat = in.readString();
        addresslng = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(mobile);
        dest.writeString(isActive);
        dest.writeString(desc);
        dest.writeString(country_id);
        dest.writeString(city_id);
        dest.writeString(country_name);
        dest.writeString(city_name);
        dest.writeString(working_hours);
        dest.writeString(service_1);
        dest.writeString(service_2);
        dest.writeString(service_3);
        dest.writeString(service_4);
        dest.writeString(other_services);
        dest.writeString(delivery);
        dest.writeString(facebook_url);
        dest.writeString(twitter_url);
        dest.writeString(picassa_url);
        dest.writeString(image_full_path);
        dest.writeString(addressName);
        dest.writeString(addresslat);
        dest.writeString(addresslng);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProviderInfo> CREATOR = new Parcelable.Creator<ProviderInfo>() {
        @Override
        public ProviderInfo createFromParcel(Parcel in) {
            return new ProviderInfo(in);
        }

        @Override
        public ProviderInfo[] newArray(int size) {
            return new ProviderInfo[size];
        }
    };
}