package com.perfect_apps.koch.rest;

import com.perfect_apps.koch.models.CitiesResponse;
import com.perfect_apps.koch.models.Countries;
import com.perfect_apps.koch.models.CountriesResponse;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by mostafa_anter on 10/3/16.
 */

public interface ApiInterface {

    @Multipart
    @POST("register/client")
    Call<ResponseBody> registerClient(@Field("name") String name, @Field("mobile") String mobile,
                                      @Field("email") String email, @Field("password") String password,
                                      @Field("password_confirmation") String password_confirmation,
                                      @Field("desc") String desc, @Part("image") RequestBody image);

    @Multipart
    @POST("register/provider")
    Call<ResponseBody> registerProvider(@Field("name") String name, @Field("mobile") String mobile,
                                        @Field("email") String email, @Field("password") String password,
                                        @Field("password_confirmation") String password_confirmation,
                                        @Field("desc") String desc, @Field("country_id") int country_id,
                                        @Field("city_id") int city_id, @Field("working_hours") String working_hours,
                                        @Field("service_1") String service_1, @Field("service_2") String service_2,
                                        @Field("service_3") String service_3, @Field("service_4") String service_4,
                                        @Field("other_services") String other_services, @Field("facebook_url") String facebook_url,
                                        @Field("twitter_url") String twitter_url, @Field("picassa_url") String picassa_url,
                                        @Part("image") RequestBody image);

    @POST("login/provider")
    Call<ResponseBody> loginProvider(@Field("email") String email, @Field("password") String password);

    @POST("login/client")
    Call<ResponseBody> loginClient(@Field("email") String email, @Field("password") String password);

    @POST("info/client")
    Call<ResponseBody> getClientInfo(@Field("user_id") String user_id);

    @POST("info/provider")
    Call<ResponseBody> getProviderInfo(@Field("user_id") String user_id);

    @POST("set_location/provider")
    Call<ResponseBody> addProviderLocation(@Field("email") String email, @Field("password") String password,
                                           @Field("lat") double lat, @Field("lng") double lng,
                                           @Field("name") String locationName);

    @POST("set_location/client")
    Call<ResponseBody> addClientLocation(@Field("email") String email, @Field("password") String password,
                                         @Field("lat") double lat, @Field("lng") double lng,
                                         @Field("name") String locationName);

    @GET("get_location/client")
    Call<ResponseBody> getClientLocation(@Query("email") String email, @Query("password") String password);

    @GET("get_location/provider")
    Call<ResponseBody> getProviderLocation(@Query("email") String email, @Query("password") String password);

    @GET("registration/lists/countries")
    Call<CountriesResponse> getCountries();

    @GET("registration/lists/cities")
    Call<CitiesResponse> getCities(@Query("country") String countryId);


}