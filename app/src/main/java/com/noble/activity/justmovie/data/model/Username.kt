package com.noble.activity.justmovie.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Username(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("request_token") val requestToken: String
): Serializable