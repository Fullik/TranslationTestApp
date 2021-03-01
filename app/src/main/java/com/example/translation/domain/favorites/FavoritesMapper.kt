package com.example.translation.domain.favorites

import com.example.translation.data.database.entity.TranslationEntity
import com.example.translation.domain.model.FavoriteTranslationModel

object FavoritesMapper {
    fun mapFromEntity(entity: TranslationEntity): FavoriteTranslationModel {
        return FavoriteTranslationModel(
            entity.id,
            entity.originalWord,
            entity.wordTranslation,
            entity.timestamp
        )
    }
}