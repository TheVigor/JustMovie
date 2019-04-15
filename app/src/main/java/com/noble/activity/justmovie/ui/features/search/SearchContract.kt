package com.noble.activity.justmovie.ui.features.search

import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.ui.base.BaseView
import io.reactivex.Observable

interface SearchContract {

    interface ViewContract: BaseView<List<Movie>> {
        fun searchStart()
    }

    interface ViewModelContract {
        fun search(query: String)
        fun nextResults()
    }

    interface RepositoryContract {
        fun search(query: String, page: Int): Observable<List<Movie>>
    }

}