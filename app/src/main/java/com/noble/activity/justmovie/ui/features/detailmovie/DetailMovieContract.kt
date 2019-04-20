package com.noble.activity.justmovie.ui.features.detailmovie

import com.noble.activity.justmovie.data.model.AccountStates
import com.noble.activity.justmovie.data.model.Mark
import com.noble.activity.justmovie.data.model.Movie
import io.reactivex.Observable

interface DetailMovieContract {

    interface ViewContract {
        fun movie(movie: Movie)

        fun setURLs(imdbId: String, homepage: String)
        fun setStates(fave: Boolean, watch : Boolean)
        fun onFavoriteChanged(mark: Mark)
        fun onWatchListChanged(mark: Mark)
        fun setCredits(casts: String, directors: String, writers: String, producers: String)
        fun setConnectionError()
        fun showComplete(movie: Movie)
    }

    interface ViewModelContract {
        fun getDetails(sessionId: String, movieId: Int)
        fun markFavorite(sessionId: String, accountId: Int, mediaId: Int, favorite: Boolean)
        fun addWatchlist(sessionId: String, accountId: Int, mediaId: Int, watchlist: Boolean)
        fun getAccountStates(sessionId: String, movieId: Int)
    }

    interface RepositoryContract {
        fun getDetails(movieId: Int): Observable<Movie>
        fun markFavorite(accountId: Int, sessionId: String, mediaId: Int, favorite: Boolean): Observable<Mark>
        fun addWatchlist(accountId: Int, sessionId: String, mediaId: Int, watchlist: Boolean): Observable<Mark>
        fun getAccountStates(movieId: Int, sessionId: String): Observable<AccountStates>
    }
}