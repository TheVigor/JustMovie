package com.noble.activity.justmovie.ui.features.detailmovie

import androidx.lifecycle.LiveData
import com.noble.activity.justmovie.data.model.AccountStates
import com.noble.activity.justmovie.data.model.CreditsResponse
import com.noble.activity.justmovie.data.model.Crew.Companion.DIRECTING
import com.noble.activity.justmovie.data.model.Crew.Companion.PRODUCTION
import com.noble.activity.justmovie.data.model.Crew.Companion.WRITING
import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.ui.base.BaseViewModel
import com.noble.activity.justmovie.ui.common.SingleLiveEvent
import com.noble.activity.justmovie.ui.errors.EmptyViewMode
import com.noble.activity.justmovie.ui.utils.NetworkUtils
import io.reactivex.observers.DisposableObserver
import java.util.ArrayList

class DetailMovieViewModel(
    private val repository: DetailMovieContract.RepositoryContract
): BaseViewModel(), DetailMovieContract.ViewModelContract {


    private var _errorState = SingleLiveEvent<Int>()
    val errorState: LiveData<Int>
        get() = _errorState

    private var _movieState = SingleLiveEvent<Movie>()
    val movieState: LiveData<Movie>
        get() = _movieState


    override fun getDetails(sessionId: String, movieId: Int) {
        if (NetworkUtils.isNetworkConnected().not()) {
            _errorState.value = EmptyViewMode.MODE_NO_CONNECTION
            return
        }

        addDisposable(repository.getDetails(movieId)
            .subscribeWith(object: DisposableObserver<Movie>() {
                override fun onNext(movie: Movie) {
                    fixCredits(movie.credits!!)
                    _movieState.value = movie

//                    if (movie != null) {
//                        if (movie.homepage != null) {
//                            view.setURLs(movie.imdbId, movie.homepage)
//                        }
//
//                        fixCredits(movie.credits!!)
//                        view.showComplete(movie)
//                    }
                }

                override fun onError(e: Throwable) {
                    _errorState.value = EmptyViewMode.MODE_NO_CONNECTION
                }

                override fun onComplete() {
                    //getAccountStates(sessionId, movieId)
                }
            }))
    }

//    override fun markFavorite(sessionId: String, accountId: Int, mediaId: Int, favorite: Boolean) {
//        if (NetworkUtils.isNetworkConnected().not()) return
//
//        addDisposable(repository.markFavorite(accountId, sessionId, mediaId, favorite)
//            .subscribe({ view.onFavoriteChanged(it) }, { _errorState.value = EmptyViewMode.MODE_NO_CONNECTION }))
//    }

//    override fun addWatchlist(sessionId: String, accountId: Int, mediaId: Int, watchlist: Boolean) {
//        if (NetworkUtils.isNetworkConnected().not()) return
//
//        addDisposable(repository.addWatchlist(accountId, sessionId, mediaId, watchlist)
//            .subscribe({ view.onWatchListChanged(it) }, { _errorState.value = EmptyViewMode.MODE_NO_CONNECTION }))
//    }

//    override fun getAccountStates(sessionId: String, movieId: Int) {
//        addDisposable(repository.getAccountStates(movieId, sessionId)
//            .subscribeWith(object: DisposableObserver<AccountStates>() {
//                override fun onNext(states: AccountStates) {
//                    view.setStates(states.favorite, states.watchlist)
//                }
//
//                override fun onError(e: Throwable) {
//                    // Fixme: Rated object has an error.
//                }
//
//                override fun onComplete() {}
//            }))
//    }

    private fun fixCredits(credits: CreditsResponse) {
        val actorsBuilder = StringBuilder()
        for (cast in credits.cast) {
            actorsBuilder.append(cast.name)
            if (cast != credits.cast[credits.cast.size - 1]) {
                actorsBuilder.append(", ")
            }
        }

        val directors = ArrayList<String>()
        val writers = ArrayList<String>()
        val producers = ArrayList<String>()
        for (crew in credits.crew) {
            when (crew.department) {
                DIRECTING -> directors.add(crew.name)
                WRITING -> writers.add(crew.name)
                PRODUCTION -> producers.add(crew.name)
            }
        }

        val directorsBuilder = StringBuilder()
        for (i in directors.indices) {
            directorsBuilder.append(directors[i])
            if (i != directors.size - 1) {
                directorsBuilder.append(", ")
            }
        }

        val writersBuilder = StringBuilder()
        for (i in writers.indices) {
            writersBuilder.append(writers[i])
            if (i != writers.size - 1) {
                writersBuilder.append(", ")
            }
        }

        val producersBuilder = StringBuilder()
        for (i in producers.indices) {
            producersBuilder.append(producers[i])
            if (i != producers.size - 1) {
                producersBuilder.append(", ")
            }
        }

//        view.setCredits(actorsBuilder.toString(), directorsBuilder.toString(),
//            writersBuilder.toString(), producersBuilder.toString())
    }



}