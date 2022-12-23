package com.example.movieverse.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movieverse.data.source.local.LocalDataSource
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.data.source.remote.RemoteDataSource
import com.example.movieverse.utils.*
import com.example.movieverse.vo.Resource
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class MovieAppRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val movieAppRepository = FakeMovieAppRepository(remote, local, appExecutors)

    private val movieResponse = DataDummy.generateDummyMovies()
    private val movieId = movieResponse[0].id
    private val tvShowResponse = DataDummy.generareDummyTvShows()
    private val tvSHowId = tvShowResponse[0].id

    private val random = SortUtils.RANDOM

    @Test
    fun getAllMovies() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        `when`(local.getAllMovies(random)).thenReturn(dataSourceFactory)
        movieAppRepository.getAllMovies(random)

        val movieEntities =
            Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyMovies()))
        verify(local).getAllMovies(random)
        assertNotNull(movieEntities.data)
        assertEquals(movieResponse.size.toLong(), movieEntities.data?.size?.toLong())
    }

    @Test
    fun getAllTvShows() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        `when`(local.getAllTvShows(random)).thenReturn(dataSourceFactory)
        movieAppRepository.getAllTvShows(random)

        val tvShowEntities =
            Resource.success(PagedListUtil.mockPagedList(DataDummy.generareDummyTvShows()))
        verify(local).getAllTvShows(random)
        assertNotNull(tvShowEntities.data)
        assertEquals(tvShowResponse.size.toLong(), tvShowEntities.data?.size?.toLong())
    }

    @Test
    fun getMovieById() {
        val dummyMovie = MutableLiveData<MovieEntity>()
        dummyMovie.value = DataDummy.generateDummyMovies()[0]
        `when`(local.getMovieById(movieId)).thenReturn(dummyMovie)

        val movieEntity = LiveDataTestUtil.getValue(movieAppRepository.getMovieById(movieId)).data
        verify(local).getMovieById(movieId)
        val movieResponse = movieResponse[0]
        assertNotNull(movieEntity)
        if (movieEntity != null) {
            assertEquals(movieResponse.title, movieEntity.title)
            assertEquals(movieResponse.releaseDate, movieEntity.releaseDate)
            assertEquals(movieResponse.overview, movieEntity.overview)
            assertEquals(
                movieResponse.voteAverage,
                movieResponse.voteAverage,
                movieResponse.voteAverage
            )
            assertEquals(movieResponse.backdropPath, movieEntity.backdropPath)
            assertEquals(movieResponse.posterPath, movieEntity.posterPath)
            assertEquals(movieResponse.id, movieEntity.id)
        }
    }

    @Test
    fun getTvShowById() {
        val dummyTvShow = MutableLiveData<MovieEntity>()
        dummyTvShow.value = DataDummy.generareDummyTvShows()[0]
        `when`(local.getTvShowById(tvSHowId)).thenReturn(dummyTvShow)

        val tvShowEntity =
            LiveDataTestUtil.getValue(movieAppRepository.getTvSHowById(tvSHowId)).data
        verify(local).getTvShowById(tvSHowId)
        val tvShowResponse = tvShowResponse[0]
        assertNotNull(tvShowEntity)
        if (tvShowEntity != null) {
            assertEquals(tvShowResponse.title, tvShowEntity.title)
            assertEquals(tvShowResponse.releaseDate, tvShowEntity.releaseDate)
            assertEquals(tvShowResponse.overview, tvShowEntity.overview)
            assertEquals(
                tvShowResponse.voteAverage,
                tvShowResponse.voteAverage,
                tvShowResponse.voteAverage
            )
            assertEquals(tvShowResponse.backdropPath, tvShowEntity.backdropPath)
            assertEquals(tvShowResponse.posterPath, tvShowEntity.posterPath)
            assertEquals(tvShowResponse.id, tvShowEntity.id)
        }
    }

    @Test
    fun getFavoriteMovies() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        `when`(local.getAllFavoriteMovies(random)).thenReturn(dataSourceFactory)
        movieAppRepository.getFavoriteMovies(random)

        val favMovieEntities =
            Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyMovies()))
        verify(local).getAllFavoriteMovies(random)
        assertNotNull(favMovieEntities.data)
        assertEquals(movieResponse.size.toLong(), favMovieEntities.data?.size?.toLong())
    }

    @Test
    fun setFavoriteMovies() {
        movieAppRepository.setFavorite(DataDummy.getDumMov(), true)
        verify(local).setMovieFavorite(DataDummy.getDumMov(), true)
        verifyNoMoreInteractions(local)
    }

    @Test
    fun getFavoriteTvShows() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        `when`(local.getAllFavoriteTvShows(random)).thenReturn(dataSourceFactory)
        movieAppRepository.getFavoriteTvSHows(random)

        val favTvShowEntities =
            Resource.success(PagedListUtil.mockPagedList(DataDummy.generareDummyTvShows()))
        verify(local).getAllFavoriteTvShows(random)
        assertNotNull(favTvShowEntities.data)
        assertEquals(movieResponse.size.toLong(), favTvShowEntities.data?.size?.toLong())
    }

    @Test
    fun setFavoriteTvShows() {
        movieAppRepository.setFavorite(DataDummy.getDumTvShow(), true)
        verify(local).setMovieFavorite(DataDummy.getDumTvShow(), true)
        verifyNoMoreInteractions(local)
    }
}