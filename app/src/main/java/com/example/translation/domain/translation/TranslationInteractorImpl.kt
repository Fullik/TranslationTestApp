package com.example.translation.domain.translation

import com.example.translation.R
import com.example.translation.data.database.entity.TranslationEntity
import com.example.translation.data.repository.translation.TranslationRepository
import com.example.translation.domain.ResourceManager
import com.example.translation.domain.SupportedTranslations
import com.example.translation.domain.model.AvailableLanguagesModel
import com.example.translation.domain.model.RecentTranslationModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class TranslationInteractorImpl @Inject constructor(
    private val repository: TranslationRepository,
    private val resourceManager: ResourceManager
) : TranslationInteractor {

    override fun getAvailableLanguages(): List<AvailableLanguagesModel> {
        return SupportedTranslations.values()
            .map {
                val nameRes = when (it) {
                    SupportedTranslations.RU -> R.string.ru_translation
                    SupportedTranslations.ENG -> R.string.en_translation
                    SupportedTranslations.DE -> R.string.de_translation
                }
                AvailableLanguagesModel(resourceManager.getString(nameRes), it)
            }
    }

    override fun translateWord(
        word: String,
        translateFrom: SupportedTranslations,
        translateTo: SupportedTranslations
    ): Completable {
        return repository.translateWord(word, translateFrom, translateTo)
            .flatMapCompletable {
                repository.saveTranslation(word, it.translation, translateFrom, translateTo)
            }
    }

    override fun getRecentTranslations(): Flowable<List<RecentTranslationModel>> {
        return repository.observeTranslationChanges()
            .flatMapSingle { entities -> mapEntities(entities) }
    }

    override fun searchWord(word: String): Single<List<RecentTranslationModel>> {
        return repository.searchWord(word)
            .flatMap { mapEntities(it) }
    }

    private fun mapEntities(entities: List<TranslationEntity>) =
        Flowable.fromIterable(entities)
            .map { TranslationMapper.mapFromEntity(it) }
            .toSortedList { o1, o2 -> o2.timestamp.compareTo(o1.timestamp) }

}