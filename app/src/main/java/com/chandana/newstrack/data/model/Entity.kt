package com.chandana.newstrack.data.model

import com.google.gson.annotations.SerializedName

data class Entity(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)