package com.perfect_apps.koch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mostafa_anter on 10/27/16.
 */

public class CountriesResponse {
    @SerializedName("countries")
    private List<Countries> countries;

    public List<Countries> getCountries() {
        return countries;
    }

    public void setCountries(List<Countries> countries) {
        this.countries = countries;
    }

    public CountriesResponse(List<Countries> countries) {
        this.countries = countries;
    }

    public CountriesResponse(){

    }
}
