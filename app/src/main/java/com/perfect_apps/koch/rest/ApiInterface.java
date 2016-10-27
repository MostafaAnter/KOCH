package com.perfect_apps.koch.rest;

import com.perfect_apps.koch.models.CitiesResponse;
import com.perfect_apps.koch.models.Countries;
import com.perfect_apps.koch.models.CountriesResponse;

import java.util.List;

import okhttp3.MultipartBody;
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
    Call<ResponseBody> registerClient(@Part("name") RequestBody name, @Part("mobile") RequestBody mobile,
                                      @Part("email") RequestBody email, @Part("password") RequestBody password,
                                      @Part("password_confirmation") RequestBody password_confirmation,
                                      @Part("desc") RequestBody desc, @Part("image")MultipartBody.Part image);

    @Multipart
    @POST("register/provider")
    Call<ResponseBody> registerProvider(@Part("name") RequestBody name, @Part("mobile") RequestBody mobile,
                                        @Part("email") RequestBody email, @Part("password") RequestBody password,
                                        @Part("password_confirmation") RequestBody password_confirmation,
                                        @Part("desc") RequestBody desc, @Part("country_id") RequestBody country_id,
                                        @Part("city_id") RequestBody city_id, @Part("working_hours") RequestBody working_hours,
                                        @Part("service_1") RequestBody service_1, @Part("service_2") RequestBody service_2,
                                        @Part("service_3") RequestBody service_3, @Part("service_4") RequestBody service_4,
                                        @Part("other_services") RequestBody other_services, @Part("facebook_url") RequestBody facebook_url,
                                        @Part("twitter_url") RequestBody twitter_url, @Part("picassa_url") RequestBody picassa_url,
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