package com.noble.activity.justmovie.ui.features.movies

import com.noble.activity.justmovie.BuildConfig.TMDB_API_KEY
import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.data.model.MoviesResponse
import com.noble.activity.justmovie.data.remote.TmdbApi
import com.noble.activity.justmovie.ui.MovieApp
import com.noble.activity.justmovie.ui.utils.AdultUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class MoviesRepository(private val service: TmdbApi): MoviesContract.RepositoryContract {

    override fun movies(movieId: Int, list: String, page: Int): Observable<List<Movie>> {
        val response = if (movieId == 0)
            service.movies(list, TMDB_API_KEY, Locale.getDefault().language, page)
        else
            service.moviesById(movieId, list, TMDB_API_KEY, Locale.getDefault().language, page)

        return response
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.movies }
    }

    override fun movies(keywordId: Int, page: Int): Observable<List<Movie>> =
        service.moviesByKeyword(keywordId, TMDB_API_KEY, Locale.getDefault().language,
            AdultUtil.includeAdult(MovieApp.appContext), page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.movies }

    override fun moviesWatchlist(accountId: Int, sessionId: String, page: Int): Observable<List<Movie>> =
        service.moviesWatchlist(accountId, TMDB_API_KEY, sessionId, Locale.getDefault().language,
            MoviesResponse.ASC, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.movies }

    override fun moviesFavorite(accountId: Int, sessionId: String, page: Int): Observable<List<Movie>> =
        service.moviesFavorite(accountId, TMDB_API_KEY, sessionId, Locale.getDefault().language,
            MoviesResponse.ASC, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.movies }
}