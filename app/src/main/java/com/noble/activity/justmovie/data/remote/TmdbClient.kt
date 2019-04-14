package com.noble.activity.justmovie.data.remote

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.noble.activity.justmovie.data.TmdbConfig.GSON_DATE_FORMAT
import com.noble.activity.justmovie.data.TmdbConfig.TMDB_API_ENDPOINT
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TmdbClient {

    companion object {
        @Volatile
        private var INSTANCE: TmdbApi? = null

        fun getInstance(): TmdbApi {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?:
                Retrofit.Builder().baseUrl(TMDB_API_ENDPOINT)
                    .client(HttpClient.getInstance())
                    .addConverterFactory(GsonConverterFactory.create(gson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create<TmdbApi>(TmdbApi::class.java).also {
                    INSTANCE = it
                }
            }
        }

        fun gson() : Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat(GSON_DATE_FORMAT)
            .create()
    }
}