package com.noble.activity.justmovie.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import com.noble.activity.justmovie.ui.MovieApp

object NetworkUtils {

    fun isNetworkConnected(): Boolean {
        val connectivityManager = MovieApp.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }
}