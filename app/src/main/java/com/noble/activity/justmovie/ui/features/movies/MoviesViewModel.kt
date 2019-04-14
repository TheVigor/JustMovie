package com.noble.activity.justmovie.ui.features.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.ui.base.BaseViewModel
import com.noble.activity.justmovie.ui.common.SingleLiveEvent
import com.noble.activity.justmovie.ui.errors.EmptyViewMode
import com.noble.activity.justmovie.ui.utils.NetworkUtils
import retrofit2.HttpException

class MoviesViewModel(private val repository: MoviesRepository): BaseViewModel(),
    MoviesContract.ViewModelContract {

    private var page: Int = 0

    private var _loadingState = SingleLiveEvent<Boolean>()
    val loadingState: LiveData<Boolean>
            get() = _loadingState

    private var _errorState = SingleLiveEvent<Int>()
    val errorState: LiveData<Int>
        get() = _errorState

    private var _contentState = SingleLiveEvent<List<Movie>>()
    val contentState: LiveData<List<Movie>>
        get() = _contentState

    override fun movies(movieId: Int, list: String) {
        if (!NetworkUtils.isNetworkConnected()) {
            _errorState.value = EmptyViewMode.MODE_NO_CONNECTION
            return
        }

        page = 1
        addDisposable(repository.movies(movieId, list, page)
            .doOnSubscribe { _loadingState.value = true }
            .doAfterTerminate { _loadingState.value = false }
            .subscribe({
                if (it.isEmpty()) {
                    _errorState.value = EmptyViewMode.MODE_NO_MOVIES
                    return@subscribe
                }
                _contentState.value = it
            }, { _errorState.value = EmptyViewMode.MODE_NO_MOVIES }))
    }

    override fun moviesNext(movieId: Int, list: String) {
        if (NetworkUtils.isNetworkConnected().not()) return

        page++
        addDisposable(repository.movies(movieId, list, page).subscribe { _contentState.value =it })
    }

    override fun movies(keywordId: Int) {
        if (NetworkUtils.isNetworkConnected().not()) {
            _errorState.value = EmptyViewMode.MODE_NO_CONNECTION
            return
        }

        page = 1
        addDisposable(repository.movies(keywordId, page)
            .doOnSubscribe { _loadingState.value = true }
            .doAfterTerminate { _loadingState.value = false }
            .switchIfEmpty { _errorState.value = EmptyViewMode.MODE_NO_MOVIES }
            .subscribe({ _contentState.value = it }, { _errorState.value = EmptyViewMode.MODE_NO_MOVIES }))
    }

    override fun moviesNext(keywordId: Int) {
        if (NetworkUtils.isNetworkConnected().not()) {
            return
        }

        page++
        addDisposable(repository.movies(keywordId, page).subscribe { _contentState.value = it })
    }

    override fun moviesWatchlist(accountId: Int, sessionId: String) {
        /*if (!NetworkUtil.isNetworkConnected()) {
            view?.error(EmptyViewMode.MODE_NO_CONNECTION)
            return
        }*/

        page = 1
        addDisposable(repository.moviesWatchlist(accountId, sessionId, page)
            .doOnSubscribe { _loadingState.value = true }
            .doAfterTerminate { _loadingState.value = false }
            .subscribe({
                if (it.isEmpty()) {
                    _errorState.value = 1
                    return@subscribe
                }
                _contentState.value = it }, { _errorState.value = (it as HttpException).code() }
            )
        )
    }

    override fun moviesWatchlistNext(accountId: Int, sessionId: String) {
        if (!NetworkUtils.isNetworkConnected()) return

        page++
        addDisposable(repository.moviesWatchlist(accountId, sessionId, page)
            .subscribe { _contentState.value = it })
    }

    override fun moviesFavorite(accountId: Int, sessionId: String) {
        /*if (NetworkUtil.isNetworkConnected().not()) {
            view?.error(Throwable("no connection"))
            return
        }*/

        page = 1
        addDisposable(repository.moviesFavorite(accountId, sessionId, page)
            .doOnSubscribe { _loadingState.value = true }
            .doAfterTerminate { _loadingState.value = false }
            .subscribe(
                {
                    if (it.isEmpty()) {
                        _errorState.value = 1
                        return@subscribe
                    }
                    _contentState.value = it },
                { _errorState.value = (it as HttpException).code()}
            )
        )
    }

    override fun moviesFavoriteNext(accountId: Int, sessionId: String) {
        if (NetworkUtils.isNetworkConnected().not()) {
            return
        }

        page++
        addDisposable(repository.moviesFavorite(accountId, sessionId, page)
            .subscribe { _contentState.value = it })
    }


}