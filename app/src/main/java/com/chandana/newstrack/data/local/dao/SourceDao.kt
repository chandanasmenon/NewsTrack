package com.chandana.newstrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.chandana.newstrack.data.local.entity.Source
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao {

    @Query("SELECT * FROM source")
    fun getAll(): Flow<List<Source>>

    @Insert
    fun insertAll(articles: List<Source>)

    @Query("DELETE FROM source")
    fun deleteAll()

    @Transaction
    fun deleteAllAndInsertAll(articles: List<Source>) {
        deleteAll()
        return insertAll(articles)
    }

}