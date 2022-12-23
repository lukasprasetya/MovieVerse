package com.example.movieverse.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.vo.Resource

interface MovieAppDataSource {

    fun getAllMovies(sort: String): LiveData<Resource<PagedList<MovieEntity>>>
    fun getMovieById(movieId: Int): LiveData<Resource<MovieEntity>>

    fun getAllTvShows(sort: String): LiveData<Resource<PagedList<MovieEntity>>>
    fun getTvSHowById(tvShowId: Int): LiveData<Resource<MovieEntity>>

    fun getFavoriteMovies(sort: String): LiveData<PagedList<MovieEntity>>
    fun getFavoriteTvSHows(sort: String): LiveData<PagedList<MovieEntity>>

    fun setFavorite(movie: MovieEntity, state: Boolean)
}