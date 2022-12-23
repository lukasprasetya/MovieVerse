package com.example.movieverse.network

import com.example.movieverse.BuildConfig
import com.example.movieverse.data.source.remote.response.MovieResponse
import com.example.movieverse.data.source.remote.response.TvShowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie?api_key=${BuildConfig.API_KEY}")
    fun getMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Call<MovieResponse>


    @GET("tv?api_key=${BuildConfig.API_KEY}")
    fun getTvShows(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Call<TvShowResponse>

}