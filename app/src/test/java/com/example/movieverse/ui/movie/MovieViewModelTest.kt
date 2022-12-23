package com.example.movieverse.ui.movie

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
class MovieViewModelTest {

    private lateinit var movieViewModel: MovieViewModel
    private val sort = SortUtils.RANDOM

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieAppRepository: MovieAppRepository

    @Mock
    private lateinit var observer: Observer<Resource<PagedList<MovieEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<MovieEntity>

    @Before
    fun setUp() {
        movieViewModel = MovieViewModel(movieAppRepository)
    }

    @Test
    fun getMovie() {
        val dummyMovie = Resource.success(pagedList)
        `when`(dummyMovie.data?.size).thenReturn(10)
        val movie = MutableLiveData<Resource<PagedList<MovieEntity>>>()
        movie.value = dummyMovie

        `when`(movieAppRepository.getAllMovies(sort)).thenReturn(movie)
        val movieEntity = movieViewModel.getMovies(sort).value?.data
        verify(movieAppRepository).getAllMovies(sort)
        assertNotNull(movieEntity)
        assertEquals(10, movieEntity?.size)

        movieViewModel.getMovies(sort).observeForever(observer)
        verify(observer).onChanged(dummyMovie)
    }
}