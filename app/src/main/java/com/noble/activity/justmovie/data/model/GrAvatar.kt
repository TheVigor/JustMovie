package com.noble.activity.justmovie.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GrAvatar(
    @SerializedName("hash") val hash: String
): Serializable