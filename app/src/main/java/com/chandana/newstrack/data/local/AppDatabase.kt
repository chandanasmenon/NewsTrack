package com.chandana.newstrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chandana.newstrack.data.local.dao.SourceDao
import com.chandana.newstrack.data.local.entity.Source

@Database(entities = [Source::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sourceDao(): SourceDao
}