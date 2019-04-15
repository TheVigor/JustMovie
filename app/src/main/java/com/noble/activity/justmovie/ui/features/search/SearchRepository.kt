package com.noble.activity.justmovie.ui.features.search

import com.noble.activity.justmovie.BuildConfig.TMDB_API_KEY
import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.data.remote.TmdbApi
import com.noble.activity.justmovie.ui.MovieApp
import com.noble.activity.justmovie.ui.utils.AdultUtils
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class SearchRepository(private val service: TmdbApi): SearchContract.RepositoryContract {
    override fun search(query: String, page: Int): Observable<List<Movie>> =
            service.searchMovies(TMDB_API_KEY, Locale.getDefault().language, query, page,
                AdultUtils.includeAdult(MovieApp.appContext), "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.movies }


    companion object {
        @Volatile
        private var INSTANCE: SearchRepository? = null

        fun getInstance(service: TmdbApi): SearchRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SearchRepository(service).also {
                    INSTANCE = it
                }
            }
        }
    }

}