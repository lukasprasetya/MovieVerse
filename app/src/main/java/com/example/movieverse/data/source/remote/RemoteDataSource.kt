package com.example.movieverse.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieverse.data.source.remote.response.Movie
import com.example.movieverse.data.source.remote.response.MovieResponse
import com.example.movieverse.data.source.remote.response.TvShow
import com.example.movieverse.data.source.remote.response.TvShowResponse
import com.example.movieverse.network.ApiConfig
import com.example.movieverse.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    companion object {

        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource = instance ?: synchronized(this) {
            instance ?: RemoteDataSource()
        }
    }

    fun getMovies(): LiveData<ApiResponse<List<Movie>>> {
        EspressoIdlingResource.increment()
        val resultMovies = MutableLiveData<ApiResponse<List<Movie>>>()
        ApiConfig.getApiService().getMovies().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val results = response.body()?.results
                if (results != null) {
                    resultMovies.postValue(ApiResponse.success(results))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e(
                    RemoteDataSource::class.java.simpleName,
                    "onFailure: ${t.message.toString()} "
                )
                EspressoIdlingResource.decrement()
            }
        })
        return resultMovies
    }

    fun getTvShows(): LiveData<ApiResponse<List<TvShow>>> {
        EspressoIdlingResource.increment()
        val resultTvShows = MutableLiveData<ApiResponse<List<TvShow>>>()
        ApiConfig.getApiService().getTvShows().enqueue(object : Callback<TvShowResponse> {
            override fun onResponse(
                call: Call<TvShowResponse>,
                response: Response<TvShowResponse>
            ) {
                val results = response.body()?.results
                if (results != null) {
                    resultTvShows.postValue(ApiResponse.success(results))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<TvShowResponse>, t: Throwable) {
                Log.e(
                    RemoteDataSource::class.java.simpleName,
                    "onFailure: ${t.message.toString()} "
                )
                EspressoIdlingResource.decrement()
            }
        })
        return resultTvShows
    }
}