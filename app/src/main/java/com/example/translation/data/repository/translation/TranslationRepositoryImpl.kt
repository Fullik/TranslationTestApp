package com.example.translation.data.repository.translation

import com.example.translation.data.database.TranslationDao
import com.example.translation.data.database.entity.TranslationEntity
import com.example.translation.data.network.TranslationApi
import com.example.translation.data.network.response.TranslationResponse
import com.example.translation.domain.SupportedTranslations
import com.example.translation.util.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class TranslationRepositoryImpl @Inject constructor(
    private val translationDao: TranslationDao,
    private val translationApi: TranslationApi,
    private val schedulerProvider: SchedulerProvider
) : TranslationRepository {

    override fun translateWord(
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

    override fun observeTranslationChanges(): Flowable<List<TranslationEntity>> {
        return translationDao.getAllTranslations()
            .subscribeOn(schedulerProvider.getDatabaseScheduler())
    }

    override fun saveTranslation(
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
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }.subscribeOn(schedulerProvider.getDatabaseScheduler())
    }

    override fun searchWord(word: String): Single<List<TranslationEntity>> {
        return translationDao.searchWord(word)
            .subscribeOn(schedulerProvider.getDatabaseScheduler())
    }

}