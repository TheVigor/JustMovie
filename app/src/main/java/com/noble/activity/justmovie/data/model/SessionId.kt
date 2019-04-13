package com.noble.activity.justmovie.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SessionId(
    @SerializedName("session_id") val sessionId: String
): Serializable