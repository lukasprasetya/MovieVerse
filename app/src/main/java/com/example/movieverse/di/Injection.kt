package com.example.movieverse.di

import android.content.Context
import com.example.movieverse.data.source.MovieAppRepository
import com.example.movieverse.data.source.local.LocalDataSource
import com.example.movieverse.data.source.local.room.FilmDatabase
import com.example.movieverse.data.source.remote.RemoteDataSource
import com.example.movieverse.utils.AppExecutors

object Injection {

    fun provideRepository(context: Context): MovieAppRepository {

        val database = FilmDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance()
        val localDataSource = LocalDataSource.getInstance(database.filmDao())
        val appExecutors = AppExecutors()

        return MovieAppRepository.getInstance(remoteDataSource,localDataSource,appExecutors)
    }
}