package com.example.movieverse.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.movieverse.data.source.MovieAppRepository
import com.example.movieverse.data.source.local.entity.MovieEntity

class FavoriteMovieViewModel(private val movieAppRepository: MovieAppRepository) : ViewModel() {

    fun getFavoriteMovies(sort: String): LiveData<PagedList<MovieEntity>> =
        movieAppRepository.getFavoriteMovies(sort)

    fun getFavoriteTvShows(sort: String): LiveData<PagedList<MovieEntity>> =
        movieAppRepository.getFavoriteTvSHows(sort)

    fun setFavorite(movieEntity: MovieEntity) {
        val newState = !movieEntity.favorite
        movieAppRepository.setFavorite(movieEntity, newState)
    }
}