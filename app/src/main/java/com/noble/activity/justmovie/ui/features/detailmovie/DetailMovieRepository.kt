package com.noble.activity.justmovie.ui.features.detailmovie

import com.noble.activity.justmovie.BuildConfig.TMDB_API_KEY
import com.noble.activity.justmovie.data.TmdbConfig.CONTENT_TYPE
import com.noble.activity.justmovie.data.model.*
import com.noble.activity.justmovie.data.remote.TmdbApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class DetailMovieRepository(private val service: TmdbApi): DetailMovieContract.RepositoryContract {

    override fun getDetails(movieId: Int): Observable<Movie> =
        service.getDetails(movieId, TMDB_API_KEY, Locale.getDefault().language, Movie.CREDITS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun markFavorite(accountId: Int, sessionId: String, mediaId: Int, favorite: Boolean): Observable<Mark> =
        service.markAsFavorite(CONTENT_TYPE, accountId, TMDB_API_KEY, sessionId, Fave(Movie.MOVIE, mediaId, favorite))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun addWatchlist(accountId: Int, sessionId: String, mediaId: Int, watchlist: Boolean): Observable<Mark> =
        service.addToWatchlist(CONTENT_TYPE, accountId, TMDB_API_KEY, sessionId, Watch(Movie.MOVIE, mediaId, watchlist))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getAccountStates(movieId: Int, sessionId: String): Observable<AccountStates> =
        service.getAccountStates(movieId, TMDB_API_KEY, sessionId, "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}