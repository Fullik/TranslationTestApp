package com.example.translation.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.translation.data.database.entity.TranslationEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface TranslationDao {

    @Query("SELECT * FROM ${TranslationEntity.TABLE_NAME}")
    fun observeAllTranslations(): Flowable<List<TranslationEntity>>

    @Query("SELECT * FROM ${TranslationEntity.TABLE_NAME}")
    fun getAllTranslations(): Single<List<TranslationEntity>>

    @Query("""SELECT * FROM ${TranslationEntity.TABLE_NAME} 
        WHERE ${TranslationEntity.COLUMN_IS_FAVORITE} = 1""")
    fun getAllFavoriteTranslations(): Single<List<TranslationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTranslation(translation: TranslationEntity): Long

    @Query(
        """
        SELECT * FROM ${TranslationEntity.TABLE_NAME} 
        WHERE ${TranslationEntity.COLUMN_ORIGINAL_WORD} LIKE :originalWord
        AND ${TranslationEntity.COLUMN_WORD_TRANSLATION} LIKE :translationWord
        """
    )
    fun searchTranslation(originalWord: String, translationWord: String): TranslationEntity?

    @Query(
        """
        SELECT * FROM ${TranslationEntity.TABLE_NAME} 
        WHERE ${TranslationEntity.COLUMN_ORIGINAL_WORD} LIKE :word
        OR ${TranslationEntity.COLUMN_WORD_TRANSLATION} LIKE :word
        """
    )
    fun searchWord(word: String): Single<List<TranslationEntity>>

    @Query(
        """
            UPDATE ${TranslationEntity.TABLE_NAME} 
            SET ${TranslationEntity.COLUMN_IS_FAVORITE} = :favoriteState
            WHERE ${TranslationEntity.COLUMN_ID} = :id
        """
    )
    fun updateFavoriteState(id: Long, favoriteState: Int): Completable
}