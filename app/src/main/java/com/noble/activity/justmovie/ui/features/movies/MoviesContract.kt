package com.noble.activity.justmovie.ui.features.movies

import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.ui.base.BaseView
import io.reactivex.Observable

interface MoviesContract {

    interface ViewContract: BaseView<List<Movie>>

    interface ViewModelContract{
        fun movies(movieId: Int = 0, list: String)
        fun moviesNext(movieId: Int = 0, list: String)

        fun movies(keywordId: Int)
        fun moviesNext(keywordId: Int)

        fun moviesWatchlist(accountId: Int, sessionId: String)
        fun moviesWatchlistNext(accountId: Int, sessionId: String)

        fun moviesFavorite(accountId: Int, sessionId: String)
        fun moviesFavoriteNext(accountId: Int, sessionId: String)
    }

    interface RepositoryContract {
        fun movies(movieId: Int, list: String, page: Int): Observable<List<Movie>>
        fun movies(keywordId: Int, page: Int): Observable<List<Movie>>

        fun moviesWatchlist(accountId: Int, sessionId: String, page: Int): Observable<List<Movie>>
        fun moviesFavorite(accountId: Int, sessionId: String, page: Int): Observable<List<Movie>>
    }
}