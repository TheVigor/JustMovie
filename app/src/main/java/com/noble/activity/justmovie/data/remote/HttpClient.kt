package com.noble.activity.justmovie.data.remote

import okhttp3.OkHttpClient

class HttpClient {
    companion object {
        @Volatile
        private var INSTANCE: OkHttpClient? = null

        fun getInstance(): OkHttpClient {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: OkHttpClient().newBuilder().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}