package com.example.translation.data.repository.translation

import com.example.translation.data.database.TranslationDao
import com.example.translation.data.database.entity.TranslationEntity
import com.example.translation.data.network.TranslationApi
import com.example.translation.data.network.response.TranslationResponse
import com.example.translation.domain.SupportedTranslations
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single

class TranslationRepository(
    private val translationDao: TranslationDao,
    private val translationApi: TranslationApi,
    private val databaseScheduler: Scheduler
) {
    fun translateWord(
        word: String,
        translateFrom: SupportedTranslations,
        translateTo: SupportedTranslations
    ): Single<TranslationResponse> {
        return translationApi.translate(
            word,
            translateTo.key,
            translateFrom.key
        )
    }

    fun getAllTranslations(): Single<List<TranslationEntity>> {
        return translationDao.getAllTranslations()
            .subscribeOn(databaseScheduler)
    }

    fun observeTranslationChanges(): Flowable<List<TranslationEntity>> {
        return translationDao.observeAllTranslations()
            .subscribeOn(databaseScheduler)
    }

    fun getAllFavoriteTranslations(): Single<List<TranslationEntity>> {
        return translationDao.getAllFavoriteTranslations()
            .subscribeOn(databaseScheduler)
    }

    fun saveTranslation(
        word: String,
        translation: String,
        translateFrom: SupportedTranslations,
        translateTo: SupportedTranslations
    ): Completable {
        return Completable.fromAction {
            val existedTranslation = translationDao.searchTranslation(word, translation)
            if (existedTranslation == null) {
                translationDao.insertTranslation(
                    TranslationEntity(
                        originalWord = word,
                        wordTranslation = translation,
                        translatedFrom = translateFrom.key,
                        translatedTo = translateTo.key,
                        timestamp = System.currentTimeMillis(),
                        isFavorite = false
                    )
                )
            }
        }.subscribeOn(databaseScheduler)
    }

    fun searchWord(word: String): Single<List<TranslationEntity>> {
        return translationDao.searchWord(word)
            .subscribeOn(databaseScheduler)
    }

    fun changeTranslationFavoriteState(id: Long, favoriteState: Boolean): Completable {
        return translationDao.updateFavoriteState(id, favoriteState.toInt())
            .subscribeOn(databaseScheduler)
    }

    private fun Boolean.toInt(): Int {
        return if (this) 1 else 0
    }
}