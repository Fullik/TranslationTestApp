package com.example.translation.domain.translation

import com.example.translation.domain.SupportedTranslations
import com.example.translation.domain.model.AvailableLanguagesModel
import com.example.translation.domain.model.RecentTranslationModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface TranslationInteractor {

    fun getAvailableLanguages(): List<AvailableLanguagesModel>

    fun translateWord(
        word: String,
        translateFrom: SupportedTranslations,
        translateTo: SupportedTranslations
    ): Completable

    fun getRecentTranslations(): Flowable<List<RecentTranslationModel>>

    fun searchWord(word: String): Single<List<RecentTranslationModel>>

    fun changeFavoriteState(model: RecentTranslationModel): Completable
}