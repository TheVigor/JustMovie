package com.noble.activity.justmovie.ui.errors

import androidx.annotation.IntDef
import com.noble.activity.justmovie.ui.errors.Error.Companion.ERROR_AUTH_WITH_LOGIN
import com.noble.activity.justmovie.ui.errors.Error.Companion.ERROR_CONNECTION_NO_TOKEN
import com.noble.activity.justmovie.ui.errors.Error.Companion.ERROR_NOT_FOUND
import com.noble.activity.justmovie.ui.errors.Error.Companion.ERROR_UNAUTHORIZED
import com.noble.activity.justmovie.ui.errors.Error.Companion.ERR_NO_CONNECTION
import com.noble.activity.justmovie.ui.errors.Error.Companion.ERR_NO_MOVIES

@IntDef(
    ERR_NO_CONNECTION,
    ERR_NO_MOVIES,
    ERROR_UNAUTHORIZED,
    ERROR_CONNECTION_NO_TOKEN,
    ERROR_AUTH_WITH_LOGIN,
    ERROR_NOT_FOUND
)
@Error
annotation class Error {
    companion object {
        const val ERR_NO_CONNECTION = -100
        const val ERR_NO_MOVIES = -101
        const val ERROR_UNAUTHORIZED = 0
        const val ERROR_CONNECTION_NO_TOKEN = 2
        const val ERROR_AUTH_WITH_LOGIN = 3
        const val ERROR_NOT_FOUND = 4
    }
}