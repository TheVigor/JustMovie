package com.noble.activity.justmovie.ui.features.search

import androidx.lifecycle.LiveData
import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.ui.base.BaseViewModel
import com.noble.activity.justmovie.ui.common.SingleLiveEvent
import com.noble.activity.justmovie.ui.errors.EmptyViewMode
import com.noble.activity.justmovie.ui.utils.NetworkUtils

class SearchViewModel(private val repository: SearchRepository): BaseViewModel(), SearchContract.ViewModelContract {

    private var page: Int = 0
    private var currentQuery: String? = null

    private var _searchStartState = SingleLiveEvent<Unit>()
    val searchStartState: LiveData<Unit>
        get() = _searchStartState

    private var _loadingState = SingleLiveEvent<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    private var _errorState = SingleLiveEvent<Int>()
    val errorState: LiveData<Int>
        get() = _errorState

    private var _contentState = SingleLiveEvent<List<Movie>>()
    val contentState: LiveData<List<Movie>>
        get() = _contentState

    override fun search(query: String) {
        currentQuery = query
        _searchStartState.value = Unit

        if (!NetworkUtils.isNetworkConnected()) {
            _errorState.value = EmptyViewMode.MODE_NO_CONNECTION
        }

        page = 1
        addDisposable(repository.search(query, page)
            .doOnSubscribe { _loadingState.value = true }
            .doAfterTerminate { _loadingState.value = false }
            .subscribe({
                if (it.isEmpty()) {
                    _errorState.value = EmptyViewMode.MODE_NO_RESULTS
                    return@subscribe
                }
                _contentState.value = it
            }, { _errorState.value = EmptyViewMode.MODE_NO_RESULTS }))

    }

    override fun nextResults() {
        if (!NetworkUtils.isNetworkConnected()) return
        if (currentQuery == null) return

        page++
        addDisposable(repository.search(currentQuery!!, page).subscribe { _contentState.value = it })
    }

}