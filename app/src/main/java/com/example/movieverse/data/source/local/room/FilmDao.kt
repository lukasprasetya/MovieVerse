package com.example.movieverse.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.movieverse.data.source.local.entity.MovieEntity

@Dao
interface FilmDao {

    @RawQuery(observedEntities = [MovieEntity::class])
    fun getMovies(query: SupportSQLiteQuery): DataSource.Factory<Int, MovieEntity>

    @RawQuery(observedEntities = [MovieEntity::class])
    fun getTvSHows(query: SupportSQLiteQuery): DataSource.Factory<Int, MovieEntity>

    @Transaction
    @Query("SELECT * FROM movieEntities where id = :movieId")
    fun getMovieById(movieId: Int): LiveData<MovieEntity>

    @Transaction
    @Query("SELECT * FROM movieEntities where id = :tvShowId")
    fun getTvShowById(tvShowId: Int): LiveData<MovieEntity>

    @RawQuery(observedEntities = [MovieEntity::class])
    fun getFavoriteMovies(query: SupportSQLiteQuery): DataSource.Factory<Int, MovieEntity>

    @RawQuery(observedEntities = [MovieEntity::class])
    fun getFavoriteTvShows(query: SupportSQLiteQuery): DataSource.Factory<Int, MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<MovieEntity>)

    @Update
    fun updateMovies(movie: MovieEntity)
}