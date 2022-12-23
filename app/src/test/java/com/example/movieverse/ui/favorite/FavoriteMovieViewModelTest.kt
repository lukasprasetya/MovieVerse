package com.example.movieverse.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.example.movieverse.data.source.MovieAppRepository
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.utils.DataDummy
import com.example.movieverse.utils.SortUtils
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoriteMovieViewModelTest {

    private lateinit var favViewModel: FavoriteMovieViewModel
    private val sort = SortUtils.RANDOM

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieAppRepository: MovieAppRepository

    @Mock
    private lateinit var observer: Observer<PagedList<MovieEntity>>

    @Mock
    private lateinit var pagedList: PagedList<MovieEntity>

    @Before
    fun setUp() {
        favViewModel = FavoriteMovieViewModel(movieAppRepository)
    }

    @Test
    fun getFavoriteMovies() {
        val dummyFavMovie = pagedList
        `when`(dummyFavMovie.size).thenReturn(10)
        val favMovie = MutableLiveData<PagedList<MovieEntity>>()
        favMovie.value = dummyFavMovie

        `when`(movieAppRepository.getFavoriteMovies(sort)).thenReturn(favMovie)
        val favMovieEntity = favViewModel.getFavoriteMovies(sort).value
        verify(movieAppRepository).getFavoriteMovies(sort)
        assertNotNull(favMovieEntity)
        assertEquals(10, favMovieEntity?.size)

        favViewModel.getFavoriteMovies(sort).observeForever(observer)
        verify(observer).onChanged(dummyFavMovie)
    }

    @Test
    fun setFavMovies() {
        favViewModel.setFavorite(DataDummy.getDumMov())
        verify(movieAppRepository).setFavorite(DataDummy.getDumMov(), true)
        verifyNoMoreInteractions(movieAppRepository)
    }

    @Test
    fun getFavoriteTvShows() {
        val dummyFavTvSHow = pagedList
        `when`(dummyFavTvSHow.size).thenReturn(10)
        val favTvShow = MutableLiveData<PagedList<MovieEntity>>()
        favTvShow.value = dummyFavTvSHow

        `when`(movieAppRepository.getFavoriteTvSHows(sort)).thenReturn(favTvShow)
        val favTvShowEntity = favViewModel.getFavoriteTvShows(sort).value
        verify(movieAppRepository).getFavoriteTvSHows(sort)
        assertNotNull(favTvShowEntity)
        assertEquals(10, favTvShowEntity?.size)

        favViewModel.getFavoriteTvShows(sort).observeForever(observer)
        verify(observer).onChanged(dummyFavTvSHow)
    }

    @Test
    fun setFavTvShows() {
        favViewModel.setFavorite(DataDummy.getDumTvShow())
        verify(movieAppRepository).setFavorite(DataDummy.getDumTvShow(), true)
        verifyNoMoreInteractions(movieAppRepository)
    }
}