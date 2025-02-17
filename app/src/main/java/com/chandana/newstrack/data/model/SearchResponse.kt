package com.chandana.newstrack.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)