package com.example.msi.portali.api;

import com.example.msi.portali.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(

            @Query("country") String country,
            @Query("apikey") String apiKey


    );
    @GET("everything")
    Call<News> changeNews(
            @Query("q") String bitcoin,
            @Query("apikey") String apiKey
    );
}
