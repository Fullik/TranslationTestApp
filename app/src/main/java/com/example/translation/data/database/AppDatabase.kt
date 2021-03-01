package com.example.translation.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.translation.data.database.entity.TranslationEntity

@Database(
    entities = [TranslationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}