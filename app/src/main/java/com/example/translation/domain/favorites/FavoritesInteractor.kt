package com.example.translation.domain.favorites

import com.example.translation.data.repository.translation.TranslationRepository
import com.example.translation.domain.model.FavoriteTranslationModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class FavoritesInteractor(
    private val translationRepository: TranslationRepository
) {

    fun getAllFavoriteTranslations(): Single<List<FavoriteTranslationModel>> {
        return translationRepository.getAllFavoriteTranslations()
            .flatMap { entities ->
                Flowable.fromIterable(entities)
                    .map { FavoritesMapper.mapFromEntity(it) }
                    .toSortedList { o1, o2 -> o2.timestamp.compareTo(o1.timestamp) }
            }
    }

    fun removeFavoriteTranslation(model: FavoriteTranslationModel): Completable {
        return translationRepository.changeTranslationFavoriteState(model.id, false)
    }
}