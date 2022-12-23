package com.example.movieverse.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object SortUtils {

    const val NEWEST = "newest"
    const val BEST_VOTE = "best_vote"
    const val RANDOM = "random"

    fun getSortedQueryMovies(filter: String): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM movieEntities where isTvShow = 0 ")
        when (filter) {
            NEWEST -> {
                simpleQuery.append("ORDER BY releaseDate DESC")
            }
            BEST_VOTE -> {
                simpleQuery.append("ORDER BY voteAverage DESC")
            }
            RANDOM -> {
                simpleQuery.append("ORDER BY RANDOM()")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

    fun getSortedQueryTvShows(filter: String): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM movieEntities where isTvShow = 1 ")
        when (filter) {
            NEWEST -> {
                simpleQuery.append("ORDER BY releaseDate DESC")
            }
            BEST_VOTE -> {
                simpleQuery.append("ORDER BY voteAverage DESC")
            }
            RANDOM -> {
                simpleQuery.append("ORDER BY RANDOM()")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

    fun getSortedQueryFavoriteMovies(filter: String): SimpleSQLiteQuery {
        val simpleQuery =
            StringBuilder().append("SELECT * FROM movieEntities where favorite = 1 and isTvShow = 0 ")
        when (filter) {
            NEWEST -> {
                simpleQuery.append("ORDER BY releaseDate DESC")
            }
            BEST_VOTE -> {
                simpleQuery.append("ORDER BY voteAverage DESC")
            }
            RANDOM -> {
                simpleQuery.append("ORDER BY RANDOM()")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

    fun getSortedQueryFavoriteTvShows(filter: String): SimpleSQLiteQuery {
        val simpleQuery =
            StringBuilder().append("SELECT * FROM movieEntities where favorite = 1 and isTvShow = 1 ")
        when (filter) {
            NEWEST -> {
                simpleQuery.append("ORDER BY releaseDate DESC")
            }
            BEST_VOTE -> {
                simpleQuery.append("ORDER BY voteAverage DESC")
            }
            RANDOM -> {
                simpleQuery.append("ORDER BY RANDOM()")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }

}