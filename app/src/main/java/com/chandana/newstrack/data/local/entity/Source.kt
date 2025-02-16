package com.chandana.newstrack.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "source")
data class Source(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("source_id")
    val sourceId: Int = 0,
    @ColumnInfo("category")
    val category: String = "",
    @ColumnInfo("country")
    val country: String = "",
    @ColumnInfo("description")
    val description: String = "",
    @ColumnInfo("id")
    val id: String = "",
    @ColumnInfo("language")
    val language: String = "",
    @ColumnInfo("name")
    val name: String = "",
    @ColumnInfo("url")
    val url: String = ""
)
