package com.example.translation.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.translation.data.database.entity.TranslationEntity

object Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `${TranslationEntity.TABLE_NAME}` 
            (`${TranslationEntity.COLUMN_ID}` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
            `${TranslationEntity.COLUMN_ORIGINAL_WORD}` TEXT NOT NULL, 
            `${TranslationEntity.COLUMN_WORD_TRANSLATION}` TEXT NOT NULL, 
            `${TranslationEntity.COLUMN_TRANSLATED_FROM}` TEXT NOT NULL, 
            `${TranslationEntity.COLUMN_TRANSLATED_TO}` TEXT NOT NULL, 
            `${TranslationEntity.COLUMN_TIMESTAMP}` INTEGER NOT NULL)
        """)
        database.execSQL(
            """
                ALTER TABLE `${TranslationEntity.TABLE_NAME}` 
                ADD COLUMN `${TranslationEntity.COLUMN_IS_FAVORITE}`
                INTEGER NOT NULL DEFAULT '0'
            """
        )
    }
}