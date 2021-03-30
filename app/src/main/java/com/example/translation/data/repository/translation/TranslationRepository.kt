package com.example.translation.data.repository.translation

import com.example.translation.data.database.entity.TranslationEntity
import com.example.translation.data.network.response.TranslationResponse
import com.example.translation.domain.SupportedTranslations
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface TranslationRepository {

    fun translateWord(
        word: String,
        translateFrom: SupportedTranslations,
        translateTo: SupportedTranslations
    ): Single<TranslationResponse>

    fun getAllTranslations(): Single<List<TranslationEntity>>

    fun observeTranslationChanges(): Flowable<List<TranslationEntity>>

    fun getAllFavoriteTranslations(): Single<List<TranslationEntity>>

    fun saveTranslation(
        word: String,
        translation: String,
        translateFrom: SupportedTranslations,
        translateTo: SupportedTranslations
    ): Completable

    fun searchWord(word: String): Single<List<TranslationEntity>>

    fun changeTranslationFavoriteState(id: Long, favoriteState: Boolean): Completable

}