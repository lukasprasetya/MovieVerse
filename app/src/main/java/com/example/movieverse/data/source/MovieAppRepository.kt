package com.example.movieverse.data.source

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movieverse.data.NetworkBoundResource
import com.example.movieverse.data.source.local.LocalDataSource
import com.example.movieverse.data.source.local.entity.MovieEntity
import com.example.movieverse.data.source.remote.ApiResponse
import com.example.movieverse.data.source.remote.RemoteDataSource
import com.example.movieverse.data.source.remote.response.Movie
import com.example.movieverse.data.source.remote.response.TvShow
import com.example.movieverse.utils.AppExecutors
import com.example.movieverse.vo.Resource

class MovieAppRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) :
    MovieAppDataSource {

    companion object {
        @Volatile
        private var instance: MovieAppRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): MovieAppRepository =
            instance ?: synchronized(this) {
                instance ?: MovieAppRepository(remoteData, localData, appExecutors)
            }
    }

    override fun getAllMovies(sort: String): LiveData<Resource<PagedList<MovieEntity>>> {
        return object : NetworkBoundResource<PagedList<MovieEntity>, List<Movie>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<MovieEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllMovies(sort), config).build()
            }

            override fun shouldFetch(data: PagedList<MovieEntity>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<List<Movie>>> {
                return remoteDataSource.getMovies()
            }

            override fun saveCallResult(data: List<Movie>) {
                val movieList = ArrayList<MovieEntity>()
                for (response in data) {
                    val movie = MovieEntity(
                        response.backdropPath,
                        response.overview,
                        response.releaseDate,
                        response.voteAverage,
                        response.id,
                        response.title,
                        response.posterPath,
                        favorite = false,
                        isTvShow = false
                    )
                    movieList.add(movie)
                }
                localDataSource.insertMovies(movieList)
            }
        }.asLiveData()
    }

    override fun getAllTvShows(sort: String): LiveData<Resource<PagedList<MovieEntity>>> {
        return object : NetworkBoundResource<PagedList<MovieEntity>, List<TvShow>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<MovieEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllTvShows(sort), config).build()
            }

            override fun shouldFetch(data: PagedList<MovieEntity>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<List<TvShow>>> {
                return remoteDataSource.getTvShows()
            }

            override fun saveCallResult(data: List<TvShow>) {
                val tvSHowList = ArrayList<MovieEntity>()
                for (response in data) {
                    val tvShow = MovieEntity(
                        response.backdropPath,
                        response.overview,
                        response.firstAirDate,
                        response.voteAverage,
                        response.id,
                        response.name,
                        response.posterPath,
                        favorite = false,
                        isTvShow = true
                    )
                    tvSHowList.add(tvShow)
                }
                localDataSource.insertMovies(tvSHowList)
            }
        }.asLiveData()
    }

    override fun getMovieById(movieId: Int): LiveData<Resource<MovieEntity>> {
        return object : NetworkBoundResource<MovieEntity, List<Movie>>(appExecutors) {
            override fun loadFromDB(): LiveData<MovieEntity> {
                return localDataSource.getMovieById(movieId)
            }

            override fun shouldFetch(data: MovieEntity?): Boolean {
                return data == null
            }

            override fun createCall(): LiveData<ApiResponse<List<Movie>>> {
                return remoteDataSource.getMovies()
            }

            override fun saveCallResult(data: List<Movie>) {
                lateinit var movieEntity: MovieEntity
                for (movie in data) {
                    if (movieId == movie.id) {
                        movieEntity = MovieEntity(
                            movie.backdropPath,
                            movie.overview,
                            movie.releaseDate,
                            movie.voteAverage,
                            movie.id,
                            movie.title,
                            movie.posterPath,
                            favorite = false,
                            isTvShow = false
                        )
                    }
                }
                localDataSource.updateMovies(movieEntity)
            }
        }.asLiveData()
    }

    override fun getTvSHowById(tvShowId: Int): LiveData<Resource<MovieEntity>> {
        return object : NetworkBoundResource<MovieEntity, List<TvShow>>(appExecutors) {
            override fun loadFromDB(): LiveData<MovieEntity> {
                return localDataSource.getTvShowById(tvShowId)
            }

            override fun shouldFetch(data: MovieEntity?): Boolean {
                return data == null
            }

            override fun createCall(): LiveData<ApiResponse<List<TvShow>>> {
                return remoteDataSource.getTvShows()
            }

            override fun saveCallResult(data: List<TvShow>) {
                lateinit var tvShowEntity: MovieEntity

                for (tvShow in data) {
                    if (tvShowId == tvShow.id) {
                        tvShowEntity = MovieEntity(
                            tvShow.backdropPath,
                            tvShow.overview,
                            tvShow.firstAirDate,
                            tvShow.voteAverage,
                            tvShow.id,
                            tvShow.name,
                            tvShow.posterPath,
                            favorite = false,
                            isTvShow = true
                        )
                    }
                }
                localDataSource.updateMovies(tvShowEntity)
            }
        }.asLiveData()
    }

    override fun getFavoriteMovies(sort: String): LiveData<PagedList<MovieEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localDataSource.getAllFavoriteMovies(sort), config).build()
    }

    override fun getFavoriteTvSHows(sort: String): LiveData<PagedList<MovieEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localDataSource.getAllFavoriteTvShows(sort), config).build()

    }

    override fun setFavorite(movie: MovieEntity, state: Boolean) {
        return appExecutors.diskIO().execute {
            localDataSource.setMovieFavorite(movie, state)
        }
    }
}