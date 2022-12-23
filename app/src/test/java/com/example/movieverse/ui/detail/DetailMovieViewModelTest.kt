package com.example.movieverse.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.movieverse.data.source.MovieAppRepository
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.utils.DataDummy
import com.example.movieverse.vo.Resource
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailMovieViewModelTest {

    private lateinit var detailMovieViewModel: DetailMovieViewModel

    private val dummyMovie = DataDummy.generateDummyMovies()[0]
    private val movieId = dummyMovie.id
    private val dummyTvShow = DataDummy.generareDummyTvShows()[0]
    private val tvShowId = dummyTvShow.id

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieAppRepository: MovieAppRepository

    @Mock
    private lateinit var movieObserver: Observer<Resource<MovieEntity>>

    @Mock
    private lateinit var tvShowObserver: Observer<Resource<MovieEntity>>


    @Before
    fun setUp() {
        detailMovieViewModel = DetailMovieViewModel(movieAppRepository)
        detailMovieViewModel.selectedMovieId(movieId)
        detailMovieViewModel.selectedTvShowId(tvShowId)
    }

    @Test
    fun getMovieDetail() {
        val movieDetail = Resource.success(dummyMovie)
        val movie = MutableLiveData<Resource<MovieEntity>>()
        movie.value = movieDetail
        `when`(movieAppRepository.getMovieById(movieId)).thenReturn(movie)
        detailMovieViewModel.getMovieDetail.observeForever(movieObserver)
        verify(movieObserver).onChanged(movieDetail)
    }

    @Test
    fun getTvShowDetail() {
        val tvShowDetail = Resource.success(dummyTvShow)
        val tvShow = MutableLiveData<Resource<MovieEntity>>()
        tvShow.value = tvShowDetail
        `when`(movieAppRepository.getTvSHowById(tvShowId)).thenReturn(tvShow)
        detailMovieViewModel.getTvShowDetail.observeForever(tvShowObserver)
        verify(tvShowObserver).onChanged(tvShowDetail)
    }

    @Test
    fun setFavMovies() {
        val dummyMovie = Resource.success(DataDummy.getDumMov())
        val movie = MutableLiveData<Resource<MovieEntity>>()
        movie.value = dummyMovie

        detailMovieViewModel.getMovieDetail = movie
        detailMovieViewModel.setFavoriteMovie()

        verify(movieAppRepository).setFavorite(movie.value!!.data as MovieEntity, true)
        verifyNoMoreInteractions(movieObserver)
    }

    @Test
    fun setFavTvShows() {
        val dummyTvShow = Resource.success(DataDummy.getDumTvShow())
        val tvShow = MutableLiveData<Resource<MovieEntity>>()
        tvShow.value = dummyTvShow

        detailMovieViewModel.getTvShowDetail = tvShow
        detailMovieViewModel.setFavoriteTvShow()

        verify(movieAppRepository).setFavorite(tvShow.value!!.data as MovieEntity, true)
        verifyNoMoreInteractions(movieObserver)
    }
}