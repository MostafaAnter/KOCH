package com.perfect_apps.koch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mostafa_anter on 10/27/16.
 */

public class CitiesResponse {
    @SerializedName("cities")
    private List<Cities> cities;
    public CitiesResponse(){

    }

    public CitiesResponse(List<Cities> cities) {
        this.cities = cities;
    }

    public List<Cities> getCities() {
        return cities;
    }

    public void setCities(List<Cities> cities) {
        this.cities = cities;
    }
}
