package com.example.movieverse.ui.tvshow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.example.movieverse.data.source.MovieAppRepository
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.utils.SortUtils
import com.example.movieverse.vo.Resource
import com.nhaarman.mockitokotlin2.verify
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
class TvShowViewModelTest {

    private lateinit var tvShowViewModel: TvShowViewModel
    private val sort = SortUtils.RANDOM

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieAppRepository: MovieAppRepository

    @Mock
    private lateinit var observer: Observer<Resource<PagedList<MovieEntity>>>

    @Mock
    private lateinit var pagedList : PagedList<MovieEntity>

    @Before
    fun setUp() {
        tvShowViewModel = TvShowViewModel(movieAppRepository)
    }

    @Test
    fun getTvShow() {
        val dummyTvShow = Resource.success(pagedList)
        `when`(dummyTvShow.data?.size).thenReturn(10)
        val tvShow = MutableLiveData<Resource<PagedList<MovieEntity>>>()
        tvShow.value = dummyTvShow

        `when`(movieAppRepository.getAllTvShows(sort)).thenReturn(tvShow)
        val tvShowEntity = tvShowViewModel.getTvShows(sort).value?.data
        verify(movieAppRepository).getAllTvShows(sort)
        assertNotNull(tvShowEntity)
        assertEquals(10, tvShowEntity?.size)

        tvShowViewModel.getTvShows(sort).observeForever(observer)
        verify(observer).onChanged(dummyTvShow)
    }
}