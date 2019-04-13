package com.noble.activity.justmovie.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Token(
    @SerializedName("success") val success: Boolean,
    @SerializedName("expires_at") val date: String,
    @SerializedName("request_token") val requestToken: String
): Serializable