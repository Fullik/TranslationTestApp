package com.example.translation.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.translation.data.database.entity.TranslationEntity.Companion.COLUMN_ORIGINAL_WORD
import com.example.translation.data.database.entity.TranslationEntity.Companion.COLUMN_WORD_TRANSLATION
import com.example.translation.data.database.entity.TranslationEntity.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(value = [COLUMN_ORIGINAL_WORD, COLUMN_WORD_TRANSLATION])
    ]
)
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,
    @ColumnInfo(name = COLUMN_ORIGINAL_WORD)
    val originalWord: String,
    @ColumnInfo(name = COLUMN_WORD_TRANSLATION)
    val wordTranslation: String,
    @ColumnInfo(name = COLUMN_TRANSLATED_FROM)
    val translatedFrom: String,
    @ColumnInfo(name = COLUMN_TRANSLATED_TO)
    val translatedTo: String,
    @ColumnInfo(name = COLUMN_TIMESTAMP)
    val timestamp: Long
) {
    companion object {
        const val TABLE_NAME = "translation"
        const val COLUMN_ID = "_id"
        const val COLUMN_ORIGINAL_WORD = "original_word"
        const val COLUMN_WORD_TRANSLATION = "word_translation"
        const val COLUMN_TRANSLATED_FROM = "translated_from"
        const val COLUMN_TRANSLATED_TO = "translated_to"
        const val COLUMN_TIMESTAMP = "timestamp"
    }
}