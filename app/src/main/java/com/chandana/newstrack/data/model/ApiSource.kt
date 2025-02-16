package com.chandana.newstrack.data.model

import com.chandana.newstrack.data.local.entity.Source
import com.google.gson.annotations.SerializedName

data class ApiSource(
    @SerializedName("category")
    val category: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

fun ApiSource.toSourceEntity(): Source {
    return Source(
        sourceId = 0,
        category = category,
        country = country,
        description = description,
        id = id,
        language = language,
        name = name, url = url
    )
}
