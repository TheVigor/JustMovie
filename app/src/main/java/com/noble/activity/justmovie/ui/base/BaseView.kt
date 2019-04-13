package com.noble.activity.justmovie.ui.base

interface BaseView<T> {
    fun loading(state: Boolean)
    fun content(results: T)
    fun error(code: Int)
}