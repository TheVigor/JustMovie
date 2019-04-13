package com.noble.activity.justmovie.ui.errors

import androidx.annotation.IntDef
import com.noble.activity.justmovie.ui.errors.EmptyViewMode.Companion.MODE_NO_CONNECTION
import com.noble.activity.justmovie.ui.errors.EmptyViewMode.Companion.MODE_NO_HISTORY
import com.noble.activity.justmovie.ui.errors.EmptyViewMode.Companion.MODE_NO_KEYWORDS
import com.noble.activity.justmovie.ui.errors.EmptyViewMode.Companion.MODE_NO_MOVIES
import com.noble.activity.justmovie.ui.errors.EmptyViewMode.Companion.MODE_NO_PEOPLE
import com.noble.activity.justmovie.ui.errors.EmptyViewMode.Companion.MODE_NO_RESULTS
import com.noble.activity.justmovie.ui.errors.EmptyViewMode.Companion.MODE_NO_REVIEWS
import com.noble.activity.justmovie.ui.errors.EmptyViewMode.Companion.MODE_NO_TRAILERS

@IntDef(
    MODE_NO_CONNECTION,
    MODE_NO_MOVIES,
    MODE_NO_PEOPLE,
    MODE_NO_REVIEWS,
    MODE_NO_RESULTS,
    MODE_NO_HISTORY,
    MODE_NO_TRAILERS,
    MODE_NO_KEYWORDS
)
@EmptyViewMode
annotation class EmptyViewMode {
    companion object {
        const val MODE_NO_CONNECTION = 0
        const val MODE_NO_MOVIES = 1
        const val MODE_NO_PEOPLE = 2
        const val MODE_NO_REVIEWS = 3
        const val MODE_NO_RESULTS = 4
        const val MODE_NO_HISTORY = 5
        const val MODE_NO_TRAILERS = 6
        const val MODE_NO_KEYWORDS = 7
    }
}