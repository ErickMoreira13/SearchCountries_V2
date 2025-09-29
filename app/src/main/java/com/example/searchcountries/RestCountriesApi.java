package com.example.searchcountries;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestCountriesApi {
    @GET("v3.1/name/{name}")
    Call<List<CountryResponse>> getCountriesByName(@Path("name") String name);
}
