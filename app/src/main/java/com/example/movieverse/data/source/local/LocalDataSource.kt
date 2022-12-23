package com.example.movieverse.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.data.source.local.room.FilmDao
import com.example.movieverse.utils.SortUtils

class LocalDataSource private constructor(private val mFilmDao: FilmDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(filmDao: FilmDao): LocalDataSource = INSTANCE ?: LocalDataSource(filmDao)
    }

    fun getAllMovies(sort: String): DataSource.Factory<Int, MovieEntity> {
        val query = SortUtils.getSortedQueryMovies(sort)
        return mFilmDao.getMovies(query)
    }

    fun getAllTvShows(sort: String): DataSource.Factory<Int, MovieEntity> {
        val query = SortUtils.getSortedQueryTvShows(sort)
        return mFilmDao.getTvSHows(query)
    }

    fun getMovieById(movieId: Int): LiveData<MovieEntity> = mFilmDao.getMovieById(movieId)
    fun getTvShowById(tvShowId: Int): LiveData<MovieEntity> = mFilmDao.getTvShowById(tvShowId)

    fun getAllFavoriteMovies(sort: String): DataSource.Factory<Int, MovieEntity> {
        val query = SortUtils.getSortedQueryFavoriteMovies(sort)
        return mFilmDao.getFavoriteMovies(query)
    }

    fun getAllFavoriteTvShows(sort: String): DataSource.Factory<Int, MovieEntity> {
        val query = SortUtils.getSortedQueryFavoriteTvShows(sort)
        return mFilmDao.getFavoriteTvShows(query)
    }

    fun insertMovies(movies: List<MovieEntity>) = mFilmDao.insertMovies(movies)

    fun updateMovies(movie: MovieEntity) = mFilmDao.updateMovies(movie)

    fun setMovieFavorite(movie: MovieEntity, newState: Boolean) {
        movie.favorite = newState
        mFilmDao.updateMovies(movie)
    }
}