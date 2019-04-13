package com.noble.activity.justmovie.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Keyword(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
): Serializable