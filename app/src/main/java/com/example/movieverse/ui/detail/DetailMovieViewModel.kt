package com.example.movieverse.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.movieverse.data.source.MovieAppRepository
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.vo.Resource

class DetailMovieViewModel(private val movieAppRepository: MovieAppRepository) : ViewModel() {

    private var movieId = MutableLiveData<Int>()
    private var tvShowId = MutableLiveData<Int>()

    fun selectedMovieId(movieId: Int) {
        this.movieId.value = movieId
    }

    fun selectedTvShowId(tvShowId: Int) {
        this.tvShowId.value = tvShowId
    }

    var getMovieDetail: LiveData<Resource<MovieEntity>> =
        Transformations.switchMap(movieId) { mMovieId ->
            movieAppRepository.getMovieById(mMovieId)
        }

    var getTvShowDetail: LiveData<Resource<MovieEntity>> =
        Transformations.switchMap(tvShowId) { mTvShowId ->
            movieAppRepository.getTvSHowById(mTvShowId)
        }

    fun setFavoriteMovie() {
        val movieResource = getMovieDetail.value?.data
        if (movieResource != null) {
            val newState = !movieResource.favorite
            movieAppRepository.setFavorite(movieResource, newState)
        }
    }

    fun setFavoriteTvShow() {
        val tvShowResource = getTvShowDetail.value?.data
        if (tvShowResource != null) {
            val newState = !tvShowResource.favorite
            movieAppRepository.setFavorite(tvShowResource, newState)
        }
    }
}