package com.chandana.newstrack.data.local

import com.chandana.newstrack.data.local.entity.Source
import kotlinx.coroutines.flow.Flow

interface DatabaseService {

    fun getSources(): Flow<List<Source>>

    fun deleteAllAndInsertAll(sources: List<Source>)

}