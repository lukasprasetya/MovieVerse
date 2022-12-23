package com.example.movieverse.ui.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.movieverse.data.source.MovieAppRepository
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.vo.Resource


class TvShowViewModel(private val movieAppRepository: MovieAppRepository) : ViewModel() {

    fun getTvShows(sort: String): LiveData<Resource<PagedList<MovieEntity>>> =
        movieAppRepository.getAllTvShows(sort)
}